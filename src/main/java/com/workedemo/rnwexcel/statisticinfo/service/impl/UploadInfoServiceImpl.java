package com.workedemo.rnwexcel.statisticinfo.service.impl;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import com.workedemo.rnwexcel.statisticinfo.entity.PersonalData;
import com.workedemo.rnwexcel.statisticinfo.service.UploadInfoService;
import com.workedemo.rnwexcel.statisticinfo.utils.UploadUtils;
import org.apache.poi.ss.usermodel.*;
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
        String fileName = multipartFile.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (fileNames.length > 0 && (suffix.equals("xlsx") || suffix.equals("xls"))) {
            return true;
        }
        return false;
    }

    /***
     * 读取xlsx格式excel文件（默认只读取表格的第一个页签）
     * @param multipartFile
     * @return List<PersonalData>
     */
    @Override
    public ListMultimap<String, PersonalData> readExcel(MultipartFile multipartFile) {
        InputStream inputStream = null;
        Workbook workbook = null;
        ListMultimap<String, PersonalData> personalDataMap = LinkedListMultimap.create();
        try {
            inputStream = multipartFile.getInputStream();
            workbook = WorkbookFactory.create(inputStream);
            inputStream.close();
            //工作表对象
            Sheet sheet = workbook.getSheetAt(0);
            //总行数
            int rowLength = sheet.getLastRowNum() + 1;
            //工作表的列
            Row row = sheet.getRow(3);
            //总列数
            int colLength = row.getLastCellNum();
            //得到单元格样式
            //CellStyle cellStyle = cell.getCellStyle();
            //System.out.println("行数：" + rowLength + ",列数：" + colLength);
            Cell cell = null;
            for (int i = 2; i < rowLength; i++) {
                PersonalData personalData = new PersonalData();
                row = sheet.getRow(i);
                if ("".equals(row.getCell(0) + "")) {
                    continue;
                }
                personalData.setName(row.getCell(0) + "");
                personalData.setIsOut(row.getCell(1) + "");
                personalData.setLocation(row.getCell(2) + "");
                personalData.setOutReason(row.getCell(3) + "");
                personalDataMap.put(row.getCell(0) + "", personalData);
                /*for (int j = 0; j < colLength; j++) {
                    cell = row.getCell(j);
                    //将所有的需要读的Cell表格设置为String格式
                    if (cell != null)
                        cell.setCellType(CellType.STRING);
                    //对Excel进行修改
                    if (i > 0 && j == 1)
                        cell.setCellValue("1000");
                    System.out.print(cell.getStringCellValue() + "\t");
                }*/
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return personalDataMap;
    }

    @Override
    public ListMultimap<String, String> readNameList(String jpgPath) {
        File file = new File(jpgPath);
        String[] fileNames = file.list();
        ListMultimap<String, String> nameNReason = LinkedListMultimap.create();
        for (int i = 0; i < fileNames.length; i++) {
            String[] temp = fileNames[i].split("-");
            if (temp.length > 1) {
                nameNReason.put(temp[0], temp[1].substring(0, temp[1].indexOf(".jpg")));
            }
        }
        return nameNReason;
    }

    @Override
    public ListMultimap<String, PersonalData> analysisExcel(ListMultimap<String, PersonalData> personalDataMultimap, ListMultimap<String, String> stringMultimap) {
        //遍历stringMultimap,将已存在的更新excel，不存在的追加
        //
        for (Object key : stringMultimap.keySet()) {
            String keyValue = key.toString();
            System.out.println(keyValue);
            PersonalData personalData = null;
            if (personalDataMultimap.containsKey(keyValue)) {
                personalData = personalDataMultimap.get(keyValue).get(0);
                personalData.setOutReason(stringMultimap.get(keyValue) + "");
            } else {
                personalData = new PersonalData();
                personalData.setName(keyValue);
                personalData.setIsOut("否");
                personalData.setLocation("");
                personalData.setOutReason(stringMultimap.get(keyValue) + "");
            }
            personalDataMultimap.put(keyValue, personalData);
        }
        //
        //
        return personalDataMultimap;
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
