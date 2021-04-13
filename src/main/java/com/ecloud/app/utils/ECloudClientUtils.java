package com.ecloud.app.utils;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;

public class ECloudClientUtils {

    private static final String accessKey = "XLQXHDHOWE4Y3ASVA66G";
    private static final String accessSecret = "9nhzTLgQK9gkh41kHtJ4rsvX6YWrL22TyN0JXhCr";
    private static final String endpoint = "eos-chongqing-1.cmecloud.cn";

    private static final ClientConfiguration opts = new ClientConfiguration();
    private static final BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, accessSecret);
    private static AmazonS3Client client = null;

    private ECloudClientUtils() {
    }

    static {
        opts.setSignerOverride("S3SignerType");
        client = new AmazonS3Client(credentials, opts);
        client.setEndpoint(endpoint);
    }

    public static AmazonS3Client getInstance() {
        return client;
    }


}
