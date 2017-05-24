package io.github.jhipster.sample.web.rest;

/**
 * Created by WJ on 2017/4/18.
 */
import com.codahale.metrics.annotation.Timed;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    /**
     * 文件上传具体实现方法;
     * @param file
     * @return
     */
    @RequestMapping("/upload")
    @ResponseBody
    public String handleFileUpload(@RequestParam("file")MultipartFile file){
        if(!file.isEmpty()){
            try {

                FileUploadingUtil.uploadFile(file);
//                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(file.getOriginalFilename())));
//                out.write(file.getBytes());
//                out.flush();
//                out.close();
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

//    @GetMapping("/list")
//    @Timed
////    @RequestMapping(value = "/", method = RequestMethod.GET)
//    public String list(HttpServletRequest request, HttpServletResponse response, Model model) {
//        iniFileDir(request);
//
//        System.out.println(request.getAttribute("files"));
//        model.addAttribute("files", FileUploadingUtil.getFileMap());
//        return "files/list";
//    }

//    @PostMapping("/uploadtest")
//    @Timed
////    @RequestMapping(value = "/upload", method = RequestMethod.POST)
//    public String doUpload(HttpServletRequest request) {
//        iniFileDir(request);
//
//        try {
//            MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
//            Map<String, String> uploadedFiles = FileUploadingUtil.upload(mRequest.getFileMap());
//
//            Iterator<Entry<String, String>> iter = uploadedFiles.entrySet().iterator();
//            while (iter.hasNext()) {
//                Entry<String, String> each = iter.next();
//                System.out.print("Uploaded File Name = " + each.getKey());
//                System.out.println(", Saved Path in Server = " + each.getValue());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "redirect:/files/";
//    }

//    @RequestMapping("/greeting")
//    public String greeting(@RequestParam(value = "name", required = false, defaultValue = "World") String name, Model model) {
//        model.addAttribute("name", name);
//        return "greeting";
//    }

//    //文件上传相关代码
//    @RequestMapping("/batch/upload")
//    @ResponseBody
//    public String upload(@RequestParam("test") MultipartFile file) {
//        if (file.isEmpty()) {
//            return "文件为空";
//        }
//        // 获取文件名
//        String fileName = file.getOriginalFilename();
//        logger.info("上传的文件名为：" + fileName);
//        // 获取文件的后缀名
//        String suffixName = fileName.substring(fileName.lastIndexOf("."));
//        logger.info("上传的后缀名为：" + suffixName);
//        // 文件上传后的路径
//        String filePath = "E://test//";
//        // 解决中文问题，liunx下中文路径，图片显示问题
//        // fileName = UUID.randomUUID() + suffixName;
//        File dest = new File(filePath + fileName);
//        // 检测是否存在目录
//        if (!dest.getParentFile().exists()) {
//            dest.getParentFile().mkdirs();
//        }
//        try {
//            file.transferTo(dest);
//            return "上传成功";
//        } catch (IllegalStateException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return "上传失败";
//    }
//    //文件下载相关代码
//    @RequestMapping("/download")
//    public String downloadFile(org.apache.catalina.servlet4preview.http.HttpServletRequest request, HttpServletResponse response){
//        String fileName = "FileUploadTests.java";
//        if (fileName != null) {
//            //当前是从该工程的WEB-INF//File//下获取文件(该目录可以在下面一行代码配置)然后下载到C:\\users\\downloads即本机的默认下载的目录
//            String realPath = request.getServletContext().getRealPath(
//                "//WEB-INF//");
//            File file = new File(realPath, fileName);
//            if (file.exists()) {
//                response.setContentType("application/force-download");// 设置强制下载不打开
//                response.addHeader("Content-Disposition",
//                    "attachment;fileName=" + fileName);// 设置文件名
//                byte[] buffer = new byte[1024];
//                FileInputStream fis = null;
//                BufferedInputStream bis = null;
//                try {
//                    fis = new FileInputStream(file);
//                    bis = new BufferedInputStream(fis);
//                    OutputStream os = response.getOutputStream();
//                    int i = bis.read(buffer);
//                    while (i != -1) {
//                        os.write(buffer, 0, i);
//                        i = bis.read(buffer);
//                    }
//                    System.out.println("success");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    if (bis != null) {
//                        try {
//                            bis.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    if (fis != null) {
//                        try {
//                            fis.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//        }
//        return null;
//    }
//
//    //多文件上传
//    @RequestMapping(value = "/batch/upload", method = RequestMethod.POST)
//    @ResponseBody
//    public String handleFileUpload(HttpServletRequest request) {
//        List<MultipartFile> files = ((MultipartHttpServletRequest) request)
//            .getFiles("file");
//        MultipartFile file = null;
//        BufferedOutputStream stream = null;
//        for (int i = 0; i < files.size(); ++i) {
//            file = files.get(i);
//            if (!file.isEmpty()) {
//                try {
//                    byte[] bytes = file.getBytes();
//                    stream = new BufferedOutputStream(new FileOutputStream(
//                        new File(file.getOriginalFilename())));
//                    stream.write(bytes);
//                    stream.close();
//
//                } catch (Exception e) {
//                    stream = null;
//                    return "You failed to upload " + i + " => "
//                        + e.getMessage();
//                }
//            } else {
//                return "You failed to upload " + i
//                    + " because the file was empty.";
//            }
//        }
//        return "upload successful";
//    }
//
//    private void iniFileDir(HttpServletRequest request) {
//        FileUploadingUtil.FILEDIR = request.getSession().getServletContext().getRealPath("/") + "files/";
//        if (FileUploadingUtil.FILEDIR == null) {
//            FileUploadingUtil.FILEDIR = request.getSession().getServletContext().getRealPath("/") + "files/";
//        }
//    }



}

