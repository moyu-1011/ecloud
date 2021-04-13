package com.ecloud.app.pojo;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Date;

public class PictureInfo {
    private String keyName;
    private String url;
    private Date lastModified;
    private String formatDate;
    private Long size;
    private String formatSize;
    private String bucketName;


    public PictureInfo() {
    }

    public PictureInfo(String keyName, String url, Date lastModified, String formatDate,
                       Long size, String formatSize,String bucketName) {
        this.keyName = keyName;
        this.url = url;
        this.lastModified = lastModified;
        this.formatDate = formatDate;
        this.size = size;
        this.formatSize = formatSize;
        this.bucketName = bucketName;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getFormatDate() {
        return formatDate;
    }

    public void setFormatDate(String formatDate) {
        this.formatDate = formatDate;
    }

    public String getFormatSize() {
        return formatSize;
    }

    public void setFormatSize(String formatSize) {
        this.formatSize = formatSize;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
