package io.github.jhipster.sample.web.rest.model;

import java.io.IOException;
import org.apache.spark.ml.Model;
import org.apache.spark.ml.regression.*;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *  LR,
    DT,
    RF,
    GBT,
 */
public class SparkRegression extends SparkModel {

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
		           default:
		               break;
			   }
			}catch(Exception e){
				e.printStackTrace();
			}
			return null;	
	}

	@Override
	public String getParam(String algorithm) {
		// TODO Auto-generated method stub
	      String result = "";
	        switch(algorithm){
	            case "lr":
	            	LinearRegression lr = new LinearRegression();
	                result = lr.explainParams();
	                break;
	            case "dt":
	                DecisionTreeRegressor dt = new DecisionTreeRegressor();
	                result = dt.explainParams();
	                break;
	            case "rf":
	                RandomForestRegressor rf = new RandomForestRegressor();
	                result = rf.explainParams();
	                break;
	            case "gbt":
	                GBTRegressor gbt = new GBTRegressor();
	                result = gbt.explainParams();
	                break;
	            default:
	                break;
	        }
	        return result;	
	  }
	
    public Model lr(JSONObject paramPair, Dataset dataSet, String savePath) throws IOException, JSONException {
    	LinearRegression lr = new LinearRegression();
        injectPara(paramPair, lr);
        LinearRegressionModel model_lr= lr.fit(dataSet);
        model_lr.save(savePath);
        return model_lr;
    }
    
    public Model dt(JSONObject paramPair, Dataset<Row> dataSet, String savePath) throws IOException, JSONException {
        DecisionTreeRegressor dt = new DecisionTreeRegressor();
        injectPara(paramPair, dt);        
        DecisionTreeRegressionModel model_dt = dt.fit(dataSet);
        model_dt.save(savePath);
        return model_dt;
    }

    public Model rf(JSONObject paramPair, Dataset<Row> dataSet, String savePath) throws IOException, JSONException {
        RandomForestRegressor rf = new RandomForestRegressor();
        injectPara(paramPair, rf);        
        RandomForestRegressionModel model_rf = rf.fit(dataSet);
        model_rf.save(savePath);
        return model_rf;
    }

    public Model gbt(JSONObject paramPair, Dataset<Row> dataSet, String savePath) throws IOException, JSONException {
        GBTRegressor gbt = new GBTRegressor();
        injectPara(paramPair, gbt);
        GBTRegressionModel model_gbt = gbt.fit(dataSet);
        model_gbt.save(savePath);
        return model_gbt;
    }

}
