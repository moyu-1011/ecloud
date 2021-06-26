package com.ecloud.app.service.impl;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.chinamobile.cmss.sdk.ECloudServerException;
import com.chinamobile.cmss.sdk.IECloudClient;
import com.chinamobile.cmss.sdk.request.EngineImageClassifyDetectPostRequest;
import com.chinamobile.cmss.sdk.response.EngineImageClassifyDetectResponse;
import com.chinamobile.cmss.sdk.response.bean.EngineClassify;
import com.ecloud.app.common.UniversalDetectClientUtils;
import com.ecloud.app.service.UniversalDetectService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class UniversalDetectServiceImpl implements UniversalDetectService {


    @Override
    public String universalDetect(String uri) {
        IECloudClient client = UniversalDetectClientUtils.getInstance();

        String objectType = null;
        try {
            EngineImageClassifyDetectPostRequest request = new EngineImageClassifyDetectPostRequest();
            // 图片base64
            request.setImage(uri);
            request.setUserId("moyu1011");
            EngineImageClassifyDetectResponse response = client.call(request);
            if ("OK".equals(response.getState())) {
                //通用物品检测
                List<EngineClassify> body = response.getBody();
                objectType = body != null ? body.get(0).getClasses() : null;
            }
        } catch (IOException | ECloudServerException | IllegalAccessException | AmazonS3Exception e) {
        }

        return objectType;
    }
}
