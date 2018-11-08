package io.github.jhipster.sample.web.rest.model;

import static org.apache.spark.sql.functions.callUDF;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.spark.ml.feature.Binarizer;
import org.apache.spark.ml.feature.MinMaxScaler;
import org.apache.spark.ml.feature.MinMaxScalerModel;
import org.apache.spark.ml.feature.Normalizer;
import org.apache.spark.ml.feature.OneHotEncoder;
import org.apache.spark.ml.feature.StandardScaler;
import org.apache.spark.ml.feature.StandardScalerModel;
import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.api.java.UDF1;
import org.junit.Test;

import io.github.jhipster.sample.service.util.FileUtil;
import io.github.jhipster.sample.web.rest.util.FactoryUtil;
import io.github.jhipster.sample.web.rest.util.HDFSFileUtil;
import io.github.jhipster.sample.web.rest.util.SparkUtil;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.ml.feature.MaxAbsScaler;
import org.apache.spark.ml.feature.MaxAbsScalerModel;
 
public class SparkTransform implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
	private static UDF1 frist = new UDF1<Vector, Double>() {
		  public Double call(final Vector types) throws Exception {
		    return types.toArray()[0];
		  }
		};
		

	static{
		SparkUtil sparkUtil = FactoryUtil.getFactory().getSparkUtil();
		SparkSession spark = sparkUtil.getSession();
		spark.udf().register("frist", frist, DataTypes.DoubleType);

	}
	
	public String[] support_transforms = new String[]{
			"StandardScaler", "MinMaxScaler", "Normalizer", "MaxAbsScaler",
			"StringIndexer", "Binarizer", "drop", "dropDuplicates",
			"fillNaWithMean", "dropNa"
	};
	
	/**
	 * 转换操作。包含字符串列、向量列、数值列
	 * @param data
	 * @param method
	 * @param feature
	 * @return
	 */
	public Dataset<Row> transform(Dataset<Row> data, String method, String feature){
		String date = dateFormat.format(new Date());
		String temp_feature = feature + date;
		switch(method){
			case "StandardScaler":
				data = standardScaler(data, feature, temp_feature);
				data = data.drop(feature);
				data = data.withColumnRenamed(temp_feature, feature);
				break;
			case "MinMaxScaler":
				data = minMaxScaler(data, feature, temp_feature);
				data = data.drop(feature);
				data = data.withColumnRenamed(temp_feature, feature);
				break;
			case "Normalizer":
				data = normalizer(data, feature, temp_feature);
				data = data.drop(feature);
				data = data.withColumnRenamed(temp_feature, feature);				
				break;
			case "MaxAbsScaler":
				data = maxAbsScaler(data, feature, temp_feature);
				data = data.drop(feature);
				data = data.withColumnRenamed(temp_feature, feature);
				break;
			case "StringIndexer":
				data = stringIndexer(data, feature, temp_feature);
				data = data.drop(feature);
				data = data.withColumnRenamed(temp_feature, feature);				
				break;
			case "Binarizer":
				data = binarizer(data, feature, temp_feature);
				data = data.drop(feature);
				data = data.withColumnRenamed(temp_feature, feature);				
				break;
			case "fillNaWithMean":
				data = fillNaWithMean(data, feature);
				break;
			case "dropNa":
				data = dropNa(data, feature);
				break;
			case "drop":
				data = data.drop(feature);
				break;
			case "dropDuplicates":
				data = data.dropDuplicates(feature);
				break;																										
				
			default:
				break;
		}
		return data;
	}
	
	/**
	 * Multi columns transform
	 * @param data
	 * @param method
	 * @param columns
	 * @param out_feature
	 * @return
	 */
	public Dataset<Row> transform(Dataset<Row> data, String method, List<String> columns, String out_feature){
		String date = dateFormat.format(new Date());
		data = vectorAssemble(data, columns, out_feature);

		return data;
	}
	
	public Dataset<Row> extractVector(Dataset<Row>data, String feature){
		String date = dateFormat.format(new Date());
		String temp_feature = feature + date + "extract";	

		data = data.withColumn(temp_feature, callUDF("frist", data.col(feature)));
		data = data.drop(feature);
		data = data.withColumnRenamed(temp_feature, feature);
		return data;
	}
	
	public Dataset<Row> fillNaWithMean(Dataset<Row>data, String feature){
		Dataset<Row> stat = data.describe(feature).select(feature);
		List<Double> infos = stat.toJavaRDD().map(row -> Double.parseDouble((String) row.get(0))).collect();
		double mean = infos.get(1);
		data = data.na().fill(mean);		
		return data;
	}
	
	public Dataset<Row> dropNa(Dataset<Row> data, String feature){
		data = data.na().drop(new String[]{feature});
		return data;
	}
	
	//多列操作
	
	public Dataset<Row> vectorAssemble(Dataset<Row>data, List<String> columns, String out_feature){
		  VectorAssembler assembler = new VectorAssembler()
				  .setInputCols(columns.toArray(new String[]{}))
				  .setOutputCol(out_feature);

		  data = assembler.transform(data);
		  return data;		
	}
	
	//向量列操作
	
	public Dataset<Row> standardScaler(Dataset<Row>data, String feature, String out_feature){
		List<String> columns = new ArrayList<String>();
		columns.add(feature);
		
		String date = dateFormat.format(new Date());
		String temp_feature = feature + date + "vector";		
		data = vectorAssemble(data, columns, temp_feature);
		
		StandardScaler scaler = new StandardScaler()
				  .setInputCol(temp_feature)
				  .setOutputCol(out_feature)
				  .setWithStd(true)
				  .setWithMean(false);

		// Compute summary statistics by fitting the StandardScaler
		StandardScalerModel scalerModel = scaler.fit(data);

		// Normalize each feature to have unit standard deviation.
		Dataset<Row> scaledData = scalerModel.transform(data);
		scaledData = scaledData.drop(temp_feature);

		scaledData = extractVector(scaledData, out_feature);
		return scaledData;
	}
	
	
	public Dataset<Row> minMaxScaler(Dataset<Row>data, String feature, String out_feature){
		// to vector
		List<String> columns = new ArrayList<String>();
		columns.add(feature);
		String date = dateFormat.format(new Date());
		String temp_feature = feature + date + "vector";		
		data = vectorAssemble(data, columns, temp_feature);
		MinMaxScaler  scaler = new MinMaxScaler ()
				  .setInputCol(temp_feature)
				  .setOutputCol(out_feature);

		// Compute summary statistics by fitting the StandardScaler
		MinMaxScalerModel scalerModel = scaler.fit(data);

		// Normalize each feature to have unit standard deviation.
		Dataset<Row> scaledData = scalerModel.transform(data);

		scaledData = scaledData.drop(temp_feature);

		// to single
		scaledData = extractVector(scaledData, out_feature);	
		return scaledData;
	}
	
	public Dataset<Row> maxAbsScaler(Dataset<Row>data, String feature, String out_feature){
		// to vector
		List<String> columns = new ArrayList<String>();
		columns.add(feature);
		String date = dateFormat.format(new Date());
		String temp_feature = feature + date + "vector";		
		data = vectorAssemble(data, columns, temp_feature);
		MaxAbsScaler  scaler = new MaxAbsScaler()
				  .setInputCol(temp_feature)
				  .setOutputCol(out_feature);

		// Compute summary statistics by fitting the StandardScaler
		MaxAbsScalerModel scalerModel = scaler.fit(data);

		// Normalize each feature to have unit standard deviation.
		Dataset<Row> scaledData = scalerModel.transform(data);

		scaledData = scaledData.drop(temp_feature);

		// to single
		scaledData = extractVector(scaledData, out_feature);	
		return scaledData;
	}	
	
	public Dataset<Row> normalizer(Dataset<Row>data, String feature, String out_feature){
		// to vector
		List<String> columns = new ArrayList<String>();
		columns.add(feature);
		String date = dateFormat.format(new Date());
		String temp_feature = feature + date + "vector";		
		data = vectorAssemble(data, columns, temp_feature);
		Normalizer normalizer = new Normalizer()
				  .setInputCol(temp_feature)
				  .setOutputCol(out_feature);
		// Normalize each feature to have unit standard deviation.
		Dataset<Row> scaledData = normalizer.transform(data);

		scaledData = scaledData.drop(temp_feature);

		// to single
		scaledData = extractVector(scaledData, out_feature);	
		return scaledData;
	}		
	
	// 字符列
	
	public Dataset<Row> stringIndexer(Dataset<Row>data, String feature, String out_feature){
		StringIndexer indexer = new StringIndexer()
				  .setInputCol(feature)
				  .setOutputCol(out_feature);
		data = indexer.fit(data).transform(data);
		return data;
	}
	
	public Dataset<Row> oneHotEncoder(Dataset<Row>data, String feature, String out_feature){
		data = stringIndexer(data, feature, out_feature+"index");
		OneHotEncoder encoder = new OneHotEncoder()
				  .setInputCol(out_feature+"index")
				  .setOutputCol(out_feature);
		data = encoder.transform(data);
		return data;		
	}
	
	
	// 数值列
	public Dataset<Row> binarizer(Dataset<Row> data, String feature, String out_feature){
		Binarizer binarizer = new Binarizer()
				  .setInputCol(feature)
				  .setOutputCol(out_feature)
				  .setThreshold(0.5);
		data = binarizer.transform(data);
		return data;
	}
	
	@Test
	public void test(){
		String project = "elec";
		String task = "testHDFS";
		String dataType = "data";
		String txt_fileName = "kmeans_data.txt";
		String json_file = "data1.json";
	    FactoryUtil factory = FactoryUtil.getFactory();
	    HDFSFileUtil hdfsUtil = factory.getHDFSUtil();
	   SparkUtil sparkUtil = factory.getSparkUtil();
	    
		SparkSession spark = sparkUtil.getSession();
		String format = txt_fileName.substring(txt_fileName.lastIndexOf(".")+1, txt_fileName.length());
		String prefix = FileUtil.ProjectPathPrefix + "elec/testHDFS/data/"; 
		String path = prefix + json_file;

				
		Dataset<Row> data = null;
		format = "json";
		if(format.equals("txt") || format.equals("csv")){
			data = spark.read().option("delimiter", " ").option("inferSchema",true).format("csv").load(path);
		}
		else if(format.equals("json")){
			data = spark.read().format("json").load(path);
		}
		data.show();
		data.printSchema();
		data = transform(data, "fillNaWithMean", "heigth");
		
		data = transform(data, "MaxAbsScaler", "heigth");
		data.show();
		data = transform(data, "Normalizer", "wigth");
		data.show();		




	}
	
}
