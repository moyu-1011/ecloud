package com.ecloud.app;

import com.ecloud.app.common.Base64Utils;
import com.ecloud.app.common.FileUtils;
import com.ecloud.app.common.JPython;
import com.ecloud.app.repository.ObjectClassicRepository;
import com.ecloud.app.service.ECloudService;
import com.ecloud.app.service.FaceDetectService;
import com.ecloud.app.service.ObjectTypeService;
import com.ecloud.app.service.UniversalDetectService;
import com.ecloud.app.service.impl.FaceDetectServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@SpringBootTest
class AppApplicationTests {
    public static final Logger logger = LoggerFactory.getLogger(AppApplicationTests.class);

    @Autowired
    private ECloudService eCloudService;
    @Autowired
    private FaceDetectService faceDetectService;
    @Autowired
    private UniversalDetectService universalDetectService;
    @Autowired
    private ObjectTypeService objectTypeService;
    @Autowired
    private ObjectClassicRepository objectClassicRepository;
    @Autowired
    FaceDetectServiceImpl faceDetectServiceImpl;

    /**
     * 测试数据库
     */
    @Test
    void dbTest() {
        // update
//        objectTypeService.updateTypeById("animal", 1);

        // add
//        ObjectType objectType = new ObjectType();
//        objectType.setType("objectA");
//        objectTypeService.saveAndFlush(objectType);
//        logger.info("save object: {}", objectType);

        // delete
//        objectTypeService.deleteById(7);

        // select
//        List<String> types = objectTypeService.findTypes();
//        logger.info("All types from table object_type: {}", types);

        String vehicle = objectClassicRepository.findClassicByName("飞机");
        Assertions.assertEquals("vehicle", vehicle);
        String lady = objectClassicRepository.findClassicByName("女士");
        Assertions.assertEquals("human", lady);
        String cat = objectClassicRepository.findClassicByName("猫");
        Assertions.assertEquals("animal", cat);
        String snow = objectClassicRepository.findClassicByName("雪");
        Assertions.assertEquals("landscape", snow);
        String bridge = objectClassicRepository.findClassicByName("桥");
        Assertions.assertEquals("architecture", bridge);
    }

    /**
     * 测试通用识别
     */
    @Test
    void universalDetect() {
//        InputStream stream = eCloudService.objectGetAsStream("human", "human.jpg");
//        String base64 = Base64Utils.toBase64(stream);
//        String objectName = universalDetectService.universalDetect(base64);
//        Assertions.assertEquals("人", objectName.split(",")[0]);
//
//        try {
//            FileInputStream inputStream = new FileInputStream("D:/imgs/dog.gif");
//            String base64A = Base64Utils.toBase64(inputStream);
//            String objectNameA = universalDetectService.universalDetect(base64A);
//            logger.info("{}", objectNameA);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
    }


    /**
     * 测试是否存在人脸
     */
    @Test
    void faceTest() {
//        InputStream stream = eCloudService.objectGetAsStream("human", "human.jpg");
//        boolean isFace = faceDetectService.faceDetect(stream);
//        Assertions.assertTrue(isFace);
    }


    @Test
    void contextLoads() {
    }


}
