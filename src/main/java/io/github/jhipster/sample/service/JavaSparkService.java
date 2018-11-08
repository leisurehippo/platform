package io.github.jhipster.sample.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.spark.ml.Model;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestParam;

import io.github.jhipster.sample.service.util.FileUtil;
import io.github.jhipster.sample.web.rest.FileController;
import io.github.jhipster.sample.web.rest.model.SparkClassification;
import io.github.jhipster.sample.web.rest.model.SparkCluster;
import io.github.jhipster.sample.web.rest.model.SparkEstimate;
import io.github.jhipster.sample.web.rest.model.SparkModel;
import io.github.jhipster.sample.web.rest.model.SparkRegression;
import io.github.jhipster.sample.web.rest.support.Classification;
import io.github.jhipster.sample.web.rest.support.Cluster;
import io.github.jhipster.sample.web.rest.support.Regression;
import io.github.jhipster.sample.web.rest.util.HDFSFileUtil;
import io.github.jhipster.sample.web.rest.util.SparkUtil;

public class JavaSparkService {
    static HDFSFileUtil hdfsFileUtil = new HDFSFileUtil();
    private static FileUtil fileUtil = new FileUtil();
    static SparkUtil sparkUtil = new SparkUtil();
    static SparkCluster sparkCluster = new SparkCluster();
    static FileController fileController = new FileController();
    static SparkClassification sparkClassification = new SparkClassification();
    static SparkRegression sparkRegression = new SparkRegression();
    
    public SparkModel getSparkModel(String type){
    	SparkModel model = null;
    	if(type.equals("classification")){
    		model = sparkClassification;
    	}
    	else if(type.equals("regression")){
    		model = sparkRegression;
    	}
    	else if(type.equals("cluster")){
    		model = sparkCluster;
    	}
    	else{
    		model = sparkCluster;
    	}
    	return model;
    }
    
    public  List<String> getLibraryParameter(String Algorithm, String type){
    	String []arrParam = null;
    	if(type.equals("classification")){
        	arrParam = sparkClassification.getParam(Algorithm).split("\n");
        }
        else if(type.equals("regression")){
        	arrParam = sparkRegression.getParam(Algorithm).split("\n");
        }
        else{
        	arrParam = sparkCluster.getParam(Algorithm).split("\n");
        }
        List<String> resList = new ArrayList<String> ();
        for (int i = 0; i < arrParam.length; i++) {
            String result = "";
            System.out.println(arrParam[i]);
            String []arrDefault = arrParam[i].split(":");
            if (arrDefault.length == 3){
                result = arrDefault[0] + ":" +arrDefault[2].substring(1,arrDefault[2].length()-1);
                resList.add(result);
            }
        }
        return resList;
    }
    public List<String> trainCustom(String ProjectName,String task,String trainDataName,   
			String testDataName,String Parameters, String Algorithm){
		List<String> result = new ArrayList<String>();
		
		
		

		return result;
    }
    
