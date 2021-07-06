package com.ecloud.app.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * 执行本地python脚本
 */
public class JPython {

    /**
     * 转换为jpg图片格式
     *
     * @param imgPath
     * @return 0:成功 1:失败
     */
    public static int convertImageFile(String imgPath) {
        String pyPath = System.getProperty("user.dir") + "/py/convertImageFile.py";
        String[] arguments = new String[]{"python", pyPath, imgPath};
        int re = 1;

        BufferedReader in = null;
        try {
            Process process = Runtime.getRuntime().exec(arguments);
            in = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            //java代码中的process.waitFor()返回值为0表示我们调用python脚本成功，
            re = process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return re;
    }
}
