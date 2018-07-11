package io.github.jhipster.sample.web.rest.model;

import org.apache.spark.ml.Model;
import org.apache.spark.ml.Predictor;
import org.apache.spark.ml.classification.*;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;

public class SparkClassification extends SparkModel{

	@Override
    public String getParam(String algorithm){
        String result = "";
        switch(algorithm){
            case "lr":
                LogisticRegression lr = new LogisticRegression();
                result = lr.explainParams();
                break;
            case "dt":
                DecisionTreeClassifier dt = new DecisionTreeClassifier();
                result = dt.explainParams();
                break;
            case "rf":
                RandomForestClassifier rf = new RandomForestClassifier();
                result = rf.explainParams();
                break;
            case "gbt":
                GBTClassifier gbt = new GBTClassifier();
                result = gbt.explainParams();
                break;
            case "mutperception":
            	MultilayerPerceptronClassifier mp = new MultilayerPerceptronClassifier();
            	result = mp.explainParams();
            	break;
            case "nb":
                NaiveBayes nb = new NaiveBayes();
                result = nb.explainParams();
            	break;
            default:
                break;
        }
        return result;
    }

    
    public Model lr(JSONObject paramPair, Dataset dataSet, String savePath) throws IOException, JSONException {
        LogisticRegression lr = new LogisticRegression();
        injectPara(paramPair, lr);
        LogisticRegressionModel model_lr= lr.fit(dataSet);
        model_lr.save(savePath);
        return model_lr;
    }


    public Model dt(JSONObject paramPair, Dataset<Row> dataSet, String savePath) throws IOException, JSONException {
        DecisionTreeClassifier dt = new DecisionTreeClassifier();
        injectPara(paramPair, dt);        
        DecisionTreeClassificationModel model_dt = dt.fit(dataSet);
        model_dt.save(savePath);
        return model_dt;
    }

    public Model rf(JSONObject paramPair, Dataset<Row> dataSet, String savePath) throws IOException, JSONException {
        RandomForestClassifier rf = new RandomForestClassifier();
        injectPara(paramPair, rf);        
        RandomForestClassificationModel model_rf = rf.fit(dataSet);
        model_rf.save(savePath);
        return model_rf;
    }

    public Model gbt(JSONObject paramPair, Dataset<Row> dataSet, String savePath) throws IOException, JSONException {
        GBTClassifier gbt = new GBTClassifier();
        injectPara(paramPair, gbt);
        GBTClassificationModel model_gbt = gbt.fit(dataSet);
        model_gbt.save(savePath);
        return model_gbt;
    }

    public Model mutPerception(JSONObject paramPair, Dataset<Row> dataSet, String savePath) throws IOException, JSONException {
        MultilayerPerceptronClassifier mutPer = new MultilayerPerceptronClassifier();
        injectPara(paramPair, mutPer);        
        MultilayerPerceptronClassificationModel model_mutPer = mutPer.fit(dataSet);
        model_mutPer.save(savePath);
        return model_mutPer;
    }

    public Model nb(JSONObject paramPair, Dataset<Row> dataSet, String savePath) throws IOException, JSONException {
        NaiveBayes nb = new NaiveBayes();
        injectPara(paramPair, nb);        
        NaiveBayesModel model_nb = nb.fit(dataSet);
        model_nb.save(savePath);
        return model_nb;
    }

    
	@Override
	public Model FitModel(JSONObject paramPair, Dataset dataSet, String savePath, String algorithm) {
		// TODO Auto-generated method stub
		try{
		   switch(algorithm){
	           case "lr":
	        	   return lr(paramPair, dataSet, savePath);
	           case "dt":
	        	   return dt(paramPair, dataSet, savePath);
	           case "rf":
	        	   return rf(paramPair, dataSet, savePath);
	           case "gbt":
	        	   return gbt(paramPair, dataSet, savePath);
	           case "mutperception":
	        	   return mutPerception(paramPair, dataSet, savePath);
	           case "nb":
	        	   return nb(paramPair, dataSet, savePath);
	           default:
	               break;
		   }
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}


}
