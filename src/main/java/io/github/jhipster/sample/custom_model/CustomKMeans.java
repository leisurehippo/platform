package io.github.jhipster.sample.custom_model;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.ml.linalg.Vectors;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.util.DoubleAccumulator;
import org.apache.spark.util.random.XORShiftRandom;
import org.apache.spark.ml.linalg.BLAS;
import scala.Tuple2;

public class CustomKMeans implements Serializable{
	
	private static final long serialVersionUID = -2978702825345541251L;

	public ArrayList<VectorWithNorm> centers = null;

	private int K=2;
	private int initSteps=0;
	private int maxIter=10;
	private long seed=1L;
	private double epsilon = 1e-4;
	
	public int getK() {
		return K;
	}

	public void setK(int k) {
		K = k;
	}

	public int getInitSteps() {
		return initSteps;
	}

	public void setInitSteps(int initSteps) {
		this.initSteps = initSteps;
	}

	public int getMaxIter() {
		return maxIter;
	}

	public void setMaxIter(int maxIter) {
		this.maxIter = maxIter;
	}

	
	public CustomKMeans() {
		setMaxIter(20);
		setK(2);
		setInitSteps(5);
	}
	
	/**
	 * Inner class for fast distance computation
	 * @author zzs
	 *
	 */
	class VectorWithNorm implements Serializable{

		private static final long serialVersionUID = -534963683305112888L;
		
		public Vector vector;
		public Double norm;
		
		public VectorWithNorm(Vector vector, Double norm) {
			this.vector = vector;
			this.norm = norm;
		}
		
		public VectorWithNorm(Vector vector){
			this.vector = vector;
			this.norm = Vectors.norm(vector, 2);
		}
		
		public VectorWithNorm(double[] array){
			this(Vectors.dense(array));
		}
		
		
	}

	 // use default sqdist function now.	
	  private double fastSquaredDistance(VectorWithNorm v1,VectorWithNorm v2){
		  return Vectors.sqdist(v1.vector,v2.vector);
	  }	
	
	  
    public Tuple2<Integer, Double> findClosest(List<VectorWithNorm> centers, VectorWithNorm point){
		  double bestDistance = Double.MAX_VALUE;
		  int bestIndex = 0;
		  int i = 0;
		  for(VectorWithNorm center : centers){
			  double lowerBoundOfSqDist = center.norm - point.norm;
			  lowerBoundOfSqDist = lowerBoundOfSqDist * lowerBoundOfSqDist;
			  if(lowerBoundOfSqDist < bestDistance){
				  double distance = fastSquaredDistance(center, point);
				  if(distance < bestDistance){
					  bestDistance = distance;
					  bestIndex = i;
				  }
			  }
			  i += 1;
		  }
		  return new Tuple2<Integer, Double>(bestIndex, bestDistance);
    }
	public Tuple2<Vector, Integer> merge(Tuple2<Vector, Integer> a, Tuple2<Vector, Integer> b){
		BLAS.axpy(1.0, a._1, b._1);
		return new Tuple2<Vector, Integer>(b._1, a._2+b._2);
	}
    
	public boolean isCenterConverged(VectorWithNorm oldCenter, VectorWithNorm newCenter, double epsilon){
		double cost = fastSquaredDistance(newCenter, oldCenter);
		System.out.println("cost:"+cost +"epsilon" + epsilon*epsilon);
		return cost <= epsilon * epsilon;
	}
	
	
	public double run(JavaRDD<Vector> data){		  
		  JavaRDD<Double> norms = data.map(vector -> Vectors.norm(vector, 2));
		  norms.cache();
		  JavaRDD<VectorWithNorm> zippedData = data.zip(norms).map(
				  z -> new VectorWithNorm(z._1, z._2));
		  double cost = runAlgorithm(zippedData);
		  norms.unpersist();
		  return cost;
	}
	
	public double run(Dataset<Row> data){
		JavaRDD<Vector> rdd_data = data.toJavaRDD().map(row -> (Vector)row.get(0));
		return run(rdd_data);
	}
	
