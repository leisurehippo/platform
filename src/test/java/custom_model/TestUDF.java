package custom_model;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.api.java.UDF1;
import org.apache.spark.sql.types.DataTypes;
import org.junit.Test;

import static org.apache.spark.sql.functions.*;

import io.github.jhipster.sample.service.util.FileUtil;

public class TestUDF {
	private static UDF1 toUpper = new UDF1<String, String>() {
	    public String call(final String str) throws Exception {
	        return str.toUpperCase();
	    }
	};
	
	private static UDF1 mode = new UDF1<Vector, Double>() {
		  public Double call(final Vector types) throws Exception {
		    return types.toArray()[0];
		  }
		};
	@Test
	public void main(){
		SparkConf conf = new SparkConf()
	  			.setAppName("data-platform")
				.setMaster("local")
				.set("spark.sql.warehouse.dir", "/spark-warehouse/");
		SparkSession spark = SparkSession.builder().config(conf).getOrCreate();
		JavaSparkContext jsc = JavaSparkContext.fromSparkContext(spark.sparkContext());
		spark.udf().register("stringLengthTest", (String str) -> str.length(), DataTypes.IntegerType);
		
		spark.udf().register("toUpper", toUpper, DataTypes.StringType);

		
		String dataPrefix = FileUtil.ProjectPathPrefix + "elec/task1/data/"; 
		/*
		String path = dataPrefix+"word_count.txt";		
		Dataset<Row> df = spark.read().format("text").load(path);
		df.show();
		df.withColumn("Upper",callUDF("toUpper", df.col("value"))).show();
		df.withColumn("length",callUDF("stringLengthTest", df.col("value"))).show();
		*/
		String path = dataPrefix+"kmeans_data.txt";				
		Dataset<Row> df = spark.read().option("delimiter", " ").option("inferSchema", "true").format("csv").load(path);
		df.show();
		VectorAssembler assembler = new VectorAssembler()
				  .setInputCols(df.columns())
				  .setOutputCol("features");

		Dataset<Row> data = assembler.transform(df).select("features");
		data.show();	
		spark.udf().register("mode", mode, DataTypes.DoubleType);
		
		data.withColumn("first", callUDF("mode", data.col("features"))).show();
	}
}
