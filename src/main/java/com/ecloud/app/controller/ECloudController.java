package com.ecloud.app.controller;

import com.ecloud.app.common.DateUtils;
import com.ecloud.app.common.SortUtils;
import com.ecloud.app.pojo.PictureInfo;
import com.ecloud.app.pojo.StorageObject;
import com.ecloud.app.service.ECloudService;
import com.ecloud.app.service.ObjectTypeService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
public class ECloudController {
    @Autowired
    private ECloudService eCloudService;
    @Autowired
    private ObjectTypeService objectTypeService;

    private static final Logger logger = LoggerFactory.getLogger(ECloudController.class);

    @GetMapping(value = {"/pages/widgets/{type}", "/pages/widgets"})
    public String widgets(@PathVariable(value = "type", required = false) String type, Model model) {
        List<PictureInfo> pictures = null;
        // 获取相册的所有类别
        List<String> picTypes = objectTypeService.findTypes();
        model.addAttribute("picTypes", picTypes);

        if ("".equals(type) || null == type) {
            // 获取全部相册
            pictures = eCloudService.objectsGet("base1");
            logger.info("请求全部相册");
        } else {
            // 获取分类相册
            pictures = eCloudService.objectsGet(type);
            logger.info("请求分类相册: {}", type);
        }
        model.addAttribute("pictures", pictures);
        return "pages/widgets";
    }


    // 根据图片大小,时间先后排序
    @GetMapping("/pages/widgets/sortType={sortType}")
    public String widgetsSort(@PathVariable("sortType") String sortType, Model model) {
        List<PictureInfo> pictures = eCloudService.objectsGet("base1");
        SortUtils.sort(pictures, sortType);
        logger.info("pictures sort: {}", sortType);
        model.addAttribute("pictures", pictures);
        return "pages/widgets";
    }

    // 回收站
    @GetMapping("/pages/recycle")
    public String recyclePage(Model model) {
        List<PictureInfo> recycleInfo = eCloudService.objectsGet("recycle-bin");
        logger.info("访问回收站");
        model.addAttribute("list", recycleInfo);
        return "pages/recycle";
    }

    @GetMapping("/pages/recycle/sortType={sortType}")
    public String recycleSort(@PathVariable("sortType") String sortType, Model model) {
        List<PictureInfo> infos = eCloudService.objectsGet("recycle-bin");
        SortUtils.sort(infos, sortType);
        model.addAttribute("list", infos);
        return "pages/recycle";
    }

    //　删除存储对象
    @PostMapping("/pages/delete_completely")
    @ResponseBody
    public String objectsDelete(@RequestBody List<StorageObject> listCopy) {
        System.out.println(listCopy.toString());
        eCloudService.objectsDelete(listCopy);
        return "success";
    }

    // 先复制对象到其他bucket中，再删除原bucket中的对象
    @PostMapping("/pages/action/delete")
    @ResponseBody
    public String objectsCopyAndDelete(@RequestBody List<StorageObject> listCopy) {
        System.out.println(listCopy.toString());
        eCloudService.objectsCopyAndDelete(listCopy);
        return "pages/widgets";
    }

    // 从回收站中复原
    @PostMapping("/pages/action/recover")
    @ResponseBody
    public String objectsRecover(@RequestBody List<StorageObject> objects) {
        eCloudService.objectsRecover(objects);
        System.out.println(objects.toString());
        return "success";
    }

    //　以压缩文件方式保存对象到本地
    @PostMapping("/pages/action/save")
    @ResponseBody
    public ResponseEntity<byte[]> getZipAsStreamNew(@RequestBody List<StorageObject> object) {
        logger.info("accepted object {}", object);
        byte[] bytes = eCloudService.objectsSave(object);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment;filename=" + DateUtils.format(new Date()) + ".zip");
        return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.CREATED);
    }
}
