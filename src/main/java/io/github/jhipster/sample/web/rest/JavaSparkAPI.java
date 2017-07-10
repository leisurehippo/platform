package io.github.jhipster.sample.web.rest;

import io.github.jhipster.sample.web.rest.model.SparkEstimate;
import io.github.jhipster.sample.web.rest.support.Classification;
import org.apache.spark.ml.Model;
import org.springframework.web.bind.annotation.*;

import io.github.jhipster.sample.web.rest.model.SparkClassification;
import io.github.jhipster.sample.web.rest.model.SparkCluster;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.json.JSONObject;
import io.github.jhipster.sample.web.rest.util.HDFSFileUtil;
import io.github.jhipster.sample.web.rest.util.SparkUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by WJ on 2017/4/25.
 */
@RestController
@RequestMapping("/api")
public class JavaSparkAPI {

    static HDFSFileUtil hdfsFileUtil = new HDFSFileUtil();
    static SparkUtil sparkUtil = new SparkUtil();
    static SparkCluster sparkCluster = new SparkCluster();
    static SparkClassification sparkClassification = new SparkClassification();


    /**
     * 上传本地数据至HDFS
     * @param DataName 本地src\main\webappfiles\Data目录下的数据文件名
     * @return true or false表示是否上传成功
     * @throws Exception
     */
    @PostMapping("/HdfsUpload")
    @ResponseBody
    public boolean HdfsUpload(@RequestParam(value = "DataName") String DataName) throws Exception{
        String localDir = "src\\main\\webappfiles\\Data\\" + DataName + ".json";
        String hdfsDir = "/user/hadoop/data_platform/data/" + DataName + ".json";
        hdfsFileUtil.upload(localDir, hdfsDir, false);
        return hdfsFileUtil.checkFile(hdfsDir);
    }

    /**
     * 获取HDFS数据列表
     * @return 数据文件名列表
     * @throws Exception
     */
    @PostMapping("/getHdfsData")
    @ResponseBody
    public String[] getHdfsData() throws Exception{
        String [] tmp = {};
        return hdfsFileUtil.list("/user/hadoop/data_platform/data/").toArray(tmp);
    }

    /**
     * 获取所有数据文件
     * @return 数据文件名列表
     * @throws Exception
     */
    @PostMapping("/getAllData")
    @ResponseBody
    public Map<String,Integer> getAllData() throws Exception{
        String [] hdfs = getHdfsData();
        FileController fileController = new FileController();
        String [] local = fileController.getLocalData("Data");
        Map<String,Integer> fileLocation = new HashMap<String,Integer>();
        for (int i = 0; i < hdfs.length; i++) {
            fileLocation.put(hdfs[i],1);
        }
        for (int i = 0; i < local.length; i++) {
            if (!fileLocation.containsKey(local[i]))
                fileLocation.put(local[i], 0);
        }
        return fileLocation;
    }

    @PostMapping("/getModel")
    @ResponseBody
    public String[] getModel() throws Exception{
        String [] tmp = {};
        return hdfsFileUtil.list("/user/hadoop/data_platform/model/").toArray(tmp);
    }

    /**
     * 获取数据列名
     * @param DataName HDFS上数据文件名
     * @return String []columns 数据列名
     * @throws Exception
     */
    @PostMapping("/getDataColumns")
    @ResponseBody
    public String [] getDataColumns(@RequestParam(value = "DataName") String DataName) throws Exception{
        String hdfsDir = "/user/hadoop/data_platform/data/" + DataName + ".json";
        String [] columns = sparkUtil.getColumns(hdfsDir, "HDFS", "json");
        return columns;
    }

    /**
     * train
     * @param DataName
     * @param featureCols
     * @param ModelName
     * @param Parameters
     * @param Algorithm
     * @throws Exception
     */
    @PostMapping("/SparkTrain")
    @ResponseBody
    public void SparkTrain(@RequestParam(value = "DataName") String DataName,
//                              @RequestParam(value = "Columns") String [] featureCols,
                              @RequestParam(value = "ModelName") String ModelName,
                              @RequestParam(value = "Parameters") String Parameters,
                              @RequestParam(value = "Algorithm") String Algorithm) throws Exception{
        Date date = new Date();
        String[] featureCols = {"wigth", "age", "heigth", "interets"};
        String hdfsDir = "/user/hadoop/data_platform/data/" + DataName + ".json";
        Dataset<Row> dataset = sparkUtil.readData(hdfsDir, "HDFS", "json",featureCols, "label");
        System.out.println(dataset.count());
        for(String column : dataset.columns())
            System.out.println(column);
//        {'maxIter':10, 'regParam' : 0.5, 'elasticNetParam' : 0.8, 'standardization' : true}
        JSONObject jsonObject = new JSONObject(Parameters);

        String modelPath = hdfsFileUtil.HDFSPath("/user/hadoop/data_platform/model/" + ModelName + String.valueOf(date.getTime()));
        switch (Algorithm){
            case "lr":
                sparkClassification.lr(jsonObject, dataset, modelPath);
                break;
            default:
                break;
        }

    }

    @PostMapping("/SparkPredict")
    @ResponseBody
    public void SparkPredict(@RequestParam(value = "DataName") String DataName,
//                             @RequestParam(value = "Columns") String [] featureCols,
                             @RequestParam(value = "ModelName") String ModelName,
                             @RequestParam(value = "Algorithm") String Algorithm) throws Exception{

        String[] featureCols = {"wigth", "age", "heigth", "interets"};
        String hdfsDir = "/user/hadoop/data_platform/data/" + DataName + ".json";
        System.out.println(hdfsDir);
        Dataset<Row> dataset = sparkUtil.readData(hdfsDir, "HDFS", "json",featureCols, "label");
        System.out.println("read over");
        String modelPath = hdfsFileUtil.HDFSPath("/user/hadoop/data_platform/model/" + ModelName);
        System.out.println(modelPath);
        SparkEstimate sparkEstimate = new SparkEstimate();
        switch (Algorithm){
            case "lr":
                Model model = sparkEstimate.loadModel(modelPath, Classification.LR);
                sparkEstimate.predict(dataset,model);
                break;
            default:
                break;
        }

    }




    public static void testCluster() throws Exception{
        SparkCluster sparkCluster = new SparkCluster();
        String[] featureCols = {"wigth", "age", "heigth", "interets"};
        Dataset<Row> dataset = sparkUtil.readData("/user/hadoop/data_platform/data.json", "HDFS", "json",
            featureCols, "label");
        System.out.println(dataset.count());
        for(String column : dataset.columns())
            System.out.println(column);
        JSONObject jsonObject = new JSONObject("{'K':10, 'seed' : 10, 'initSteps' : 30, 'tol' : 0.5}");
        Vector[] vectors = sparkCluster.kmeans(jsonObject, dataset);
        for(Vector vector : vectors) {
            System.out.println(vector);
        }
    }
}
