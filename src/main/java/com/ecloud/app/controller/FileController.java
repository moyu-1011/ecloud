package com.ecloud.app.controller;

import com.ecloud.app.service.ECloudService;
import com.ecloud.app.service.FaceDetectService;
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
                logger.info("Upload file '{}' containsFace: {}", name, containsFace);
                if (containsFace) {
                    //识别到人脸
                    cloudService.objectUpload("face-1011", name, file.getInputStream(), size);
                } else {
                    String base64 = Base64Utils.toBase64(stream);
                    String detectName = universalDetectService.universalDetect(base64);
                    logger.info("universal detect name: {}", detectName);
                    // 根据detectName遍历种类
                    // TODO
                    // bucketName还没改
                    // Is it possible that the input stream that is specified in the request has already been fully read?
                    // If the input stream is a file stream, have you tried specifying the original file in the request instead of the input stream of the file?
                    cloudService.objectUpload("base1", name, file.getInputStream(), size);
                }
            } catch (IOException e) {
                logger.error("{}", e);
            }
        }
        return "redirect:/pages/widgets";
    }
}