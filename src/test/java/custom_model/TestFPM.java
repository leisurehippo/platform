package custom_model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.HashPartitioner;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.junit.Test;
import scala.Tuple2;

import io.github.jhipster.sample.service.util.FileUtil;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.fpm.AssociationRules;
import org.apache.spark.mllib.fpm.FPGrowth;
import org.apache.spark.mllib.fpm.FPGrowthModel;

public class TestFPM implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2022120607398479306L;

	public void testOfficial(){
	   SparkConf conf = new SparkConf()
	  			.setAppName("data-platform")
				.setMaster("local")
				.set("spark.sql.warehouse.dir", "/spark-warehouse/");
		SparkSession spark = SparkSession.builder().config(conf).getOrCreate();
		JavaSparkContext jsc = JavaSparkContext.fromSparkContext(spark.sparkContext());
		String dataPrefix = FileUtil.ProjectPathPrefix + "elec/testHDFS/data/";			
		String path = dataPrefix+"sample_fpgrowth.txt";		
		JavaRDD<String> data = jsc.textFile(path);

		JavaRDD<List<String>> transactions = data.map(
		  new Function<String, List<String>>() {
		    /**
			 * 
			 */
			private static final long serialVersionUID = 1599856848355219039L;

			public List<String> call(String line) {
		      String[] parts = line.split(" ");
		      return Arrays.asList(parts);
		    }
		  }
		);

		FPGrowth fpg = new FPGrowth()
		  .setMinSupport(0.2)
		  .setNumPartitions(10);
		FPGrowthModel<String> model = fpg.run(transactions);

		for (FPGrowth.FreqItemset<String> itemset: model.freqItemsets().toJavaRDD().collect()) {
		  System.out.println("[" + itemset.javaItems() + "], " + itemset.freq());
		}

		double minConfidence = 0.8;
		for (AssociationRules.Rule<String> rule
		  : model.generateAssociationRules(minConfidence).toJavaRDD().collect()) {
		  System.out.println(
		    rule.javaAntecedent() + " => " + rule.javaConsequent() + ", " + rule.confidence());
		}
	}
	
	@Test	
	public void main(){
		   SparkConf conf = new SparkConf()
		  			.setAppName("data-platform")
					.setMaster("local")
					.set("spark.sql.warehouse.dir", "/spark-warehouse/");
			SparkSession spark = SparkSession.builder().config(conf).getOrCreate();
			JavaSparkContext jsc = JavaSparkContext.fromSparkContext(spark.sparkContext());
			String dataPrefix = FileUtil.ProjectPathPrefix + "elec/testHDFS/data/";			
			String path = dataPrefix+"sample_fpgrowth.txt";		
			Dataset<Row> data = spark.read().format("text").load(path);
			data.show(true);
			
			JavaRDD<List<String>> items = data.where(data.col("value").isNotNull()).javaRDD().map(r -> {
				String line = r.getString(0);
				List<String> list = Arrays.asList(line.split(" "));
				return list;
			});
			System.out.println("Step1: Convert data");
			for(List<String>item : items.collect()){
				for(String s : item){
					System.out.println(s);
				}
			}
			
			System.out.println("Step2: Generate frequent items by filtering the input data using minimal"
					+"supportr level");
			int numParts = 2;
		   HashPartitioner partitioner = new HashPartitioner(numParts);
			int minCount = 2;
		    List<Tuple2<String, Integer>> item_maps = items.flatMap(t -> {
				return t.iterator();
			}).mapToPair(v -> new Tuple2<String, Integer>(v, 1))
		      .reduceByKey(partitioner, (k1,k2)-> k1+k2)
		      .filter(s -> s._2>=minCount).collect();
		      ;
		   
		    for(Tuple2<String, Integer> item_map :item_maps){
		    	System.out.println(item_map._1+":"+item_map._2);
		    }
		    List<String> freq_items = jsc.parallelize(item_maps).sortBy(f -> f._2, false, numParts).map(
		    		kv -> kv._1).collect();
		    	
		    HashMap<String,Integer> itemToRank = new HashMap<String, Integer>();
		    for(int i=0;i<freq_items.size();i++){
		    	itemToRank.put(freq_items.get(i), i);
		    }

		    System.out.println("Step3: Test FPTree");
		    
		    
		    System.out.println("Step4: Get Condition Transactions");	    
		    JavaRDD<FreqItemSet> freqItemSets = items.mapToPair(transaction -> getCondTransactions(transaction, itemToRank, partitioner))
		    	.aggregateByKey(new FPTree<Integer>(), partitioner.numPartitions(), 
		    			(tree, transaction) -> tree.add(transaction, 1)
		    			, (tree1, tree2) -> tree1.merge(tree2))
		    	.flatMap( k -> k._2.extract(minCount).iterator())
		    	.map( t -> {
		    		List<String> ft = new ArrayList<String>();
		    		for(Integer rank : t._1){
		    			ft.add(freq_items.get(rank));
		    		}
		    		return new FreqItemSet(ft, t._2);
		    				
		    	});
		    
		    List<FreqItemSet> fsets = freqItemSets.collect();
		    for(FreqItemSet s : fsets){
		    	System.out.println(s);
		    }
		    
			  
	}
	
	public Tuple2<Integer, List<Integer>> getCondTransactions(List<String> transactions, 
			HashMap<String,Integer> itemToRank, HashPartitioner partitioner){
		
		List<Integer> filtered = new ArrayList<Integer>();
		for(String item: transactions){
			if(itemToRank.containsKey(item))
				filtered.add(itemToRank.get(item));
		}
		filtered.sort((a,b)->Integer.compare(a, b));
		int part = partitioner.getPartition(filtered);
		Tuple2<Integer, List<Integer>> output = new Tuple2<Integer, List<Integer>>(part, filtered);
		
		return output;
		
	}
	
	class FreqItemSet implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1042820479317635971L;
		List<String> items;
		int freq;
		public FreqItemSet(List<String >items, int freq) {
			this.items = items;
			this.freq = freq;
		}
		public String toString(){

			String item = String.join(",", items.toArray(new String[]{}));
			return "{" + item + " }," + freq;
		}
	}
}
