package com.workedemo.rnwexcel.statisticinfo.service.impl;

import com.workedemo.rnwexcel.statisticinfo.service.UploadInfoService;
import com.workedemo.rnwexcel.statisticinfo.utils.UploadUtils;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Service
public class UploadInfoServiceImpl implements UploadInfoService {

    @Override
    public boolean validateInfo(MultipartFile multipartFile, String jpgPath) {
        //validation all passing then analysis excel file and jpg names.
        //TODO validate jpgPath     -one
        File file = new File(jpgPath);
        String[] fileNames = file.list();

        //TODO validate excel file  -two

        if (fileNames.length > 0 && true) {
            return true;
        }
        return false;
    }

    @Override
    public String uploadExcel(MultipartFile multipartFile, String jpgPath, Model model) {
        //判断multipartFile&jpgPath是否为空
        if (multipartFile.isEmpty() || "".equals(jpgPath)) {
            model.addAttribute("result_msg", "文件或路径为空，请检查");
            return "index";
        }

        //创建输入输出流
        InputStream inputStream = null;
        OutputStream outputStream = null;


        try {
            // 存放上传的文件夹
            File fileDir = UploadUtils.getDirFile();
            // 输出文件夹绝对路径 – 这里的绝对路径是相当于当前项目的路径而不是“容器”路径
            //System.out.println(fileDir.getAbsolutePath());

            //获取文件的输入流
            inputStream = multipartFile.getInputStream();
            //获取上传时的文件名
            String fileName = multipartFile.getOriginalFilename();
            //注意是路径+文件名
            File targetFile = new File(fileDir.getAbsolutePath() + "/" + fileName);

            //判断文件父目录是否存在
            if (!targetFile.getParentFile().exists()) {
                //不存在就创建一个
                targetFile.getParentFile().mkdir();
            }

            //获取文件的输出流
            outputStream = new FileOutputStream(targetFile);
            //最后使用资源访问器FileCopyUtils的copy方法拷贝文件
            FileCopyUtils.copy(inputStream, outputStream);

            //告诉页面上传成功了
            model.addAttribute("result_msg", "上传成功");
        } catch (IOException e) {
            e.printStackTrace();
            //出现异常，则告诉页面失败
            model.addAttribute("result_msg", "上传失败");
        } finally {
            //无论成功与否，都有关闭输入输出流
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
