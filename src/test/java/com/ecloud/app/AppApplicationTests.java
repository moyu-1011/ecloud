package com.ecloud.app;

import com.chinamobile.bcop.api.sdk.ai.core.constant.Region;
import com.chinamobile.bcop.api.sdk.ai.core.exception.ClientException;
import com.chinamobile.bcop.api.sdk.ai.core.model.CommonJsonObjectResponse;
import com.chinamobile.bcop.api.sdk.ai.facebody.AiFaceBody;
import com.ecloud.app.service.ECloudService;
import com.ecloud.app.service.FaceDetectService;
import com.ecloud.app.service.UniversalDetectService;
import com.ecloud.app.util.Base64Utils;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

    /**
     * 测试通用识别
     */
    @Test
    void universalDetect() {
        InputStream stream = eCloudService.objectGetAsStream("base1", "4.PNG");
        String base64 = Base64Utils.toBase64(stream);
        String objectType = universalDetectService.universalDetect(base64);
        logger.info("ObjectType: {}", objectType);
    }


    /**
     * 测试是否存在人脸
     */
    @Test
    void faceTest() {
        InputStream stream = eCloudService.objectGetAsStream("base1", "my.jpg");
        boolean isFace = faceDetectService.faceDetect(stream);
        logger.info("The images contains face: {}.", isFace);
    }


    @Test
    void contextLoads() {
    }


}
