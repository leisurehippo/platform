package custom_model;

import java.io.IOException;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.junit.Test;

import io.github.jhipster.sample.custom_model.CustomKMeans;
import io.github.jhipster.sample.web.rest.util.FactoryUtil;
import io.github.jhipster.sample.web.rest.util.HDFSFileUtil;
import io.github.jhipster.sample.web.rest.util.SparkUtil;

public class TestCustomModel {
	
	@Test
    public void testKmeans(){
		/*
	    SparkConf conf = new SparkConf()
	  			.setAppName("data-platform")
				.setMaster("local")
				.set("spark.sql.warehouse.dir", "/spark-warehouse/");
		SparkSession spark = SparkSession.builder().config(conf).getOrCreate();
		JavaSparkContext jsc = JavaSparkContext.fromSparkContext(spark.sparkContext());
		*/
		
		SparkUtil sparkUtil = new SparkUtil();
		SparkSession spark = sparkUtil.getSession();
		JavaSparkContext jsc = JavaSparkContext.fromSparkContext(spark.sparkContext());
		HDFSFileUtil hdfsFileUtil = FactoryUtil.getFactory().getHDFSUtil();
		CustomKMeans model = new CustomKMeans();
		
        String jarPath = hdfsFileUtil.master() + "/user/hadoop/runJars/jhipster-sample-application-0.0.1-SNAPSHOT.jar";
        try {
			boolean flag = hdfsFileUtil.checkFile(jarPath);
			System.out.println(flag);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Part0: Load and parse data
		String dataPrefix = hdfsFileUtil.root() + "elec/testHDFS/data/"; 
		
		String path = dataPrefix+"kmeans_data.txt";
		Dataset<Row> df = spark.read().option("delimiter", " ").option("inferSchema", "true").format("csv").load(path);
		
		//Dataset<Row> features = df.map((MapFunction<Row, Integer>) row-> row.getString(0).split(" "),  
		//	  Encoders.);
		System.out.println("Part0: Load Data");
		df.show();
		
		
		//Part1 : transform rawData to JavaRDD<Vector>
		System.out.println("Part1: transform rawData to JavaRDD<Vector>");
		
		VectorAssembler assembler = new VectorAssembler()
			  .setInputCols(df.columns())
			  .setOutputCol("features");
		
		Dataset<Row> data = assembler.transform(df).select("features");
		data.show();
		
		JavaRDD<Vector> rdd_data = data.toJavaRDD().map(row -> (Vector)row.get(0));
		double cost = model.run(rdd_data);
		System.out.println("final cost" + cost);
		model.printCenters();
		
		System.out.println("\nPredict");
		JavaRDD<Integer> result = model.transform(rdd_data);
		for(int label : result.collect()){
			System.out.println(label);
		}
    }
	
}
