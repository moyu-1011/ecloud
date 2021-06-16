package com.ecloud.app.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {
    public static byte[] convertBytes(InputStream is) {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;

        try {
            while ((n = is.read(buffer)) != -1) {
                bao.write(buffer, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bao.toByteArray();
    }

}
