package com.ecloud.app.controller;

import com.ecloud.app.service.ECloudService;
import com.ecloud.app.service.FaceDetectService;
import com.ecloud.app.service.ObjectClassicService;
import com.ecloud.app.service.UniversalDetectService;
import com.ecloud.app.common.Base64Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

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

    // 上传至相册
    @PostMapping("/file/upload")
    public String fileUpload(@RequestParam("upload_img") MultipartFile[] files) {
        for (MultipartFile file : files) {
            if (file == null) {
                logger.error("ERROR!!!FILE CAN'T BE NULL");
                continue;
            }

            try {
                String name = file.getOriginalFilename();
                InputStream stream = file.getInputStream();
                long size = file.getSize();
                InputStream inputStream = file.getInputStream();
                boolean containsFace = faceDetectService.faceDetect(inputStream);
                if (containsFace) {
                    //识别到人脸
                    cloudService.objectUpload("human", name, file.getInputStream(), size);
                    logger.info("对象'{}', 检测到人脸，归类为human", name);
                } else {
                    String base64 = Base64Utils.toBase64(stream);
                    // 识别对象名称
                    String detectName = universalDetectService.universalDetect(base64);
                    detectName = detectName.split(",")[0];
                    // 查询该物品所属类别
                    String classic = objectClassicService.findClassicByName(detectName);
                    // 没有查询到该对象所属何类，归在其他类
                    classic = classic == null ? "others" : classic;
                    logger.info(" 对象'{}', 通用识别物品名称: {}, 归类为:  {}", name, detectName, classic);
                    cloudService.objectUpload(classic, name, file.getInputStream(), size);
                }
            } catch (IOException e) {
                logger.error("{}", e);
            }
        }
        return "redirect:/pages/widgets";
    }
}