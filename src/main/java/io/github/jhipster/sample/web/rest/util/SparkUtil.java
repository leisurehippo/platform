package io.github.jhipster.sample.web.rest.util;


import org.apache.ivy.util.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.ml.feature.RFormula;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import io.github.jhipster.sample.web.rest.support.DataType;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Properties;

public class SparkUtil {

    private static SparkSession spark;
    private static HDFSFileUtil hdfsFileUtil;

    static {
        try{
            FactoryUtil factoryUtil = FactoryUtil.getFactory();
            Properties properties = new Properties();
            properties.load(SparkUtil.class.getResourceAsStream("/cluster.properties"));
            String master = properties.getProperty("spark_master_ip");
            String port = properties.getProperty("spark_port");
            SparkConf conf = new SparkConf().setAppName("data-platform").setMaster("spark://" + master + ":" + port).set("spark.sql.warehouse.dir", "/spark-warehouse/");
            spark = SparkSession.builder().config(conf).getOrCreate();
            hdfsFileUtil = factoryUtil.getHDFSUtil();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Dataset<Row> readFromHDFS(String path, String dataFormat) throws Exception {
        if(!hdfsFileUtil.checkFile(path)) {
            System.out.println("not");
            throw new FileNotFoundException(path + "not found on hadoop!");
        }
        System.out.println("fff");
        return spark.read().format(dataFormat).load(hdfsFileUtil.HDFSPath(path));
    }

    private Dataset<Row> readFromLocal(String path, String dataFormat) throws Exception{
        if (!new File(path).exists())
            throw new FileNotFoundException(path + " not found on local!");
        return spark.read().format(dataFormat).load(path);
    }

    private Dataset<Row> readFromSQL(String sql) {
        return spark.sql(sql);
    }

    private Dataset<Row> transformData(Dataset<Row> dataset, String[] featureCols, String labelCol) throws Exception{
        String [] columns = dataset.columns();
        List<String> columnList = java.util.Arrays.asList(columns);
        for(String featureCol : featureCols)
            if (!columnList.contains(featureCol))
                throw new Exception(featureCol + " not in data column!");
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

    public Dataset<Row> readData(String dataPath, String dataType, String dataFormat, String[] featureCols, String labelCol) throws Exception{
        if (dataType.equals(DataType.HDFS.toString())) {
            System.out.println("eee");
            return transformData(readFromHDFS(dataPath, dataFormat), featureCols, labelCol);
        } else if (dataType.equals(DataType.LOCAL.toString())){
            return transformData(readFromLocal(dataPath, dataFormat), featureCols, labelCol);
        } else if (dataType.equals(DataType.SQL.toString())) {
            return transformData(readFromSQL(dataPath), featureCols, labelCol);
        } else {
            return null;
        }
    }
}
