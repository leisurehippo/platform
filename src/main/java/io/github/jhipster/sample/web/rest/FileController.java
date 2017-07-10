package io.github.jhipster.sample.web.rest;

/**
 * Created by WJ on 2017/4/18.
 */
import com.codahale.metrics.annotation.Timed;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
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
            System.out.println("上传失败，因为文件是空的");
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
    public String handleFileUpload(@RequestParam("file") MultipartFile file){
        return upload(file,"Algorithm");
    }
    @PostMapping("/uploadData")
    @ResponseBody
    public String handleDataUpload( @RequestParam("file") MultipartFile file){
        return upload(file,"Data");
    }


    /**
     * 获取本地数据
     * @param type
     * @return String []dataList
     */
    @PostMapping("/getLocalData")
    @ResponseBody
    public String [] getLocalData(@RequestParam(value = "Type") String type){
        String []dataList = {};
        ArrayList<String> arrayList = new ArrayList<String>();
        File file = new File("src/main/webappfiles/" + type + "/");
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length > 0) {
                for (File file2 : files) {
                    if (!file2.isDirectory()) {
                        String [] arrList = file2.getAbsolutePath().split("\\\\");
                        arrayList.add(arrList[arrList.length-1]);
                    }
                }
            }
        }
        return arrayList.toArray(dataList);
    }

}