	/**
	 * Java Implementation of K-Means algorithm base on Spark scala implementation
	 * @param data
	 * @return
	 */
	public double runAlgorithm(JavaRDD<VectorWithNorm> data){
		  SparkContext sc = data.context();
		  JavaSparkContext jsc = JavaSparkContext.fromSparkContext(sc);
		  ArrayList<VectorWithNorm> centers = new ArrayList<VectorWithNorm>(
				  data.takeSample(false, K,  new XORShiftRandom(this.seed).nextInt())
				  );
		  	  
		  double final_cost = 0.0;
		  boolean converged = false;
		  int iteration = 0;
		  
		  while(iteration < maxIter && !converged){
			  DoubleAccumulator costAccum = sc.doubleAccumulator();
			  Broadcast<List<VectorWithNorm>> bcCenters = jsc.broadcast(centers);
			  
			  Map<Integer, Tuple2<Vector, Integer>> totalContribs = data.mapPartitionsToPair(points -> {
				  List<VectorWithNorm> activeCenters = bcCenters.value();				  
				  int dims = activeCenters.get(0).vector.size();
				  Vector[] sums = new Vector[K]; //这里不能对象来填充，否则会出现同一引用问题
				  for(int i=0;i<K;i++){
					  sums[i] = Vectors.zeros(dims);
				  }
				  int[] counts = new int[K];
				  Arrays.fill(counts, 0);			
				  points.forEachRemaining(c_point->{
					  Tuple2<Integer, Double> temp = findClosest(activeCenters, c_point);
					  int bestCenter = temp._1;
					  double cost = temp._2;
					  counts[bestCenter] += 1;
					  Vector sum = sums[bestCenter];
					  BLAS.axpy(1.0, c_point.vector, sum);
					  costAccum.add(cost);
				  });
				  List<Tuple2<Integer, Tuple2<Vector, Integer>>> result = new ArrayList<Tuple2<Integer, Tuple2<Vector, Integer>>>();
				  for(int i=0; i<K;i++){
					  System.out.println();
					  Tuple2<Vector, Integer> value = new Tuple2<Vector, Integer>(sums[i], counts[i]);
					  Tuple2<Integer, Tuple2<Vector, Integer>> k_v = new Tuple2<Integer, Tuple2<Vector, Integer>>(i, value);
					  result.add(k_v);
				  }
				  return result.iterator();
			  }).reduceByKey((a,b )-> merge(a,b)).collectAsMap();
			  					  
			  bcCenters.unpersist(false);
			  
			  // Update the cluster centers and costs
			  converged = true;
			  for(int i=0; i<K; i++){
				  Tuple2<Vector, Integer> value = totalContribs.get(i);
				  System.out.println("key:"+i+"vector:"+value._1 + "count:" + value._2);
				  if(value._2 == 0){
					  continue;
				  }
				  BLAS.scal(1.0 / value._2, value._1);
				  VectorWithNorm newCenter = new VectorWithNorm(value._1);
				  if (converged && !isCenterConverged(centers.get(i), newCenter, epsilon)){
					  converged = false;
				  }				  
				  centers.set(i, newCenter);
			  }

			  final_cost = costAccum.value();
			  
			  iteration += 1;
			  System.out.println("iteration "+iteration + "cost" + final_cost);
			  System.out.println("\n print centers");
			  for (VectorWithNorm center : centers){
			      System.out.println(center.vector);
			  }			  
		  }
		  this.centers = centers;
		  return final_cost;
	  		
	}
	
	public JavaRDD<Integer> transform(JavaRDD<Vector> data){
		 JavaRDD<Integer> preds = data.map(vector -> predict(vector));
		 return preds;
	}
	
	public Integer predict(Vector vector){
		Tuple2<Integer, Double> pred = findClosest(this.centers, new VectorWithNorm(vector));
		return pred._1;
	}
	
	public void printCenters(){
		if(this.centers == null){
			return;
		}
		System.out.println("\nprint centers");
		for (VectorWithNorm center : centers){
			System.out.println(center.vector);
		}
		System.out.println("done \n");
	}
	
	//这里简单对类直接序列化和反序列化。后期可能模仿Spark的方式存取模型数据
	
	/**
	 * 序列化类
	 * @param path
	 */
	@SuppressWarnings("resource")
	public void save(String path){
		try {
			FileOutputStream file = new FileOutputStream(path);
			ObjectOutputStream out = new ObjectOutputStream(file);
			out.writeObject(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 反序列化类
	 * @param path
	 * @return
	 */
	@SuppressWarnings("resource")
	public static CustomKMeans load(String path){
		 try {
			FileInputStream file = new FileInputStream(path);
			ObjectInputStream in = new ObjectInputStream(file);
			CustomKMeans model = (CustomKMeans)in.readObject();
			return model;
		} catch (Exception e) {
			e.printStackTrace();
		}
		 return null;
	}


}
