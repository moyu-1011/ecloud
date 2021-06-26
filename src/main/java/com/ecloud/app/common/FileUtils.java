package com.ecloud.app.common;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 文件工具类
 */
public class FileUtils {
    /**
     * 返回字节流
     *
     * @param is
     * @return
     */
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


    /**
     * 返回jpg格式的输入流
     *
     * @param inputStream
     * @return
     */
    public static InputStream convertImageFile(InputStream inputStream) {
        BufferedImage bufferedImage;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteArrayInputStream bais = null;

        try {
            bufferedImage = ImageIO.read(inputStream);
            BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
            newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
            ImageIO.write(newBufferedImage, "jpg", baos);
            bais = new ByteArrayInputStream(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bais != null)
                    bais.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bais;
    }

    /**
     * 检查图片格式
     * @param fileName
     * @return
     */
    public static boolean standardSuffix(String fileName) {
        List<String> listSuffix = Arrays.asList("BMP", "JPG", "WBMP", "JPEG", "PNG", "GIF");
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        return listSuffix.contains(suffix.toUpperCase());
    }

}
