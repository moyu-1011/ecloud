package com.ecloud.app.service.impl;

import com.chinamobile.bcop.api.sdk.ai.core.exception.ClientException;
import com.chinamobile.bcop.api.sdk.ai.core.model.CommonJsonObjectResponse;
import com.chinamobile.bcop.api.sdk.ai.facebody.AiFaceBody;
import com.ecloud.app.common.AiClientUtils;
import com.ecloud.app.common.FileUtils;
import com.ecloud.app.service.FaceDetectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class FaceDetectServiceImpl implements FaceDetectService {

    @Override
    public boolean faceDetect(InputStream is) {
        AiFaceBody aiFaceBody = AiClientUtils.getInstance();
        CommonJsonObjectResponse response = null;

        try {
            response = aiFaceBody.faceDetect(FileUtils.convertBytes(is), null);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        if (response == null || response.getCommonResult() == null) {
            return false;
        }
        return true;
    }

}
