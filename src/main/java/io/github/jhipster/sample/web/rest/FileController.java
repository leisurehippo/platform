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
import io.github.jhipster.sample.web.rest.util.Files_Utils_DG;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.compress.utils.IOUtils;
import org.omg.CORBA.Object;
import org.springframework.data.repository.query.Param;
import org.springframework.http.*;
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
    private static Files_Utils_DG files_utils_dg = new Files_Utils_DG();


    // ----------------------------------------------UPLOAD FILE--------------------------------------------------------
    /**
     * 文件上传具体实现方法;
     * @param file
     * @return
     */
    @PostMapping("/uploadAlgorithm")
    @ResponseBody
    public String handleAlgorithmUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam(value = "ProjectName") String ProjectName){
        JSONObject object = new JSONObject();
        if (object.isEmpty())
            object = new JSONObject();

        boolean flagUpload;
        try{
            // upload algorithm file
            flagUpload = fileUtil.uploadFile(file,ProjectPathPrefix+ProjectName+"/Algorithm/algorithm/");
        }catch (Exception e){
            object.put("result", "fail");
            return object.toString();
        }
        object.put("result", flagUpload ? "success" : "fail");
        return object.toString();
    }
    /**
     * 文件上传具体实现方法;
     * @param ProjectName
     * @param AlgorithmName
     * @param ParameterDescribe
     * @return
     */
    @PostMapping("/uploadParameterDescribe")
    @ResponseBody
    public String handleParameterDescribeUpload(@RequestParam(value = "ProjectName") String ProjectName,
                                                @RequestParam(value = "AlgorithmName") String AlgorithmName,
                                                @RequestParam(value = "ParameterDescribe") String ParameterDescribe){
        JSONObject object = new JSONObject();
        if (object.isEmpty())
            object = new JSONObject();
        boolean flagDescri;
        try{
            String describe = "";
            // Analysis json object
            JSONArray paramArray = JSONArray.fromObject(ParameterDescribe);
            for (int i=0; i < paramArray.size(); i++) {
                JSONObject paramJson = paramArray.getJSONObject(i);
                String name = paramJson.getString("parameterName");
                String des = paramJson.getString("parameterDescribe");
                String isData = paramJson.getString("isData");
                // splice each item
                describe += name + " " + des + " " + isData + "\n";
            }
            System.out.println(describe);
            // get algorithm name
            String [] arrtemp = AlgorithmName.split("\\.");
            // write ParameterDescribe into txt
            flagDescri = fileUtil.createFile(ProjectPathPrefix+ProjectName+"/Algorithm/ParameterDescribe/",arrtemp[0]+"ParameterDescribe.txt",describe);
        }catch (Exception e){
            object.put("result", "fail");
            return object.toString();
        }
        object.put("result", flagDescri?"success":"fail");
        return object.toString();
    }
    @PostMapping("/uploadData")
    @ResponseBody
    public String handleDataUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam(value = "ProjectName") String ProjectName){
        JSONObject object = new JSONObject();
        if (object.isEmpty())
           object = new JSONObject();

        //---check data format
        String []filename = file.getOriginalFilename().split("\\.");
        String format = filename[filename.length-1];
        boolean flag = false;
        try{
            File DataFormatFile = new File(ProjectPathPrefix + ProjectName + "/" + ProjectDescribeFile);
            InputStreamReader read = new InputStreamReader(new FileInputStream(DataFormatFile),"utf-8");
            BufferedReader bufferedReader = new BufferedReader(read);
            String describe = bufferedReader.readLine();
            //get the DataFormat
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
        //--------------------
        if (flag){
            // upload data file
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
        //upload file one by one
        for (int i = 0; i < arrDataName.length; i++) {
            String DataName = arrDataName[i];
            String localDir = ProjectPathPrefix + ProjectName + "/Data/" + DataName;
            String hdfsDir = HDFSPathPrefix + ProjectName + "/Data/" + DataName;
            hdfsFileUtil.upload(localDir, hdfsDir, false);
            // if upload fail, add it into output
            if (!hdfsFileUtil.checkFile(hdfsDir))
                result.add(DataName);
        }
        return result;
    }
    // ----------------------------------------------UPLOAD FILE--------------------------------------------------------


    // ----------------------------------------------PROJECT MANAGEMENT-------------------------------------------------
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
        if (object.isEmpty())
            object = new JSONObject();
        // get the project list
        List<String> ProjectList = getServerProjectList();

        for (int i = 0; i < ProjectList.size(); i++) {
            try{
                String projectName = ProjectList.get(i);
                File DataFormatFile = new File(ProjectPathPrefix + projectName + "/" + ProjectDescribeFile);
                InputStreamReader read = new InputStreamReader(new FileInputStream(DataFormatFile),"utf-8");
                BufferedReader bufferedReader = new BufferedReader(read);
                // read the describe from txt
                String describe = bufferedReader.readLine();
                // generate json object
                object.put(projectName, describe);
                read.close();
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
        if (object.isEmpty())
            object = new JSONObject();

        String destDirName = ProjectPathPrefix + ProjectName;
        // whether end with "/"
        if (!destDirName.endsWith(File.separator))
            destDirName = destDirName + File.separator;

        File dir = new File(destDirName);
        // if the project exist, return
        if (dir.exists()){
            object.put("result", "exist");
            return object.toString();
        }
        boolean flagServer,flagHdfs=true;
        //if the project is not exist, make the dir
        if (dir.mkdirs()) {
            //create the project`s describe file on server
            flagServer = fileUtil.createFile(destDirName,ProjectDescribeFile,ProjectDescribe+"\n"+DataFormatLimit);
            //create the project`s describe file on HDFS
            try{
                String path = HDFSPathPrefix + ProjectName + "/";
                // make the dir, both "Project" and "Project/Data"
                String []allPath = {path,path+"Data/"};
                for (int i = 0; i < allPath.length; i++) {
                    if (!hdfsFileUtil.mkdir(allPath[i]))
                        flagHdfs = false;
                }
                if (flagHdfs) {
                    // upload the project`s describe file from server to HDFS
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
        if (object.isEmpty())
            object = new JSONObject();

        String destDirName = ProjectPathPrefix + ProjectName;
        // whether end with "/"
        if (!destDirName.endsWith(File.separator))
            destDirName = destDirName + File.separator;

        File dir = new File(destDirName);
        boolean flagServerDelete,flagServerCreate,flagHdfsDelete,flagHdfsCreate = true;
        // if the project is exist
        if (dir.exists()){
            // delete the ProjectDescribeFile, and re-create it on server
            flagServerDelete = fileUtil.deleteFile(destDirName + ProjectDescribeFile);
            flagServerCreate = fileUtil.createFile(destDirName,ProjectDescribeFile,ProjectDescribe+"\n"+DataFormatLimit);
            // delete the ProjectDescribeFile, and re-create it on HDFS
            try{
                flagHdfsDelete = hdfsFileUtil.delFile(HDFSPathPrefix + ProjectName + "/" + ProjectDescribeFile, false);
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
        if (object.isEmpty())
            object = new JSONObject();

        String destDirName = ProjectPathPrefix + ProjectName;
        File file = new File(destDirName);
        // whether the project is exist
        if (!file.exists()) {
            object.put("result", "not exist");
            return object.toString();
        } else {
            boolean flagServer,flagHdfs;
            //delete project on server
            flagServer = fileUtil.deleteDirectory(destDirName);
            //delete project on HDFS
            try{
                flagHdfs = hdfsFileUtil.delFile(HDFSPathPrefix + ProjectName + "/", true);
            }catch (Exception e){
                object.put("result", "fail");
                return object.toString();
            }
            object.put("result",(flagServer&&flagHdfs) ? "success" : "fail");
            return object.toString();
        }
    }
    // ----------------------------------------------PROJECT MANAGEMENT-------------------------------------------------


    //----------------------------------------------GET DATA OR ALGORITHM-----------------------------------------------
    /**
     * 获取HDFS数据列表
     * @return 数据文件名列表
     * @throws Exception
     */
    @GetMapping("/getHdfsData")
    @ResponseBody
    public List<String> getHdfsData(@RequestParam(value = "ProjectName") String ProjectName) throws Exception{
        return hdfsFileUtil.list(HDFSPathPrefix + ProjectName + "/Data/");
    }

    /**
     * 获取所有数据文件
     * @return 数据文件名列表(hdfs已有,后缀拼接 "+1",否则拼接 "+0")
     * @throws Exception
     */
    @GetMapping("/getServerData")
    @ResponseBody
    public List<String> getAllData(@RequestParam(value = "ProjectName") String ProjectName) throws Exception{
        //get HDFS data
        List<String> hdfs = getHdfsData(ProjectName);
        //get server data
        List<String> local = fileUtil.listDir(ProjectPathPrefix + ProjectName + "/Data/", false);
        List<String> result = new ArrayList<String>();
        //judge whether the server data is on the HDFS
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
     * 获取算法参数描述
     * @return String []dataList
     */
    @GetMapping("/getAlgorithmParameter")
    @ResponseBody
    public List<List<String>> getAlgorithmParameter(@RequestParam(value = "ProjectName") String ProjectName,
                                                    @RequestParam(value = "AlgorithmName") String AlgorithmName){
        List<List<String>> result = new ArrayList<List<String>>();
        try{
            //get ParameterDescribe file
            String [] arrtemp = AlgorithmName.split("\\.");
            File DataFormatFile = new File(ProjectPathPrefix+ProjectName+"/Algorithm/ParameterDescribe/"+arrtemp[0]+"ParameterDescribe.txt");
            InputStreamReader read = new InputStreamReader(new FileInputStream(DataFormatFile),"utf-8");
            BufferedReader bufferedReader = new BufferedReader(read);
            String describe = "";
            while (!(describe = bufferedReader.readLine()).equals("")){
                List<String> perParam = new ArrayList<String>();
                String [] arr= describe.split(" ");
                for (int i = 0; i < arr.length; i++) {
                    perParam.add(arr[i]);
                }
                result.add(perParam);
            }
            read.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return result;

    }
    //----------------------------------------------GET DATA OR ALGORITHM-----------------------------------------------
    @GetMapping("/getSize")
    @ResponseBody
    public String getSize(@RequestParam(value = "DataName") String DataName) throws Exception{
        String path = HDFSPathPrefix + DataName;
        return hdfsFileUtil.getSizeK(path)+"KB";
    }


    //原始的文件httpservlet上传
    @RequestMapping("/DownLoad")
    @ResponseBody
    public  String  downLoadFile(Model model,HttpServletResponse response,String descFile) throws IOException {
        File file=new File("src/main/webappfiles/Project/aa/Algorithm/"+descFile);
        if(descFile==null||!file.exists()){
            model.addAttribute("msg", "亲,您要下载的文件"+descFile+"不存在");
            return "load";
        }
        System.out.println(descFile);
        try{
            response.reset();
            //设置ContentType
            response.setContentType("application/octet-stream; charset=utf-8");
            //处理中文文件名中文乱码问题
            String fileName=new String(file.getName().getBytes("utf-8"),"ISO-8859-1");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            IOUtils.copy(new FileInputStream(file), response.getOutputStream());
            return null;
        }catch (Exception e) {
            model.addAttribute("msg", "下载失败");
            return "load";

        }

    }

    @RequestMapping("/fileDownload_servlet")
    @ResponseBody
     public void fileDownload_servlet(HttpServletRequest request, HttpServletResponse response) {
        files_utils_dg.FilesDownload_stream(request,response,"/filesOut/Download/mst.txt");
     }


}