    public List<String> SparkTrain(String ProjectName,String task,String trainDataName, 
    								String trainLabel,String testDataName,String testLabel,
    								String Parameters, String algo_type,
    								String Algorithm)throws Exception{
			List<String> result = new ArrayList<String>();
			result.add("Begin train, Algorithm: "+Algorithm+",trainData:"+trainDataName+",testDataName: "+testDataName);
			
			String pattern = "yyyy_MM_dd_HH_mm_ss";
			SimpleDateFormat format = new SimpleDateFormat(pattern);
			String date = format.format(new Date());
			// get data columns
			String trainHdfsDir = ProjectName+'/'+task+'/'+"/data/"+trainDataName;
			String type = trainDataName.split("\\.")[1];
			if(type.equals("txt")){
				type = "csv";
			}
			Dataset<Row> train = sparkUtil.readData(trainHdfsDir, "HDFS", type,trainLabel);
			
			train.show();
			String testHdfsDir = ProjectName+'/'+task+'/'+"/data/"+testDataName;
			type = testDataName.split("\\.")[1];
			if(type.equals("txt")){
				type = "csv";
			}
			Dataset<Row> test = sparkUtil.readData(testHdfsDir, "HDFS", type,testLabel);

			//add log info
			String train_datainfo = train.showString(5, false);
			result.add("Train Dataset Info:");
			result.add(train_datainfo);
			String test_datainfo = test.showString(5, false);
			result.add("Test Dataset Info");
			result.add(test_datainfo);
			
			JSONObject trainParam = new JSONObject(Parameters);	
			System.out.println(trainParam.toString());
			result.add("Train Parameter Info:");
			result.add(trainParam.toString());	
			
			String modelName = Algorithm + "_" +  trainDataName + "_" + date;
			String modelPath = hdfsFileUtil.root() + ProjectName + '/' + task + "/model/" + modelName;
			System.out.println(modelPath);
			result.add("Model save to:" + modelName);

			SparkModel sparkModel = getSparkModel(algo_type);
			
			SparkEstimate sparkEstimate = new SparkEstimate();
			Model model = sparkModel.FitModel(trainParam, train, modelPath, Algorithm);
			result.add(sparkEstimate.evaluate(algo_type, test, model));
			return result;
		}
    
    
    public List<String> SparkTest(String ProjectName,String task,
    								String testDataName, String testLabel, String Algorithm,String algo_type, 
    								String modelName) throws Exception{   
		String testHdfsDir = ProjectName+'/'+task+'/'+"/data/"+testDataName;
		String type = testDataName.split("\\.")[1];
		if(type.equals("txt")){
			type = "csv";
		}
		Dataset<Row> test = sparkUtil.readData(testHdfsDir, "HDFS", type, testLabel);		
		List<String> result = new ArrayList<String>();
		result.add("Begin test, Algorithm: "+Algorithm+",testDataName: "+testDataName+",ModelName: "+modelName);

		String test_datainfo = test.showString(5, false);
		result.add("Test Dataset Info:");
		result.add(test_datainfo);
		
		SparkEstimate sparkEstimate = new SparkEstimate();
		String modelPath = hdfsFileUtil.root() + ProjectName + '/' + task + "/model/" + modelName;
		Model model = null;
		if(algo_type.equals("classification")){
			model = sparkEstimate.loadModel(modelPath, Classification.valueOf(Algorithm.toUpperCase()));
		}else if(algo_type.equals("regression")){
			model = sparkEstimate.loadModel(modelPath, Regression.valueOf(Algorithm.toUpperCase()));
		}else{
			model = sparkEstimate.loadModel(modelPath, Cluster.valueOf(Algorithm.toUpperCase()));
		}
		Dataset<Row> rows = sparkEstimate.predict(test, model);
		rows.show();
		Dataset<Row> result_dataset = rows.select("prediction");
		result_dataset.show();
		result.add("Prediction:");
		String prediction_info = rows.showString(5, false);
		result.add(prediction_info);
		
		StringBuffer file_res = new StringBuffer();
		for (Row r: rows.collectAsList()) {
		   file_res.append(r.get(0) + "\n");
		}
		
		String result_name = modelName+"_"+testDataName;
		result_name = result_name.replace('.', '_') + ".csv";
		
		result.add("result save to:" + result_name);
		
		String result_path = ProjectName+'/'+task+'/'+"/result/"+result_name;		
		result_dataset.write().mode("Overwrite").option("delimiter", " ").option("inferSchema",true).option("header", true).format("csv").save(hdfsFileUtil.HDFSPath(result_path));
		
		/*
		// save predictions to local file. (not hdfs)
		fileUtil.saveToFile(fileUtil.ProjectPathPrefix+ProjectName+"/"+task+"/"+"result/" + result_name, 
								file_res.toString());
		*/
		return result;
	}    		
    

}
