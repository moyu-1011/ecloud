package com.ecloud.app.service;

import java.io.InputStream;

public interface FaceDetectService {
    /**
     * 检测到人脸返回true, 否则false
     *
     * @param is
     * @return boolean
     */
    boolean faceDetect(InputStream is);
}
