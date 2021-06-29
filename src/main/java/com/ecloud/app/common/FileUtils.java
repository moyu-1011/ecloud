package com.ecloud.app.common;

import org.springframework.web.multipart.MultipartFile;

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
        } finally {
            try {
                bao.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
     * 存储到本地 路径: 项目根目录下/imgs/
     * @param file
     * @return
     */
    public static boolean saveAsLocal(MultipartFile file) {
        String savePath = System.getProperty("user.dir") + "/imgs";
        String filePath = savePath + "/" + file.getOriginalFilename();
        File saveDir = new File(savePath);

        if (!saveDir.exists()) {
            saveDir.mkdir();
        }

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(filePath);
            outputStream.write(convertBytes(file.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

}
