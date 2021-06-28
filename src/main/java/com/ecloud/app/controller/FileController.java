package com.ecloud.app.controller;

import com.ecloud.app.common.Base64Utils;
import com.ecloud.app.common.FileUtils;
import com.ecloud.app.common.JPython;
import com.ecloud.app.service.ECloudService;
import com.ecloud.app.service.FaceDetectService;
import com.ecloud.app.service.ObjectClassicService;
import com.ecloud.app.service.UniversalDetectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Controller
public class FileController {
    @Autowired
    private ECloudService cloudService;
    @Autowired
    private FaceDetectService faceDetectService;
    @Autowired
    private UniversalDetectService universalDetectService;
    @Autowired
    private ObjectClassicService objectClassicService;
    static final Logger logger = LoggerFactory.getLogger(FileController.class);

    // 图片上传
    @PostMapping("/file/upload")
    public String fileUpload(@RequestParam("upload_img") MultipartFile[] files) {
        String classic = null;

        for (MultipartFile file : files) {
            String name = file.getOriginalFilename();

            if (file.getSize() == 0) {
                logger.error("ERROR!!!FILE CAN'T BE NULL!");
                continue;
            }

            FileOutputStream fos = null;
            File newFile = null;
            try {
                // 存储至服务器临时资源
                File tmpFile;
                String absolutePath = System.getProperty("user.dir") + "/imgs/" + name;
                String dirPath = System.getProperty("user.dir") + "/imgs";
                tmpFile = new File(dirPath);
                if (!tmpFile.exists()) {
                    tmpFile.mkdir();
                }
                fos = new FileOutputStream(absolutePath);
                fos.write(FileUtils.convertBytes(file.getInputStream()));

                // 图片格式转换
                JPython.convertImageFile(absolutePath);

                // 获取转换格式后的输入流
                String newName = name.substring(0, name.lastIndexOf(".")) + ".jpg";
                String newFilePath = absolutePath.substring(0, absolutePath.lastIndexOf(".")) + ".jpg";
                newFile = new File(newFilePath);
                long size = newFile.length();


                // 上传至对象存储资源
                boolean containsFace = faceDetectService.faceDetect(new FileInputStream(newFile));
                if (containsFace) {
                    classic = "human";
                    cloudService.objectUpload("human", newName, new FileInputStream(newFile), size);
                    logger.info("对象'{}', 检测到人脸，归类为human", name);
                } else {
                    // 转为base64编码
                    String base64 = Base64Utils.toBase64(new FileInputStream(newFile));
                    // 识别对象名称
                    String detectName = universalDetectService.universalDetect(base64);
                    detectName = detectName.split(",")[0];
                    // 查询该物品所属类别
                    classic = objectClassicService.findClassicByName(detectName);
                    // 没有查询到该对象所属何类，归在其他类
                    classic = classic == null ? "others" : classic;
                    logger.info(" 对象'{}', 通用识别物品名称: {}, 归类为:  {}", newName, detectName, classic);
                    cloudService.objectUpload(classic, name, new FileInputStream(newFile), size);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }
        return "redirect:/pages/widgets/" + classic;
    }
}