package io.github.jhipster.sample.web.rest;
/**
 * Created by WJ on 2017/5/29.
 */

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;

import org.springframework.web.bind.annotation.*;

import java.io.FileWriter;
import java.io.Serializable;


//import org.apache.spark.examples.JavaSparkPi;


@RestController
@RequestMapping("/api")
public class SparkKMeans implements Serializable {
    public static String DATADIR = "src/main/webappfiles/Data/";
    @PostMapping("/kmeans")
    @ResponseBody
    public String kmeans(@RequestParam(value = "FileName")String fileName,
                         @RequestParam(value = "numClusters")int numClusters) {
        SparkConf conf = new SparkConf().setAppName("JavaKMeansExample").setMaster("local");
        JavaSparkContext jsc = new JavaSparkContext(conf);

        // Load and parse data
        String path = DATADIR + fileName + ".txt";
        JavaRDD<String> data = jsc.textFile(path);
        JavaRDD<Vector> parsedData = data.map(
            new Function<String, Vector>() {
                public Vector call(String s) {
                    String[] sarray = s.split(" ");
                    double[] values = new double[sarray.length];
                    for (int i = 0; i < sarray.length; i++) {
                        values[i] = Double.parseDouble(sarray[i]);
                    }
                    return Vectors.dense(values);
                }
            }
        );
        parsedData.cache();

        // Cluster the data into two classes using KMeans
//        int numClusters = 2;
        int numIterations = 20;
        KMeansModel clusters = KMeans.train(parsedData.rdd(), numClusters, numIterations);

//        System.out.println("Cluster centers:");
//        for (Vector center: clusters.clusterCenters()) {
//            System.out.println(" " + center);
//        }
//        double cost = clusters.computeCost(parsedData.rdd());
//        System.out.println("Cost: " + cost);
//
//        // Evaluate clustering by computing Within Set Sum of Squared Errors
//        double WSSSE = clusters.computeCost(parsedData.rdd());
//        System.out.println("Within Set Sum of Squared Errors = " + WSSSE);

        try{
            FileWriter fw = new FileWriter(DATADIR+"output/"+fileName+".txt",true);
            for (Vector center: clusters.clusterCenters()) {
                fw.write(center + "\n");
            }
            fw.close();
        }catch (Exception e){
            e.printStackTrace();
            return "file write fail";
        }
        // Save and load model
//        clusters.save(jsc.sc(), DATADIR+"target/org/apache/spark/JavaKMeansExample/KMeansModel");
//        KMeansModel sameModel = KMeansModel.load(jsc.sc(),
//            DATADIR + "target/org/apache/spark/JavaKMeansExample/KMeansModel");

        jsc.stop();
        return "success";
    }


}
