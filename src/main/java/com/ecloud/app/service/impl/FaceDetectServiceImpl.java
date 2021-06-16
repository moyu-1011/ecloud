package com.ecloud.app.service.impl;

import com.chinamobile.bcop.api.sdk.ai.core.constant.Region;
import com.chinamobile.bcop.api.sdk.ai.core.exception.ClientException;
import com.chinamobile.bcop.api.sdk.ai.core.model.CommonJsonObjectResponse;
import com.chinamobile.bcop.api.sdk.ai.facebody.AiFaceBody;
import com.ecloud.app.service.FaceDetectService;
import com.ecloud.app.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class FaceDetectServiceImpl implements FaceDetectService {
    private static final String accessKey = "CIDC-AK-ed527649-69c4-4297-93ff-bc8fb34201ee";
    private static final String accessSecret = "CIDC-SK-c4b503bb-5cce-45a8-9019-cd3c7694e960";
    AiFaceBody aiFaceBody = null;

    Logger logger = LoggerFactory.getLogger(FaceDetectServiceImpl.class);

    /**
     * 检测到人脸返回true, 否则false
     * @param  is
     * @return boolean
     */
    @Override
    public boolean faceDetect(InputStream is) {
        CommonJsonObjectResponse response = null;

        if (aiFaceBody == null) {   // DCL
            synchronized (this) {
                if (aiFaceBody == null) {
                    aiFaceBody = new AiFaceBody(Region.POOL_CS, accessKey, accessSecret);
                }
            }
        }

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
