package com.ecloud.app.service;

import com.ecloud.app.pojo.PictureInfo;
import com.ecloud.app.pojo.StorageObject;

import java.io.InputStream;
import java.util.List;

public interface ECloudService {
    List<PictureInfo> objectsGet(String bucketName);

    void objectsCopy(List<StorageObject> objects);

    void objectsDelete(List<StorageObject> objects);

    void objectsRecover(List<StorageObject> objects);

    void objectsCopyAndDelete(List<StorageObject> objects);

    InputStream objectGetAsStream(String bucketName, String keyName);

    void objectUpload(String bucketName, String keyName, InputStream is, Long length);

    byte[] objectsSave(List<StorageObject> objects);
}
