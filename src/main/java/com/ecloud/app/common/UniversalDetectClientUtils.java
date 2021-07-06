package com.ecloud.app.common;

import com.chinamobile.cmss.sdk.ECloudDefaultClient;
import com.chinamobile.cmss.sdk.IECloudClient;
import com.chinamobile.cmss.sdk.http.constant.Region;
import com.chinamobile.cmss.sdk.http.signature.Credential;
import com.ecloud.app.service.impl.UniversalDetectServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UniversalDetectClientUtils {
    private static final String accessKey = "afe7c82c7fc54fbab1d7bce4cad3d380";
    private static final String accessSecret = "a24bb149b9d94ac786e8ae9559347236";
    private static final Credential credential = new Credential(accessKey, accessSecret);
    private static final Logger logger = LoggerFactory.getLogger(UniversalDetectServiceImpl.class);
    private static IECloudClient ecloudClient = null;


    public static IECloudClient getInstance() {
        if (ecloudClient == null) {
            synchronized (UniversalDetectServiceImpl.class) {
                if (ecloudClient == null) {
                    ecloudClient = new ECloudDefaultClient(credential, Region.POOL_SZ);
                }
            }
        }
        return ecloudClient;
    }


}
