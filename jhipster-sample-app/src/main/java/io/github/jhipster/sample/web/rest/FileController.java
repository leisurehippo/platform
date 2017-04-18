package io.github.jhipster.sample.web.rest;

/**
 * Created by WJ on 2017/4/18.
 */
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import io.github.jhipster.sample.web.rest.util.FileUploadingUtil;

/**
 * 文件上传控制器
 *
 * @author Chris Mao(Zibing)
 *
 */
@Controller
@RequestMapping(value = "/files")
public class FileController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String list(HttpServletRequest request, HttpServletResponse response, Model model) {
        iniFileDir(request);

        System.out.println(request.getAttribute("files"));
        model.addAttribute("files", FileUploadingUtil.getFileMap());
        return "files/list";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String doUpload(HttpServletRequest request) {
        iniFileDir(request);

        try {
            MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
            Map<String, String> uploadedFiles = FileUploadingUtil.upload(mRequest.getFileMap());

            Iterator<Entry<String, String>> iter = uploadedFiles.entrySet().iterator();
            while (iter.hasNext()) {
                Entry<String, String> each = iter.next();
                System.out.print("Uploaded File Name = " + each.getKey());
                System.out.println(", Saved Path in Server = " + each.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/files/";
    }

    private void iniFileDir(HttpServletRequest request) {
        FileUploadingUtil.FILEDIR = request.getSession().getServletContext().getRealPath("/") + "files/";
        if (FileUploadingUtil.FILEDIR == null) {
            FileUploadingUtil.FILEDIR = request.getSession().getServletContext().getRealPath("/") + "files/";
        }
    }
}
