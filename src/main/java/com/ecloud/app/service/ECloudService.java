package com.ecloud.app.service;

import com.ecloud.app.pojo.ObjectCopy;
import com.ecloud.app.pojo.PictureInfo;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface ECloudService {
    List<PictureInfo> objectsGet(String bucketName);

    void objectsCopy(List<ObjectCopy> listCopy);

    void objectsDelete(List<ObjectCopy> listCopy);

    void objectsDelete(String bucketName, String keyname);

    void objectsRecover(List<ObjectCopy> listCopy);

    void objectsUpload(String bucketName, String keyName, InputStream is, Long length);

    void objectsCopyAndDelete(List<ObjectCopy> listCopy);

    byte[] objectsSave(String[] buckets, String[] keys) throws IOException;

    InputStream objectGetAsStream(String bucketName, String keyName);

    void objectUpload(String bucketName, String keyName, InputStream is, Long length);
}
