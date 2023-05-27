package com.example.secondhand.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * 图片上传
 */
@RestController
public class UploadFile {

    @RequestMapping("/uploadFile")
    public JSON uploadFile(MultipartFile file, HttpServletRequest request) {
        JSONObject json=new JSONObject();

        String filePath = System.getProperty("user.dir")+"/src/main/resources/static/uploadFile/";//上传到这个文件夹
        File file1 = new File(filePath);
        if (!file1.exists()) {
            file1.mkdirs();
        }

        String finalFilePath = filePath + file.getOriginalFilename().trim();
        File desFile = new File(finalFilePath);
        if (desFile.exists()) {
            desFile.delete();
        }
        try {
            file.transferTo(desFile);
            json.put("code",0);
            json.put("msg","ok");

            JSONObject json2=new JSONObject();
//            json2.put("src",finalFilePath);
            json2.put("src","/uploadFile/"+file.getOriginalFilename());
            json2.put("title","");
            json.put("data",json2);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            json.put("code",1);
        }
        System.out.println(json);
        return json;
    }
}
