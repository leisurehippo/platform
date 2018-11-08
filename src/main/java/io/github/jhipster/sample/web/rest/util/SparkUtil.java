package io.github.jhipster.sample.web.rest.util;


import org.apache.ivy.util.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.ml.feature.RFormula;
import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import io.github.jhipster.sample.service.util.FileUtil;
import io.github.jhipster.sample.web.rest.support.DataType;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SparkUtil {

    private static SparkSession spark;
    private static HDFSFileUtil hdfsFileUtil;

    static {
        try{
            FactoryUtil factoryUtil = FactoryUtil.getFactory();
            hdfsFileUtil = factoryUtil.getHDFSUtil();
            Properties properties = new Properties();
            properties.load(SparkUtil.class.getResourceAsStream("/cluster.properties"));
            String master = properties.getProperty("spark_master_ip");
            String port = properties.getProperty("spark_port");
            /*
            SparkConf conf = new SparkConf().setAppName("data-platform").setMaster("spark://" + master + ":" + port)
            					.set("spark.sql.warehouse.dir", "/spark-warehouse/");
            conf.set("spark.debug.maxToStringFields", "100");
            */
            SparkConf conf = new SparkConf()
    	  			.setAppName("data-platform")
    				.setMaster("local")
    				.set("spark.sql.warehouse.dir", "/spark-warehouse/");
            conf.set("spark.sql.crossJoin.enabled", "true");
            //String jarPath = properties.getProperty("jar_path");
           // String jarPath = hdfsFileUtil.master() + "/user/hadoop/runJars/jhipster-sample-application-0.0.1-SNAPSHOT.jar";
            //conf.setJars(new String[]{jarPath});
            spark = SparkSession.builder().config(conf).getOrCreate();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public SparkSession getSession(){
    	return spark;
    }

    public Dataset<Row> readFromHDFS(String path, String dataFormat) throws Exception {
        if(!hdfsFileUtil.checkFile(hdfsFileUtil.HDFSPath(path))) {
            throw new FileNotFoundException(hdfsFileUtil.HDFSPath(path) + "not found on hadoop!");
        }
        return spark.read().option("delimiter", " ").option("inferSchema",true).option("header", true).format(dataFormat).load(hdfsFileUtil.HDFSPath(path));
    }

    public Dataset<Row> readFromLocal(String path, String dataFormat) throws Exception{
    	path = FileUtil.ProjectPathPrefix + path;
        if (!new File(path).exists())
            throw new FileNotFoundException(path + " not found on local!");
        return spark.read().option("delimiter", " ").option("inferSchema",true).option("header", true).format(dataFormat).load(path);
    }

    public Dataset<Row> readFromSQL(String sql) {
        return spark.sql(sql);
    }

    public Dataset<Row> transformData(Dataset<Row> dataset, String[] featureCols, String labelCol) throws Exception{
        String [] columns = dataset.columns();
        List<String> columnList = java.util.Arrays.asList(columns);
        for(String featureCol : featureCols){
            if (!columnList.contains(featureCol))
                throw new Exception(featureCol + " not in data column!");
        	if(featureCol.equals("features")){
        		return dataset;
        	}
        }
        if(!labelCol.equals("") && !columnList.contains(labelCol))
            throw new Exception(labelCol + " not in data column!");
        RFormula formula = new RFormula();
        if (labelCol.equals("")) {
            String formulaString = StringUtils.join(featureCols, " + ");
            formula.setFormula(featureCols[0] + "~" +formulaString)
                    .setFeaturesCol("features")
                    .setLabelCol("label");
        } else {
            String formulaString = StringUtils.join(featureCols, " + ");
            formula.setFormula(labelCol + "~" + formulaString)
                    .setFeaturesCol("features")
                    .setLabelCol("label");
        }
        return formula.fit(dataset).transform(dataset);
    }

  
    //原来的版本。需要feature cols。 但是现在为了简便。默认除了Label col外，其他全是Feature col
    public Dataset<Row> readData(String dataPath, String dataType, String dataFormat, String[] featureCols, String labelCol) throws Exception{
        if (dataType.equals(DataType.HDFS.toString())) {
            return transformData(readFromHDFS(dataPath, dataFormat), featureCols, labelCol);
        } else if (dataType.equals(DataType.LOCAL.toString())){
            return transformData(readFromLocal(dataPath, dataFormat), featureCols, labelCol);
        } else if (dataType.equals(DataType.SQL.toString())) {
            return transformData(readFromSQL(dataPath), featureCols, labelCol);
        } else {
            return null;
        }
    }
    
    public Dataset<Row> readData(String dataPath, String dataType, String dataFormat) throws Exception{
        if (dataType.equals(DataType.HDFS.toString())) {
            return readFromHDFS(dataPath, dataFormat);
        } else if (dataType.equals(DataType.LOCAL.toString())){
            return readFromLocal(dataPath, dataFormat);
        } else if (dataType.equals(DataType.SQL.toString())) {
            return readFromSQL(dataPath);
        } else {
            return null;
        }
    }
    
    
    //更新后的版本
    public Dataset<Row> readData(String dataPath, String dataType, String dataFormat,String labelCol) throws Exception{
    	Dataset<Row> dataset = null;

    	if (dataType.equals(DataType.HDFS.toString())) {
    		dataset = readFromHDFS(dataPath, dataFormat);
        } 
    	else if(dataType.equals(DataType.LOCAL.toString())){
        	dataset = readFromLocal(dataPath, dataFormat);
        } 
    	else if (dataType.equals(DataType.SQL.toString())) {
            dataset =  readFromSQL(dataPath);
        } 
    	if(dataset == null){
    		return null;
    	}
    	//提取出Label column
		List<String> features = new ArrayList<String>();
    	String[] cols = dataset.columns();
    	for(String col :cols){
			if(! col.equals(labelCol)){
				features.add(col);
			}
		}
    	dataset.show();
		Dataset<Row> _dataset = transformData(dataset, features.toArray(new String[0]), labelCol);	
		//一种丑陋的写法，将label强行变为double
	    StringIndexer indexer = new StringIndexer()
	    	      .setInputCol("label")
	    	      .setOutputCol("labelIndexed");
	    _dataset = indexer.fit(_dataset).transform(_dataset);
	    _dataset = _dataset.drop("label");
	    _dataset = _dataset.withColumnRenamed("labelIndexed", "label");
    	return _dataset;
    }

    public String [] getColumns(String dataPath, String dataType, String dataFormat) throws Exception{
        if (dataType.equals(DataType.HDFS.toString())) {
            Dataset<Row> dataset = readFromHDFS(dataPath, dataFormat);
            dataset.show();
            return dataset.columns();
        } else if (dataType.equals(DataType.LOCAL.toString())){
            return readFromLocal(dataPath, dataFormat).columns();
        } else if (dataType.equals(DataType.SQL.toString())) {
            return readFromSQL(dataPath).columns();
        } else {
            return null;
        }
    }
}
