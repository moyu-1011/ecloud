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
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
        ExecutorService exec = Executors.newCachedThreadPool(); // 缓冲线程池
        long d1 = System.currentTimeMillis();

        for (MultipartFile file : files) {
            exec.submit(() -> {
                String classic = null;  // 图像类别
                String fileName = file.getOriginalFilename();   // 文件名
                String savePath = System.getProperty("user.dir") + "/imgs"; // 保存路径
                String filePath = savePath + "/" + fileName;    // 保存的全路径
                String newFileName = fileName.substring(0, fileName.lastIndexOf(".")) + "x" + ".jpg";   // 格式转换后名字
                String newFilePath = savePath + "/" + newFileName;  // 转换后全路径

                FileInputStream in1 = null;
                FileInputStream in2 = null;
                FileInputStream in3 = null;
                FileInputStream in4 = null;
                try {
                    // 存储至服务器临时资源
                    boolean saveSuccess = FileUtils.saveAsLocal(file);
                    logger.info("'{}' 保存状态: {}", fileName, saveSuccess);

                    // 图片格式转换
                    JPython.convertImageFile(filePath);

                    // 获取转换格式后的输入流
                    // 移动云api入参传入被读取过的输入流时，会报错
                    in1 = new FileInputStream(newFilePath);
                    in2 = new FileInputStream(newFilePath);
                    in3 = new FileInputStream(newFilePath);
                    in4 = new FileInputStream(newFilePath);
                    long size = new File(newFilePath).length();

                    // 上传至对象存储资源
                    boolean containsFace = faceDetectService.faceDetect(in1);
                    if (containsFace) {
                        classic = "human";
                        cloudService.objectUpload("human", newFilePath, in2, size);
                        logger.info("对象'{}', 检测到人脸，归类为human", fileName);
                    } else {
                        String base64 = Base64Utils.toBase64(in3);
                        String detectName = universalDetectService.universalDetect(base64);
                        // 查询该物品所属类别
                        classic = objectClassicService.findClassicByName(detectName);
                        // 没有查询到该对象所属何类，归在其他类
                        classic = classic == null ? "others" : classic;
                        logger.info(" 对象'{}', 通用识别物品名称: {}, 归类为:  {}", newFilePath, detectName, classic);
                        cloudService.objectUpload(classic, fileName, in4, size);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        in1.close();
                        in2.close();
                        in3.close();
                        in4.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                boolean b1 = new File(filePath).delete();
                logger.info("delete '{}': {}", filePath, b1);
                if (!classic.equals("human")) {
                    boolean b2 = new File(newFilePath).delete();
                    logger.info("delete '{}': {}", newFilePath, b2);
                }
            });
        }

        try {
            exec.shutdown();
            exec.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        logger.info("upload file counts: {}, cost: {}ms", files.length, System.currentTimeMillis() - d1);

        return "redirect:/pages/widgets";
    }
}