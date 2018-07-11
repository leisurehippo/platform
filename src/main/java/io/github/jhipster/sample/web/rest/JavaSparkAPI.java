package io.github.jhipster.sample.web.rest;

import io.github.jhipster.sample.web.rest.model.SparkEstimate;
import io.github.jhipster.sample.web.rest.model.SparkRegression;
import io.github.jhipster.sample.web.rest.support.Classification;
import io.github.jhipster.sample.web.rest.support.Cluster;
import io.github.jhipster.sample.web.rest.support.Regression;

import org.apache.spark.ml.Model;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.github.jhipster.sample.service.JavaSparkService;
import io.github.jhipster.sample.web.rest.model.SparkClassification;
import io.github.jhipster.sample.web.rest.model.SparkCluster;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.json.JSONObject;
import io.github.jhipster.sample.web.rest.util.HDFSFileUtil;
import io.github.jhipster.sample.web.rest.util.SparkUtil;

import java.io.File;
import java.util.*;

/**
 * Created by WJ on 2017/4/25.
 */
@RestController
@RequestMapping("/api")
public class JavaSparkAPI {
    private static String HDFSPathPrefix = "/user/hadoop/data_platform/data/";

    static HDFSFileUtil hdfsFileUtil = new HDFSFileUtil();
    static SparkUtil sparkUtil = new SparkUtil();
    static SparkCluster sparkCluster = new SparkCluster();
    static FileController fileController = new FileController();
    static SparkClassification sparkClassification = new SparkClassification();
    private static JavaSparkService service = new JavaSparkService();




    @GetMapping("/getModel")
    @ResponseBody
    public List<String> getModel() throws Exception{
        return hdfsFileUtil.list("/user/hadoop/data_platform/model/");
    }

    /**
     * 获取数据列名
     * @param DataName HDFS上数据文件名
     * @return String []columns 数据列名
     * @throws Exception
     */
    @GetMapping("/getDataColumns")
    @ResponseBody
    public String [] getDataColumns(@RequestParam(value = "ProjectName") String ProjectName,
                                    @RequestParam(value = "DataName") String DataName) throws Exception{
        String hdfsDir = HDFSPathPrefix + ProjectName + "/Data/" + DataName;
        String [] columns = sparkUtil.getColumns(hdfsDir, "HDFS", "json");
        return columns;
    }

    @GetMapping("/getLibraryParameter")
    @ResponseBody
    public  List<String> getLibraryParameter(@RequestParam(value = "Algorithm") String Algorithm, @RequestParam(value = "type")String type){
    	return service.getLibraryParameter(Algorithm, type);
    }
    
    
    /**
     * 返回已经支持的Spark算法
     * @return
     */
    @GetMapping("/getSparkAlgorithms")
    @ResponseBody
    public List<String> getSparkAlgorithms(@RequestParam(value = "type") String type){
    	List<String> algos = new ArrayList<String>();
    	if(type.equals("classification")){
	    	for(Classification cls : Classification.values()){
	    		algos.add(cls.toString().toLowerCase());
			}
    	}
    	else if(type.equals("regression")){
	    	for(Regression reg : Regression.values()){
	    		algos.add(reg.toString().toLowerCase());
			}
    	}
    	else if(type.equals("cluster")){
    		for(Cluster clu : Cluster.values()){
	    		algos.add(clu.toString().toLowerCase());
			}
    	}
    	System.out.println(type);
    	return algos;
    }

    
    /**
     * 训练模型
     * @param project: 当前项目
     * @param task: 当前任务
     * @param algo: 所使用的算法
     * @param para: 算法对应的参数
     * @param trainData: 训练数据文件 HDFS地址
     * @param testData: 测试数据文件 HDFS地址
     * @return
     */
    @GetMapping("/train")
    @ResponseBody
    public List<String> train(@RequestParam(value = "project") String project,
    						  @RequestParam(value = "task") String task,
    						  @RequestParam(value = "algoType") String algo_type,    						  
    						  @RequestParam(value = "algo") String algo,
    						  @RequestParam(value = "para") String para,
    						  @RequestParam(value = "trainData") String trainData,
    						  @RequestParam(value = "testData") String testData
    						){
    	List<String> res = new ArrayList<String>();
    	try {
			res = service.SparkTrain(project, task, trainData, testData, para, algo_type, algo);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return res;
    }
    
    @GetMapping("/test")
    @ResponseBody
    public List<String> test(@RequestParam(value = "project") String project,
							  @RequestParam(value = "task") String task,
    						  @RequestParam(value = "algoType") String algo_type,    						  							  
							  @RequestParam(value = "algo") String algo,
							  @RequestParam(value = "testData") String testData,
							  @RequestParam(value = "model") String modelName){
    	List<String> res = new ArrayList<String>();
    	try {
			res = service.SparkTest(project, task, testData, algo, algo_type, modelName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return res;

    }
    
}
