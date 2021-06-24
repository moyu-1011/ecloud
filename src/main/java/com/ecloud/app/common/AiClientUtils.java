package com.ecloud.app.common;

import com.chinamobile.bcop.api.sdk.ai.core.constant.Region;
import com.chinamobile.bcop.api.sdk.ai.facebody.AiFaceBody;
import com.ecloud.app.service.impl.FaceDetectServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Ai人脸检测单例
 */
public class AiClientUtils {
    private static final String accessKey = "CIDC-AK-ed527649-69c4-4297-93ff-bc8fb34201ee";
    private static final String accessSecret = "CIDC-SK-c4b503bb-5cce-45a8-9019-cd3c7694e960";
    private static AiFaceBody aiFaceBody = null;

    public static AiFaceBody getInstance() {
        if (aiFaceBody == null) {   // DCL
            synchronized (FaceDetectServiceImpl.class) {
                if (aiFaceBody == null) {
                    aiFaceBody = new AiFaceBody(Region.POOL_CS, accessKey, accessSecret);
                }
            }
        }
        return aiFaceBody;
    }

}
