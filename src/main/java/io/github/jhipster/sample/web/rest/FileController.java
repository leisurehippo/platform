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
 * @author Chris Mao(Zibing)
 *
 */
@RestController
@RequestMapping("/api")

public class FileController {

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
        boolean flagUpload,flagDescri;
        try{
            flagUpload = fileUtil.uploadFile(file,"Project\\"+ProjectName+"\\Algorithm\\algorithm\\");
            flagDescri = fileUtil.createFile("src/main/webappfiles/Project/"+ProjectName+"/Algorithm/ParameterDescribe/",file.getOriginalFilename().split("\\.")[0]+"ParameterDescribe.txt",ParameterDescribe);
        }catch (Exception e){
            return e.getMessage();
        }
        return (flagUpload && flagDescri)?"success":"fail";
    }
    @PostMapping("/uploadData")
    @ResponseBody
    public String handleDataUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam(value = "ProjectName") String ProjectName){
        JSONObject object = new JSONObject();
        if (object.isEmpty()) {
           object = new JSONObject();
        }

//        System.out.println(test);
        String []filename = file.getOriginalFilename().split("\\.");
        String format = filename[filename.length-1];
        boolean flag = false;
        try{
            File DataFormatFile = new File("src/main/webappfiles/Project/"+ProjectName+"/Describe&DataFormatLimit.txt");
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
            System.out.println(e.getMessage());
        }
        if (flag){
            boolean flagUpload;
            try{
                flagUpload = fileUtil.uploadFile(file,"Project/"+ProjectName+"/Data/");
            }catch (Exception e){
                return e.getMessage();
            }

            if (flagUpload) {
                object.put("result", "success");
                String success = object.toString();
                return success;
            } else {
                object.put("result", "fail");
                String fail = object.toString();
                return fail;
            }


        }else {
            object.put("result", "format error");
            String error =object .toString();
            return error;
        }




    }


    /**
     * 获取服务器端项目列表
     * @return
     */
    @GetMapping("/getServerProjectList")
    @ResponseBody
    public List<String> getServerProject(){
        List<String> results = new ArrayList<String>();
        File file = new File("src/main/webappfiles/Project/");
        if (file.exists()) {
            for (File file2 : file.listFiles()) {
                if (file2.isDirectory()) {
                    String [] arrList = file2.getAbsolutePath().split("\\\\");
                    results.add(arrList[arrList.length-1]);
                }
            }
        }
        return results;
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
        String destDirName = "src/main/webappfiles/Project/"+ProjectName;
        File dir = new File(destDirName);
        if (dir.exists()) // 判断目录是否存在
            return "exist";
        if (!destDirName.endsWith(File.separator)) {// 结尾是否以"/"结束
            destDirName = destDirName + File.separator;
        }
        boolean flagServer,flagHdfs;
        if (dir.mkdirs()) {
            flagServer = fileUtil.createFile(destDirName,"Describe&DataFormatLimit.txt",ProjectDescribe+"\n"+DataFormatLimit);
            //create HDFS
            try{
                flagHdfs = hdfsFileUtil.mkdir("/user/hadoop/data_platform/data/"+ProjectName+"/");
                if (flagHdfs) {
                    String localDir = "src\\main\\webappfiles\\Project\\" + ProjectName + "\\Describe&DataFormatLimit.txt";
                    String hdfsDir = "/user/hadoop/data_platform/data/" + ProjectName + "/Describe&DataFormatLimit.txt";
                    hdfsFileUtil.upload(localDir, hdfsDir, false);
                    if (!hdfsFileUtil.checkFile(hdfsDir))
                        flagHdfs = false;
                }
            }catch (Exception e){
                return "fail";
            }
            return (flagServer&&flagHdfs) ? "success" : "fail";
        }
        else
            return "fail";

    }

    /**
     * 删除项目
     * @param ProjectName
     * @return
     */
    @GetMapping("/deleteProject")
    @ResponseBody
    public String deleteProject(@RequestParam(value = "ProjectName") String ProjectName){
        String destDirName = "src/main/webappfiles/Project/"+ProjectName;
        File file = new File(destDirName);
        if (!file.exists()) {// 判断目录或文件是否存在
            return "not exist";
        } else {
            boolean flagServer,flagHdfs;
            flagServer = deleteDirectory(destDirName);
            //delete HDFS
            try{
                flagHdfs = hdfsFileUtil.delFile("/user/hadoop/data_platform/data/" + ProjectName, true);
            }catch (Exception e){
                return "fail";
            }
            return (flagServer&&flagHdfs) ? "success" : "fail";// 为目录时调用删除目录方法
        }
    }

    private boolean deleteDirectory(String dirPath) {// 删除目录（文件夹）以及目录下的文件
        // 如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!dirPath.endsWith(File.separator)) {
            dirPath = dirPath + File.separator;
        }
        File dirFile = new File(dirPath);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        File[] files = dirFile.listFiles();// 获得传入路径下的所有文件
        for (File file : files) {
            if (file.isFile()) {// 删除子文件
                flag = deleteFile(file.getAbsolutePath());
                System.out.println(file.getAbsolutePath() + " 删除成功");
                if (!flag)
                    break;// 如果删除失败，则跳出
            } else {// 运用递归，删除子目录
                flag = deleteDirectory(file.getAbsolutePath());
                if (!flag)
                    break;// 如果删除失败，则跳出
            }
        }
        return flag && dirFile.delete();
    }

    private boolean deleteFile(String filePath) {// 删除单个文件
        boolean flag = false;
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {// 路径为文件且不为空则进行删除
            file.delete();// 文件删除
            flag = true;
        }
        return flag;
    }

    /**
     * 获取本地算法
     * @return String []dataList
     */
    @GetMapping("/getServerAlgorithm")
    @ResponseBody
    public List<String> getServerAlgorithm(@RequestParam(value = "ProjectName") String ProjectName){
        return getLocalData(ProjectName,"Algorithm/algorithm");
    }

    public List<String> getLocalData(String ProjectName, String type) {
        List<String> results = new ArrayList<String>();
        File file = new File("src/main/webappfiles/Project/" + ProjectName + "/" + type + "/");
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length > 0) {
                for (File file2 : files) {
                    if (!file2.isDirectory()) {
                        String[] arrList = file2.getAbsolutePath().split("\\\\");
                        results.add(arrList[arrList.length - 1]);
                    }
                }
            }
        }
        return results;
    }

}

