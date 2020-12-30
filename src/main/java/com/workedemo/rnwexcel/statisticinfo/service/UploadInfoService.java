package com.workedemo.rnwexcel.statisticinfo.service;

import com.google.common.collect.ListMultimap;
import com.workedemo.rnwexcel.statisticinfo.entity.PersonalData;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

public interface UploadInfoService {
    public String uploadExcel(MultipartFile multipartFile, String jpgPath, Model model);

    public boolean validateInfo(MultipartFile multipartFile, String jpgPath);

    public ListMultimap<String, PersonalData> readExcel(MultipartFile multipartFile);

    public ListMultimap<String, String> readNameList(String jpgPath);

    public ListMultimap<String, PersonalData> analysisExcel(ListMultimap<String, PersonalData> personalDataMultimap, ListMultimap<String, String> stringMultimap);

}
