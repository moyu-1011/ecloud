package com.ecloud.app.pojo;

/**
 * 实体类: 跨bucket复制时数据
 */
public class ObjectCopy {
    private String destinationBucket;
    private String sourceBucket;
    private String keyName;

    public ObjectCopy(String destinationBucket, String sourceBucket, String keyName) {
        this.destinationBucket = destinationBucket;
        this.sourceBucket = sourceBucket;
        this.keyName = keyName;
    }

    public String getDestinationBucket() {
        return destinationBucket;
    }

    public void setDestinationBucket(String destinationBucket) {
        this.destinationBucket = destinationBucket;
    }

    public String getSourceBucket() {
        return sourceBucket;
    }

    public void setSourceBucket(String sourceBucket) {
        this.sourceBucket = sourceBucket;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    @Override
    public String toString() {
        return "ObjectCopy{" +
                "destinationBucket='" + destinationBucket + '\'' +
                ", sourceBucket='" + sourceBucket + '\'' +
                ", keyName='" + keyName + '\'' +
                '}';
    }
}
