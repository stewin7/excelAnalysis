package com.workedemo.rnwexcel.statisticinfo.utils;

import java.io.File;

public class UploadUtils {
    public final static String IMG_PATH_PREFIX = "static/upload/XLS/";
    public static File getDirFile(){
        // 构建上传文件的存放 “文件夹” 路径
        String fileDirPath = new String("src/main/resources/" + IMG_PATH_PREFIX);
        File fileDir = new File(fileDirPath);
        if(!fileDir.exists()){
            // 递归生成文件夹
            fileDir.mkdirs();
        }
        return fileDir;
    }
}