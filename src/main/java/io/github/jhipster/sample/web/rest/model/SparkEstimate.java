package io.github.jhipster.sample.web.rest.model;

import org.apache.spark.ml.Model;
import org.apache.spark.ml.classification.*;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.evaluation.RegressionEvaluator;
import org.apache.spark.ml.regression.DecisionTreeRegressionModel;
import org.apache.spark.ml.regression.GBTRegressionModel;
import org.apache.spark.ml.regression.LinearRegressionModel;
import org.apache.spark.ml.regression.RandomForestRegressionModel;
import org.apache.spark.ml.clustering.*;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import io.github.jhipster.sample.web.rest.support.Classification;
import io.github.jhipster.sample.web.rest.support.Cluster;
import io.github.jhipster.sample.web.rest.support.Regression;


public class SparkEstimate {

    public Model loadModel(String path, Classification classification) {
        Model model = null;
        if (classification == Classification.LR) model = LogisticRegressionModel.load(path);
        else if (classification == Classification.DT) model = DecisionTreeClassificationModel.load(path);
        else if (classification == Classification.NB) model = NaiveBayesModel.load(path);
        else if (classification == Classification.GBT) model = GBTClassificationModel.load(path);
        else if (classification == Classification.RF) model = RandomForestClassificationModel.load(path);
        else if (classification == Classification.MUTPERCEPTION) model = MultilayerPerceptronClassificationModel.load(path);
        return model;
    }
    
    public Model loadModel(String path, Regression regression) {
        Model model = null;
        if (regression == Regression.LR) model = LinearRegressionModel.load(path);
        else if (regression == Regression.DT) model = DecisionTreeRegressionModel.load(path);
        else if (regression == Regression.GBT) model = GBTRegressionModel.load(path);
        else if (regression == Regression.RF) model = RandomForestRegressionModel.load(path);
        return model;
    }
    
    public Model loadModel(String path, Cluster cluster) {
        Model model = null;
        if (cluster == Cluster.B_KMEANS) model = BisectingKMeansModel.load(path);
        else if (cluster == Cluster.KMEANS) model = KMeansModel.load(path);
        else if (cluster == Cluster.GMM) model = GaussianMixtureModel.load(path);
        return model;
    }
    

    public Dataset<Row> predict(Dataset<Row> dataset, Model model) {
        Dataset<Row> results = model.transform(dataset);
        Dataset<Row> rows = results.select("prediction");
        return rows;
    }
    
    public String evaluate(String type, Dataset<Row> dataset, Model model){
    	if(type.equals("classification")){
    		return evaluate_classification(dataset, model);
    	}
    	else if(type.equals("regression")){
    		return evaluate_regression(dataset, model);
    	}else if(type.equals("cluster")){
    		return evaluate_cluster(dataset, model);
    	}
    	return "Unknow type";
    }
    
    public String evaluate_classification(Dataset<Row> dataset, Model model){
        Dataset<Row> predictions = model.transform(dataset);
    	MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
    			  .setLabelCol("label")
    			  .setPredictionCol("prediction")
    			  .setMetricName("accuracy");
		double accuracy = evaluator.evaluate(predictions);
		return "Test Error = " + (1.0 - accuracy);
    }
    
    public String evaluate_regression(Dataset<Row> dataset, Model model){
    	// Make predictions
        Dataset<Row> prediction = model.transform(dataset);
        prediction.show();
    	RegressionEvaluator evaluator = new RegressionEvaluator()
    			  .setLabelCol("label")
    			  .setPredictionCol("prediction")
    			  .setMetricName("rmse");
		double rmse = evaluator.evaluate(prediction);
		return "Root Mean Squared Error (RMSE) on test data = " + rmse;    
	}
    
    //2.0缺了很多特性
    public String evaluate_cluster(Dataset<Row> dataset, Model model){
    	
    	return "Cluster no cost";
    }

}
