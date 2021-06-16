package com.ecloud.app.service.impl;

import com.chinamobile.cmss.sdk.ECloudDefaultClient;
import com.chinamobile.cmss.sdk.ECloudServerException;
import com.chinamobile.cmss.sdk.IECloudClient;
import com.chinamobile.cmss.sdk.http.constant.Region;
import com.chinamobile.cmss.sdk.http.signature.Credential;
import com.chinamobile.cmss.sdk.request.EngineImageClassifyDetectPostRequest;
import com.chinamobile.cmss.sdk.response.EngineImageClassifyDetectResponse;
import com.chinamobile.cmss.sdk.response.bean.EngineClassify;
import com.ecloud.app.service.UniversalDetectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class UniversalDetectServiceImpl implements UniversalDetectService {
    private static final String accessKey = "afe7c82c7fc54fbab1d7bce4cad3d380";
    private static final String accessSecret = "a24bb149b9d94ac786e8ae9559347236";

    static final Logger logger = LoggerFactory.getLogger(UniversalDetectServiceImpl.class);

    @Override
    public String universalDetect(String uri) {
        Credential credential = new Credential(accessKey, accessSecret);
        IECloudClient ecloudClient = new ECloudDefaultClient(credential, Region.POOL_SZ);

        String objectType = null;
        try {
            EngineImageClassifyDetectPostRequest request = new EngineImageClassifyDetectPostRequest();
            // 图片base64
            request.setImage(uri);
            request.setUserId("moyu1011");
            EngineImageClassifyDetectResponse response = ecloudClient.call(request);
            if ("OK".equals(response.getState())) {
                //通用物品检测
                List<EngineClassify> body = response.getBody();
                // body如果为null,objectType取null
                objectType = body != null ? body.get(0).getClasses() : null;
                logger.info("Universal Detect results: {}", objectType);
            }
        } catch (IOException | ECloudServerException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return objectType;
    }
}
