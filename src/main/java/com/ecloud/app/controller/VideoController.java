package com.ecloud.app.controller;

import com.ecloud.app.common.ConvertVideo;
import com.ecloud.app.common.VideoComposite;
import org.jim2mov.core.MovieInfoProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Objects;

@Controller
public class VideoController {

    private final Logger logger = LoggerFactory.getLogger(VideoController.class);

    @GetMapping("toVideo")
    public String toVideo() {
        return "/pages/video";
    }

    @GetMapping("/video")
    public String video(HttpServletRequest request, HttpServletResponse response) {
        try {
            FileInputStream fis = null;
            OutputStream os = null;

            File output = new File(System.getProperty("user.dir") + "/video/output.mp4");
            if (!output.exists()) {
                logger.info("the video not exist!");
                return null;
            }

            fis = new FileInputStream(System.getProperty("user.dir") + "/video/output.mp4");
            int size = fis.available(); // 得到文件大小
            byte data[] = new byte[size];
            fis.read(data); // 读数据
            fis.close();
            fis = null;
            response.setContentType("video/mp4"); // 设置返回的文件类型
            os = response.getOutputStream();
            os.write(data);
            os.flush();
            os.close();
            os = null;


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping(value = "/action/create_video")
    @ResponseBody
    public String createVideo() {
        System.setProperty("java.awt.headless", "false");

        String root = System.getProperty("user.dir");

        // 遍历图片
        File file = new File(root + "/imgs");
        if (!file.exists() || file.listFiles().length == 0) {
            return "失败: 还没有人物图片库,请先上传图片";
        }

        File[] listFiles = file.listFiles();
        String[] files = new String[listFiles.length];

        for (int i = 0; i < listFiles.length; i++) {
            files[i] = listFiles[i].getAbsolutePath();
            logger.info("saved img: {}", files[i]);
        }

        try {
            File output = new File(root + "/video/output.mov");
            if (output.exists()) {
                logger.info("已有输出视频,删除中...");
                boolean delete = output.delete();
                logger.info("删除视频: {}", delete);
            }

            // 合成视频
            new VideoComposite(files, MovieInfoProvider.TYPE_QUICKTIME_JPEG, "video/output.mov");

            // 视频转换mp4格式
            ConvertVideo.convert(root + "/video/output.mov", root + "/video/", root + "/");
        } catch (Exception e) {
            logger.error("{}", e);
            return "failure";
        }

        return "success";
    }

    @GetMapping("/action/delete_video")
    @ResponseBody
    public String deleteVideo() {
        File output = new File(System.getProperty("user.dir") + "/video/output.mp4");
        File origin = new File(System.getProperty("user.dir") + "/video/output.mov");

        if (output.exists()) {
            boolean b = output.delete();
            logger.info("delete '{}': {}", output, b);
        }

        if (origin.exists()) {
            boolean b = origin.delete();
            logger.info("delete '{}': {}", origin, b);
        }

        return "success";
    }

}
