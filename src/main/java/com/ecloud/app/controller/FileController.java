package com.ecloud.app.controller;

import com.ecloud.app.utils.ECloudClientUtils;
import com.ecloud.app.utils.ECloudUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class FileController {

    @Autowired
    private ECloudUtils eCloud;

    @PostMapping("/file/upload")
    public String fileUpload(@RequestParam("upload_img") MultipartFile[] files) {
        for (MultipartFile file : files) {
            String name = file.getOriginalFilename();
            // 上传
            try {
                eCloud.objectUpload("base1", name, file.getInputStream(), file.getSize());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "redirect:/oss";
    }

}
