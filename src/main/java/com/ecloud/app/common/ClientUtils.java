package com.ecloud.app.common;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.ecloud.app.service.ECloudService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 对象存储的单例
 */
public class ClientUtils {
    private static final String accessKey = "XLQXHDHOWE4Y3ASVA66G";
    private static final String accessSecret = "9nhzTLgQK9gkh41kHtJ4rsvX6YWrL22TyN0JXhCr";
    private static final String endpoint = "eos-chongqing-1.cmecloud.cn";
    private static final ClientConfiguration opts = new ClientConfiguration();
    private static final BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, accessSecret);
    private static final Logger logger = LoggerFactory.getLogger(ClientUtils.class);
    private static AmazonS3Client client = null;

    public static AmazonS3Client getInstance() {
        if (client == null) {
            synchronized (ECloudService.class) {
                if (client == null) {
                    opts.setSignerOverride("S3SignerType");
                    client = new AmazonS3Client(credentials, opts);
                    client.setEndpoint(endpoint);
                    logger.info("initializing oss client ... ...");
                }
            }
        } else
            logger.info("oss client has been initialized.");
        return client;
    }

}
