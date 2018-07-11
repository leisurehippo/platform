package io.github.jhipster.sample.web.rest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.spark.ml.classification.LogisticRegression;
import org.apache.spark.ml.classification.LogisticRegressionModel;
import org.apache.spark.ml.feature.RFormula;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.spark.ml.feature.Binarizer;

import org.junit.Test;
import org.apache.spark.sql.Row;

import io.github.jhipster.sample.web.rest.support.Classification;
import io.github.jhipster.sample.web.rest.util.HDFSFileUtil;
import io.github.jhipster.sample.web.rest.util.SparkUtil;
public class SparkTest {


	public void testOpt() throws JSONException{
		String a = "1.0Es-6";
		JSONObject paramPair = new JSONObject();
		paramPair.put("a", a);
		System.out.println(Double.isNaN(paramPair.optDouble("a")));
	}
	@Test

	public void test(){
		String dataName = "a.tx,t";
		for(String s : dataName.split("\\.")){
			System.out.println(s);
		}

	}

	public void testLoadData() throws Exception{
		HDFSFileUtil hdfsUtil = new HDFSFileUtil();
		String dataPrefix = hdfsUtil.root() + "elec/testHDFS/data/"; 
		SparkUtil sparkUtil = new SparkUtil();
		SparkSession spark = sparkUtil.getSession();
		Dataset<Row> df = spark.read().format("json").load(dataPrefix + "data.json");
		df.show(false);
		df.printSchema();
		String [] cols = df.columns();	
		List<String> features = new ArrayList<String>();
		String label = "label";
		for(String col :cols){
			if(! col.equals(label)){
				features.add(col);
			}
		}
		String para = "{\"tol\":\"1.0E-6\",\"elasticNetParam\":\"0.0\",\"fitIntercept\":\"true\",\"rawPredictionCol\":\"rawPrediction\",\"featuresCol\":\"features\",\"maxIter\":\"100\",\"standardization\":\"true\",\"regParam\":\"0.0\",\"threshold\":\"0.5\",\"labelCol\":\"label\",\"predictionCol\":\"prediction\"}";
		
		LogisticRegression lr = new LogisticRegression();
		JSONObject paramPair = new JSONObject(para);	

        Iterator iter = paramPair.keys();
        for (int i = 0; i < paramPair.length(); i++) {
        
            String k = iter.next().toString();
             
            System.out.print(k+" type is :");
            String value = paramPair.getString(k);
            if (value.equals("true") || value.equals("false")) {
                System.out.println("boolean");
                lr.set(k, paramPair.optBoolean(k));
            }
            //^(-?\d+)(\.\d+)?(e)?(\d+)?$
            else if (value.matches("^(-?\\d+)(\\.\\d+)?$")){
                if (value.contains(".")){
                    System.out.println("double");
                    lr.set(k,paramPair.optDouble(k));
                }else {
                    System.out.println("int");
                    lr.set(k, paramPair.optInt(k));
                }
            }
            else {
        		if(Double.isNaN(paramPair.optDouble(k))){
        			System.out.println("string");
                    lr.set(k, paramPair.optString(k));
        		}
        		else{
                    System.out.println("double");
                    lr.set(k, paramPair.optDouble(k));
        		}
        		
            }
        }
		Dataset<Row> train = sparkUtil.transformData(df, features.toArray(new String[0]), "label");
		train.select("features").show(false);
		train.show();
		
		/*
		LogisticRegression lr = new LogisticRegression()
				  .setMaxIter(10)
				  .setRegParam(0.3)
				  .setElasticNetParam(0.8);
		*/
		LogisticRegressionModel lrModel = lr.fit(train);
		System.out.println("Coefficients: "
				  + lrModel.coefficients() + " Intercept: " + lrModel.intercept());
		
		// Displays the content of the DataFrame to stdout
	}
}
