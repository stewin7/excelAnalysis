package com.workedemo.rnwexcel.statisticinfo.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

public interface UploadInfoService {
    public String uploadExcel(MultipartFile multipartFile, String jpgPath, Model model);

    public boolean validateInfo(MultipartFile multipartFile, String jpgPath);

    public String readExcel(MultipartFile multipartFile);

    public String analysisExcel(MultipartFile multipartFile,String jpgPath);

}
