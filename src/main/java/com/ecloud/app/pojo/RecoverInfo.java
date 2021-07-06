package com.ecloud.app.pojo;


import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 实体类：从回收站复原时数据
 */
public class RecoverInfo {
    private String keyName;
    @JsonProperty("bucket")
    private String originalBucketName;

    public RecoverInfo() {
    }

    public RecoverInfo(String keyName, String originalBucketName) {
        this.keyName = keyName;
        this.originalBucketName = originalBucketName;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getOriginalBucketName() {
        return originalBucketName;
    }

    public void setOriginalBucketName(String originalBucketName) {
        this.originalBucketName = originalBucketName;
    }

    @Override
    public String toString() {
        return "RecoverInfo{" +
                "keyName='" + keyName + '\'' +
                ", originalBucketName='" + originalBucketName + '\'' +
                '}';
    }
}
