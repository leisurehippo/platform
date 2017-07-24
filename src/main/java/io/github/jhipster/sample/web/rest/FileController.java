package io.github.jhipster.sample.web.rest;

/**
 * Created by WJ on 2017/4/18.
 */
import com.codahale.metrics.annotation.Timed;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import io.github.jhipster.sample.web.rest.util.HDFSFileUtil;
import io.github.jhipster.sample.web.rest.util.SparkUtil;
import net.sf.json.JSONObject;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import java.io.*;


import io.github.jhipster.sample.web.rest.util.FileUploadingUtil;

/**
 * 文件上传控制器
 *
 */
@RestController
@RequestMapping("/api")

public class FileController {

    private static String ProjectPathPrefix = "src/main/webappfiles/Project/";
    private static String HDFSPathPrefix = "/user/hadoop/data_platform/data/";
    private static String ProjectDescribeFile = "Describe&DataFormatLimit.txt";
    private static FileUploadingUtil fileUtil = new FileUploadingUtil();
    private static HDFSFileUtil hdfsFileUtil = new HDFSFileUtil();


    /**
     * 文件上传具体实现方法;
     * @param file
     * @return
     */
    @PostMapping("/uploadAlgorithm")
    @ResponseBody
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam(value = "ProjectName") String ProjectName,
                                   @RequestParam(value = "ParameterDescribe") String ParameterDescribe){
        JSONObject object = new JSONObject();
        if (object.isEmpty()) {
            object = new JSONObject();
        }
        boolean flagUpload,flagDescri;
        try{
            flagUpload = fileUtil.uploadFile(file,ProjectPathPrefix+ProjectName+"/Algorithm/algorithm/");
            flagDescri = fileUtil.createFile(ProjectPathPrefix+ProjectName+"/Algorithm/ParameterDescribe/",file.getOriginalFilename().split("\\.")[0]+"ParameterDescribe.txt",ParameterDescribe);
        }catch (Exception e){
            object.put("result", "fail");
            return object.toString();
        }
        object.put("result", (flagUpload && flagDescri)?"success":"fail");
        return object.toString();
    }
    @PostMapping("/uploadData")
    @ResponseBody
    public String handleDataUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam(value = "ProjectName") String ProjectName){
        JSONObject object = new JSONObject();
        if (object.isEmpty()) {
           object = new JSONObject();
        }
        //check data format
        String []filename = file.getOriginalFilename().split("\\.");
        String format = filename[filename.length-1];
        boolean flag = false;
        try{
            File DataFormatFile = new File(ProjectPathPrefix + ProjectName + "/" + ProjectDescribeFile);
            InputStreamReader read = new InputStreamReader(new FileInputStream(DataFormatFile),"utf-8");
            BufferedReader bufferedReader = new BufferedReader(read);
            String describe = bufferedReader.readLine();
            String []arrDataFormat = bufferedReader.readLine().split("\\+");
            for (int i = 0; i < arrDataFormat.length; i++) {
                if (arrDataFormat[i].equals(format))
                    flag = true;
            }
            read.close();
        }catch (Exception e){
            object.put("result", "fail");
            return object.toString();
        }
        if (flag){
            boolean flagUpload;
            try{
                flagUpload = fileUtil.uploadFile(file,ProjectPathPrefix + ProjectName+"/Data/");
            }catch (Exception e){
                object.put("result", "fail");
                return object.toString();
            }
            object.put("result", flagUpload?"success":"fail");
        }else {
            object.put("result", "format error");
        }
        return object.toString();
    }


    /**
     * 获取服务器端项目列表
     * @return
     */
    @GetMapping("/getServerProjectList")
    @ResponseBody
    public List<String> getServerProjectList(){
        return fileUtil.listDir(ProjectPathPrefix,true);
    }

    /**
     * 获取服务器端项目描述
     * @return
     */
    @GetMapping("/getServerProjectDes")
    @ResponseBody
    public String getServerProjectDes(){
        JSONObject object = new JSONObject();
        if (object.isEmpty()) {
            object = new JSONObject();
        }
        List<String> ProjectList = getServerProjectList();

        for (int i = 0; i < ProjectList.size(); i++) {
            try{
                String projectName = ProjectList.get(i);
                File DataFormatFile = new File(ProjectPathPrefix+projectName+"/"+ProjectDescribeFile);
                InputStreamReader read = new InputStreamReader(new FileInputStream(DataFormatFile),"utf-8");
                BufferedReader bufferedReader = new BufferedReader(read);
                String describe = bufferedReader.readLine();
                object.put(projectName, describe);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        String result = object.toString();
        return result;
    }

    /**
     * 新增项目
     * @param ProjectName
     * @return
     */
    @GetMapping("/createProject")
    @ResponseBody
    public String createProject(@RequestParam(value = "ProjectName") String ProjectName,
                                @RequestParam(value = "ProjectDescribe") String ProjectDescribe,
                                @RequestParam(value = "DataFormatLimit") String DataFormatLimit){
        JSONObject object = new JSONObject();
        if (object.isEmpty()) {
            object = new JSONObject();
        }
        String destDirName = ProjectPathPrefix + ProjectName;
        if (!destDirName.endsWith(File.separator)) {// 结尾是否以"/"结束
            destDirName = destDirName + File.separator;
        }
        File dir = new File(destDirName);
        if (dir.exists()){
            object.put("result", "exist");
            return object.toString();
        }

        boolean flagServer,flagHdfs;
        if (dir.mkdirs()) {
            flagServer = fileUtil.createFile(destDirName,ProjectDescribeFile,ProjectDescribe+"\n"+DataFormatLimit);
            //create HDFS
            try{
                flagHdfs = hdfsFileUtil.mkdir(HDFSPathPrefix + ProjectName + "/");
                if (flagHdfs) {
                    String localDir = ProjectPathPrefix + ProjectName + "/" + ProjectDescribeFile;
                    String hdfsDir = HDFSPathPrefix + ProjectName + "/" + ProjectDescribeFile;
                    hdfsFileUtil.upload(localDir, hdfsDir, false);
                    if (!hdfsFileUtil.checkFile(hdfsDir))
                        flagHdfs = false;
                }
            }catch (Exception e){
                object.put("result", "hdfs fail");
                return object.toString();
            }
            object.put("result",(flagServer&&flagHdfs) ? "success" : "fail");
        }
        else {
            object.put("result", "fail");
        }

        return object.toString();
    }

    /**
     * 编辑项目
     * @param ProjectName
     * @return
     */
    @GetMapping("/editProject")
    @ResponseBody
    public String editProject(@RequestParam(value = "ProjectName") String ProjectName,
                              @RequestParam(value = "ProjectDescribe") String ProjectDescribe,
                              @RequestParam(value = "DataFormatLimit") String DataFormatLimit) {
        JSONObject object = new JSONObject();
        if (object.isEmpty()) {
            object = new JSONObject();
        }
        String destDirName = ProjectPathPrefix + ProjectName;
        if (!destDirName.endsWith(File.separator)) // 结尾是否以"/"结束
            destDirName = destDirName + File.separator;
        File dir = new File(destDirName);
        boolean flagServerDelete,flagServerCreate,flagHdfsDelete,flagHdfsCreate = true;
        if (dir.exists()){
            flagServerDelete = fileUtil.deleteFile(destDirName + ProjectDescribeFile);
            flagServerCreate = fileUtil.createFile(destDirName,ProjectDescribeFile,ProjectDescribe+"\n"+DataFormatLimit);
            //delete HDFS
            try{
                flagHdfsDelete = hdfsFileUtil.delFile(HDFSPathPrefix + ProjectName + ProjectDescribeFile, false);
                String localDir = ProjectPathPrefix + ProjectName + "/" + ProjectDescribeFile;
                String hdfsDir = HDFSPathPrefix + ProjectName + "/" + ProjectDescribeFile;
                hdfsFileUtil.upload(localDir, hdfsDir, false);
                if (!hdfsFileUtil.checkFile(hdfsDir))
                    flagHdfsCreate = false;
            }catch (Exception e){
                object.put("result", "fail");
                return object.toString();
            }
            object.put("result",(flagServerDelete&&flagServerCreate&&flagHdfsDelete&&flagHdfsCreate) ? "success" : "fail");
        }else{
            object.put("result", "not exist");
        }
        return object.toString();
    }

    /**
     * 删除项目
     * @param ProjectName
     * @return
     */
    @GetMapping("/deleteProject")
    @ResponseBody
    public String deleteProject(@RequestParam(value = "ProjectName") String ProjectName){
        JSONObject object = new JSONObject();
        if (object.isEmpty()) {
            object = new JSONObject();
        }
        String destDirName = ProjectPathPrefix + ProjectName;
        File file = new File(destDirName);
        if (!file.exists()) {// 判断目录或文件是否存在
            object.put("result", "not exist");
            return object.toString();
        } else {
            boolean flagServer,flagHdfs;
            flagServer = fileUtil.deleteDirectory(destDirName);
            //delete HDFS
            try{
                flagHdfs = hdfsFileUtil.delFile(HDFSPathPrefix + ProjectName, true);
            }catch (Exception e){
                object.put("result", "fail");
                return object.toString();
            }
            object.put("result",(flagServer&&flagHdfs) ? "success" : "fail");
            return object.toString();
        }
    }

    /**
     * 获取HDFS数据列表
     * @return 数据文件名列表
     * @throws Exception
     */
    @GetMapping("/getHdfsData")
    @ResponseBody
    public List<String> getHdfsData(@RequestParam(value = "ProjectName") String ProjectName) throws Exception{
        return hdfsFileUtil.list(HDFSPathPrefix + ProjectName + "/");
    }

    /**
     * 获取所有数据文件
     * @return 数据文件名列表
     * @throws Exception
     */
    @GetMapping("/getServerData")
    @ResponseBody
    public List<String> getAllData(@RequestParam(value = "ProjectName") String ProjectName) throws Exception{
        List<String> hdfs = getHdfsData(ProjectName);
        List<String> local = fileUtil.listDir(ProjectPathPrefix + ProjectName + "/Data/", false);
        List<String> result = new ArrayList<String>();
        for (int i = 0; i < local.size(); i++) {
            if (hdfs.contains(local.get(i)))
                result.add(local.get(i)+"+1");
            else
                result.add(local.get(i)+"+0");
        }
        return result;
    }

    /**
     * 获取本地算法
     * @return String []dataList
     */
    @GetMapping("/getServerAlgorithm")
    @ResponseBody
    public List<String> getServerAlgorithm(@RequestParam(value = "ProjectName") String ProjectName){
        return fileUtil.listDir(ProjectPathPrefix + ProjectName + "/Algorithm/algorithm/", false);
    }


    /**
     * 上传本地数据至HDFS
     * @param  arrDataName 本地src\main\webappfiles\Data目录下的数据文件名列表
     * @return 上传失败的文件名
     * @throws Exception
     */
    @GetMapping("/HdfsUpload")
    @ResponseBody
    public List<String> HdfsUpload(@RequestParam(value = "ProjectName") String ProjectName,
                                   @RequestParam(value = "DataName") String []arrDataName) throws Exception{
        List<String> result = new ArrayList<>();
        for (int i = 0; i < arrDataName.length; i++) {
            String DataName = arrDataName[i];
            String localDir = ProjectPathPrefix + ProjectName + "/Data/" + DataName;
            String hdfsDir = HDFSPathPrefix + ProjectName + "/" + DataName;
            hdfsFileUtil.upload(localDir, hdfsDir, false);
            if (!hdfsFileUtil.checkFile(hdfsDir))
                result.add(DataName);
        }
        return result;
    }


    @GetMapping("/getSize")
    @ResponseBody
    public String getSize(@RequestParam(value = "DataName") String DataName) throws Exception{
        String path = HDFSPathPrefix + DataName;
        return hdfsFileUtil.getSizeK(path)+"KB";
    }
}

