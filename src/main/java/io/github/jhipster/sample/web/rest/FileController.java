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

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    private String upload(MultipartFile file, String type){
        if(!file.isEmpty()){
            try {
                FileUploadingUtil.uploadFile(file, type);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return"上传失败,"+e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return"上传失败,"+e.getMessage();
            }
            return"上传成功";
        }else{
            return"上传失败，因为文件是空的.";
        }
    }
    /**
     * 文件上传具体实现方法;
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @ResponseBody
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam(value = "ProjectName") String ProjectName){
        return upload(file,"Project/"+ProjectName+"/Algorithm");
    }
    @PostMapping("/uploadData")
    @ResponseBody
    public String handleDataUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam(value = "ProjectName") String ProjectName){
        return upload(file,"Project/"+ProjectName+"/Data");
    }


    /**
     * 获取服务器端项目列表
     * @return
     */
    @GetMapping("/getServerProject")
    @ResponseBody
    public List<String> getServerProject(){
        List<String> results = new ArrayList<String>();
        File file = new File("src/main/webappfiles/Project/");
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length > 0) {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        String [] arrList = file2.getAbsolutePath().split("\\\\");
                        results.add(arrList[arrList.length-1]);
                    }
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
    public String createProject(@RequestParam(value = "ProjectName") String ProjectName){
        String destDirName = "src/main/webappfiles/Data/"+ProjectName;
        File dir = new File(destDirName);
        if (dir.exists()) // 判断目录是否存在
            return "exist";
        if (!destDirName.endsWith(File.separator)) {// 结尾是否以"/"结束
            destDirName = destDirName + File.separator;
        }
        if (dir.mkdirs())
            return "success";
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
        String destDirName = "src/main/webappfiles/Data/"+ProjectName;
        File file = new File(destDirName);
        if (!file.exists()) {// 判断目录或文件是否存在
            return "not exist";
        } else {
            return deleteDirectory(destDirName)?"success":"fail";// 为目录时调用删除目录方法
        }
    }

    public boolean deleteDirectory(String dirPath) {// 删除目录（文件夹）以及目录下的文件
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
        for (int i = 0; i < files.length; i++) {// 循环遍历删除文件夹下的所有文件(包括子目录)
            if (files[i].isFile()) {// 删除子文件
                flag = deleteFile(files[i].getAbsolutePath());
                System.out.println(files[i].getAbsolutePath() + " 删除成功");
                if (!flag)
                    break;// 如果删除失败，则跳出
            } else {// 运用递归，删除子目录
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                    break;// 如果删除失败，则跳出
            }
        }
        if (!flag)
            return false;
        if (dirFile.delete()) {// 删除当前目录
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteFile(String filePath) {// 删除单个文件
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
    public List<String> getServerAlgorithm(){
        return getLocalData("Algorithm");
    }

    public List<String> getLocalData(String type){
        List<String> results = new ArrayList<String>();
        File file = new File("src/main/webappfiles/" + type + "/");
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length > 0) {
                for (File file2 : files) {
                    if (!file2.isDirectory()) {
                        String [] arrList = file2.getAbsolutePath().split("\\\\");
                        results.add(arrList[arrList.length-1]);
                    }
                }
            }
        }
        return results;
    }

}

