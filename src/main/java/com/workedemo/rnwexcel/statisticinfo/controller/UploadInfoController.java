package com.workedemo.rnwexcel.statisticinfo.controller;

import com.workedemo.rnwexcel.statisticinfo.service.UploadInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Controller
@RequestMapping("/home")
public class UploadInfoController {
    @Autowired
    private UploadInfoService uploadInfoService;

    /***
     * 主页
     * @return
     */
    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    /***
     * multipartFile 表单上传
     * @param multipartFile
     * @param jpgPath
     * @param model
     * @return
     */
    @PostMapping("/upload")
    public String fileUpLoad(@RequestParam("multipartFile") MultipartFile multipartFile, @RequestParam("jpgPath") String jpgPath, Model model) {
//        return uploadInfoService.uploadExcel(multipartFile,jpgPath,model);
        return "index";
    }

    /***
     * excel文件解析，jpg文件名列表解析
     * @param multipartFile
     * @param jpgPath
     * @param model
     * @return
     */
    @PostMapping("/analysis")
    public String readExcel(@RequestParam("multipartFile") MultipartFile multipartFile, @RequestParam("jpgPath") String jpgPath, Model model) {
        if(uploadInfoService.validateInfo(multipartFile, jpgPath)){
            String str = uploadInfoService.readExcel(multipartFile);
        }
        return "";
    }
}
