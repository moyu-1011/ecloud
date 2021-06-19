package com.ecloud.app.service.impl;

import com.chinamobile.bcop.api.sdk.ai.core.constant.Region;
import com.chinamobile.bcop.api.sdk.ai.core.exception.ClientException;
import com.chinamobile.bcop.api.sdk.ai.core.model.CommonJsonObjectResponse;
import com.chinamobile.bcop.api.sdk.ai.facebody.AiFaceBody;
import com.ecloud.app.common.AiClientUtils;
import com.ecloud.app.service.FaceDetectService;
import com.ecloud.app.common.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class FaceDetectServiceImpl implements FaceDetectService {
//    private static final String accessKey = "CIDC-AK-ed527649-69c4-4297-93ff-bc8fb34201ee";
//    private static final String accessSecret = "CIDC-SK-c4b503bb-5cce-45a8-9019-cd3c7694e960";
//    private static AiFaceBody aiFaceBody = null;
//    private static CommonJsonObjectResponse response = null;

    private static final Logger logger = LoggerFactory.getLogger(FaceDetectServiceImpl.class);

//    private static AiFaceBody getInstance() {
//        if (aiFaceBody == null) {   // DCL
//            synchronized (FaceDetectServiceImpl.class) {
//                if (aiFaceBody == null) {
//                    aiFaceBody = new AiFaceBody(Region.POOL_CS, accessKey, accessSecret);
//                    logger.info("Initializing class: aiFaceBody... ...");
//                }
//            }
//        } else
//            logger.info("AiFaceBody has been initialized...");
//        return aiFaceBody;
//    }

    /**
     * 检测到人脸返回true, 否则false
     *
     * @param is
     * @return boolean
     */
    @Override
    public boolean faceDetect(InputStream is) {
        AiFaceBody aiFaceBody = AiClientUtils.getInstance();
        CommonJsonObjectResponse response = null;
//        aiFaceBody = new AiFaceBody(Region.POOL_CS, accessKey, accessSecret);

        try {
            response = aiFaceBody.faceDetect(FileUtils.convertBytes(is), null);
            logger.info("Detected images results: {}", response.getCommonResult());
        } catch (ClientException e) {
            e.printStackTrace();
        }
        if (response == null || response.getCommonResult() == null) {
            return false;
        }
        return true;
    }
}
