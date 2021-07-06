package com.ecloud.app.service;

import com.ecloud.app.pojo.PictureInfo;
import com.ecloud.app.pojo.StorageObject;

import java.io.InputStream;
import java.util.List;

public interface ECloudService {
    /**
     * 获取单个bucket内的对象
     *
     * @param bucketName
     * @return
     */
    List<PictureInfo> objectsGet(String bucketName);

    /**
     * 获取所有bucket内的对象
     *
     * @param types
     * @return
     */
    List<PictureInfo> objectsGetAll(List<String> types);

    /**
     * bucket内对象复制到其他bucket内
     *
     * @param objects
     */
    void objectsCopy(List<StorageObject> objects);

    /**
     * bucket内对象删除
     *
     * @param objects
     */
    void objectsDelete(List<StorageObject> objects);

    /**
     * 回收站复原
     *
     * @param objects
     */
    void objectsRecover(List<StorageObject> objects);

    /**
     * 对象移动
     *
     * @param objects
     */
    void objectsCopyAndDelete(List<StorageObject> objects);

    /**
     * 获取对象输入流
     *
     * @param bucketName
     * @param keyName
     * @return
     */
    InputStream objectGetAsStream(String bucketName, String keyName);

    /**
     * 上传
     *
     * @param bucketName
     * @param keyName
     * @param is
     * @param length
     */
    void objectUpload(String bucketName, String keyName, InputStream is, Long length);

    /**
     * 获取对象字节流
     *
     * @param objects
     * @return
     */
    byte[] objectsSave(List<StorageObject> objects);
}
