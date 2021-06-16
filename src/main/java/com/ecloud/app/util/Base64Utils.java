package com.ecloud.app.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class Base64Utils {
    public static String toBase64(InputStream is) {
        ByteArrayOutputStream bao = null;
        try {
            bao = new ByteArrayOutputStream();
            // 操作（分段读取）
            byte[] flush = new byte[1024 * 4];// 缓冲容器
            int len = -1;// 接收长度
            while ((len = is.read(flush)) != -1) {
                bao.write(flush, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] byteArray = bao.toByteArray();

        return Base64.getEncoder().encodeToString(byteArray);

    }

}
