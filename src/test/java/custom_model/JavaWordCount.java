package custom_model;

import scala.Tuple2;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.SparkSession;

import io.github.jhipster.sample.web.rest.util.FactoryUtil;
import io.github.jhipster.sample.web.rest.util.HDFSFileUtil;
import io.github.jhipster.sample.web.rest.util.SparkUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

public final class JavaWordCount {
  private static final Pattern SPACE = Pattern.compile(" ");

  public static void main(String[] args) throws Exception {


	  SparkConf conf = new SparkConf()
			  				.setAppName("WordCount")
			  				.setMaster("local")
							//.setMaster("spark://" + master + ":" + port)
							.set("spark.sql.warehouse.dir", "/spark-warehouse/");
	  //conf.set("spark.debug.maxToStringFields", "100");
	  SparkSession spark = SparkSession.builder().config(conf).getOrCreate();
	  
		HDFSFileUtil hdfsUtil = new HDFSFileUtil();
		
		String dataPrefix = hdfsUtil.root() + "elec/testHDFS/data/"; 
		
		JavaRDD<String> lines = spark.read().textFile(dataPrefix+"word_count.txt").javaRDD();
		
		JavaRDD<String> words = lines.flatMap(s -> Arrays.asList(SPACE.split(s)).iterator());
		
		JavaPairRDD<String, Integer> ones = words.mapToPair(s -> new Tuple2<>(s, 1));
		
		JavaPairRDD<String, Integer> counts = ones.reduceByKey((i1, i2) -> i1 + i2);
		
		List<Tuple2<String, Integer>> output = counts.collect();
		for (Tuple2<?,?> tuple : output) {
		  System.out.println(tuple._1() + ": " + tuple._2());
		}
		//spark.stop();
  }
}