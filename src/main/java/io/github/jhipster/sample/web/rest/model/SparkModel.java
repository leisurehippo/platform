package io.github.jhipster.sample.web.rest.model;

import java.util.Iterator;

import org.apache.spark.ml.Model;
import org.apache.spark.ml.Predictor;
import org.apache.spark.ml.param.Params;
import org.apache.spark.sql.Dataset;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Spark Model Base Class
 * @author zzs
 *
 */
public abstract class SparkModel{
	
	/**
	 * 将参数注如到Spark算法中。继承类无需重写
	 * @param paramPair
	 * @param pre
	 * @throws JSONException
	 */
    public void injectPara(JSONObject paramPair, Params pre) throws JSONException{
    	Iterator iter = paramPair.keys();
        for (int i = 0; i < paramPair.length(); i++) {
            String k = iter.next().toString();
            System.out.print(k+" type is :");
            String value = paramPair.getString(k);
            if (value.equals("true") || value.equals("false")) {
                System.out.println("boolean");
                pre.set(k, paramPair.optBoolean(k));
            }
            //^(-?\d+)(\.\d+)?(e)?(\d+)?$
            else if (value.matches("^(-?\\d+)(\\.\\d+)?$")){
                if (value.contains(".")){
                    System.out.println("double");
                    pre.set(k,paramPair.optDouble(k));
                }else {
                	// 处理特殊情况
                    if(k.equals("seed")){
                        System.out.println("long");                    	
                    	pre.set(k, paramPair.optLong(k));
                    }
                    else{
                        System.out.println("int");
                    	pre.set(k, paramPair.optInt(k));
                    }
                }
            }
            else {//Double的情况有很多种，这里先暴力解析成Double，如果失败，就认为是string类型
        		if(Double.isNaN(paramPair.optDouble(k))){
        			System.out.println("string");
                    pre.set(k, paramPair.optString(k));
        		}
        		else{
                    System.out.println("double");
                    pre.set(k, paramPair.optDouble(k));
        		}
        		
            }
        }
    }
    /**
     * return a trained model
     * @return
     */
    public abstract Model FitModel(JSONObject paramPair, Dataset dataSet, String savePath, String algorithm);
    public abstract String getParam(String algorithm);
}
