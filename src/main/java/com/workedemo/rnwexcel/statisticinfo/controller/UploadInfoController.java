package com.workedemo.rnwexcel.statisticinfo.controller;

import com.google.common.collect.ListMultimap;
import com.workedemo.rnwexcel.statisticinfo.entity.PersonalData;
import com.workedemo.rnwexcel.statisticinfo.service.UploadInfoService;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;

@Controller
/*@RequestMapping("/home")*/
public class UploadInfoController {
    @Autowired
    private UploadInfoService uploadInfoService;

    /***
     * 主页1
     * @return
     */
    @RequestMapping("/index1")
    public String index1() {
        return "index";
    }

    /***
     * 主页2
     * @return
     */
    @RequestMapping("/index")
    public String index() {
        return "index2";
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
    @ResponseBody
    public String readExcel(HttpServletResponse response, @RequestParam("multipartFile") MultipartFile multipartFile, @RequestParam("jpgPath") String jpgPath, Model model) {
        //文件传送通过验证后进行后续操作
        if (uploadInfoService.validateInfo(multipartFile, jpgPath)) {
            //解析excel内容
            ListMultimap<String, PersonalData> personalDataMultimap = uploadInfoService.readExcel(multipartFile);
            //读取jpgPath获取图片文件名列表
            ListMultimap<String, String> stringMultimap = uploadInfoService.readNameList(jpgPath);
            //解析文件名列表并更新excel表格
            ListMultimap<String, PersonalData> personalDataMultimapNew = uploadInfoService.analysisExcel(personalDataMultimap, stringMultimap);
            //调用下载
            downloadExcel(response, personalDataMultimap, model);
        }
        return "success";
    }

    @ResponseBody
    public String downloadExcel(HttpServletResponse response, ListMultimap<String, PersonalData> personalDataMultimap, Model model) {
        String[] title = {"姓名", "是否离京", "工作地点", "出京时间及行程"};
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("基本信息");
        HSSFRow row = sheet.createRow(0);
        for (int i = 0; i < title.length; i++) {
            row.createCell(i).setCellValue(title[i]);
        }
        //写入excel
        int rowNum = 1;
        for (Object key : personalDataMultimap.keySet()) {
            String keyValue = key + "";
            HSSFRow hssfRow = sheet.createRow(rowNum);
            PersonalData personalData = personalDataMultimap.get(keyValue).get(0);
            hssfRow.createCell(0).setCellValue(personalData.getName());
            hssfRow.createCell(1).setCellValue(personalData.getIsOut());
            hssfRow.createCell(2).setCellValue(personalData.getLocation());
            hssfRow.createCell(3).setCellValue(personalData.getOutReason());
            rowNum++;
        }
        try {
            String fileName = "结果.xlsx";
            //String fileName = URLEncoder.encode("下载文件的中文名称") + ".xlsx","utf-8");
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);//设置文件头编码方式和文件名
            OutputStream out = response.getOutputStream();
            workbook.write(out);
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("result_msg", "下载成功!");
        return "success";
    }
}
