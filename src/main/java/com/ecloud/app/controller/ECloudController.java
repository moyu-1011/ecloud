package com.ecloud.app.controller;

import com.ecloud.app.common.DateUtils;
import com.ecloud.app.common.FileUtils;
import com.ecloud.app.common.SortUtils;
import com.ecloud.app.pojo.PictureInfo;
import com.ecloud.app.pojo.StorageObject;
import com.ecloud.app.service.ECloudService;
import com.ecloud.app.service.ObjectTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Controller
public class ECloudController {
    @Autowired
    private ECloudService eCloudService;
    @Autowired
    private ObjectTypeService objectTypeService;
    private static final Logger logger = LoggerFactory.getLogger(ECloudController.class);

    /**
     * 请求全部相册
     *
     * @param model
     * @return
     */
    @GetMapping(value = {"/pages/widgets", "/pages/widgets/all"})
    public String widgets(Model model) {
        // 获取相册的所有类别
        List<String> picTypes = objectTypeService.findTypes();
        List<PictureInfo> pictures = eCloudService.objectsGetAll(picTypes);

        logger.info("请求全部相册");
        model.addAttribute("picTypes", picTypes);
        model.addAttribute("pictures", pictures);
        model.addAttribute("activeType", "all");
        return "pages/widgets";
    }

    /**
     * 分类相册请求
     *
     * @param type  请求相册类型
     * @param model
     * @return
     */
    @GetMapping("/pages/widgets/{type}")
    public String widgetsType(@PathVariable("type") String type, Model model) {
        // 获取相册的所有类别
        List<String> picTypes = objectTypeService.findTypes();
        // 获取分类相册
        List<PictureInfo> pictures = eCloudService.objectsGet(type);
        logger.info("请求分类相册: {}", type);
        model.addAttribute("picTypes", picTypes);
        model.addAttribute("pictures", pictures);
        model.addAttribute("activeType", type);
        return "pages/widgets";
    }


    /**
     * 全部相册将图片按大小，时间排序
     *
     * @param sortType
     * @param model
     * @return
     */
    @GetMapping(value = {"/pages/widgets/sortType={sortType}", "/pages/widgets/all/sortType={sortType}"})
    public String widgetsSort(@PathVariable("sortType") String sortType, Model model) {
        List<String> picTypes = objectTypeService.findTypes();
        List<PictureInfo> pictures = eCloudService.objectsGetAll(picTypes);
        SortUtils.sort(pictures, sortType);
        logger.info("pictures sort: {}", sortType);
        model.addAttribute("pictures", pictures);
        model.addAttribute("picTypes", picTypes);
        model.addAttribute("activeType", "all");
        return "pages/widgets";
    }

    /**
     * 分类相册排序
     */
    @GetMapping("/pages/widgets/{type}/sortType={sortType}")
    public String widgetsTypeSort(@PathVariable("type") String type, @PathVariable("sortType") String sortType, Model model) {
        List<String> picTypes = objectTypeService.findTypes();
        List<PictureInfo> pictures = eCloudService.objectsGet(type);
        SortUtils.sort(pictures, sortType);
        logger.info("pictures sort: {}", sortType);
        model.addAttribute("pictures", pictures);
        model.addAttribute("picTypes", picTypes);
        model.addAttribute("activeType", type);
        return "pages/widgets";
    }


    /**
     * 回收站
     *
     * @param model
     * @return
     */
    @GetMapping("/pages/recycle")
    public String recyclePage(Model model) {
        List<PictureInfo> recycleInfo = eCloudService.objectsGet("recycle-bin");
        logger.info("访问回收站");
        model.addAttribute("list", recycleInfo);
        return "pages/recycle";
    }

    /**
     * 图片按大小，时间排序
     *
     * @param sortType
     * @param model
     * @return
     */
    @GetMapping("/pages/recycle/sortType={sortType}")
    public String recycleSort(@PathVariable("sortType") String sortType, Model model) {
        List<PictureInfo> infos = eCloudService.objectsGet("recycle-bin");
        SortUtils.sort(infos, sortType);
        model.addAttribute("list", infos);
        return "pages/recycle";
    }

    /**
     * 完全删除对象
     *
     * @param listCopy
     * @return
     */
    @PostMapping("/pages/delete_completely")
    @ResponseBody
    public String objectsDelete(@RequestBody List<StorageObject> listCopy) {
        eCloudService.objectsDelete(listCopy);
        return "success";
    }

    /**
     * 相册图片移动
     *
     * @param listCopy
     * @return
     */
    @PostMapping("/pages/action/delete")
    @ResponseBody
    public String objectsCopyAndDelete(@RequestBody List<StorageObject> listCopy) {
        eCloudService.objectsCopyAndDelete(listCopy);

        Iterator<StorageObject> it = listCopy.iterator();
        while (it.hasNext()) {
            StorageObject next = it.next();
            String deletePath = next.getKeyName();
            logger.info("deletePath: {}", deletePath);
            boolean delete = FileUtils.deleteLocal(deletePath);
            logger.info("delete: {}", delete);
        }

        return "pages/widgets";
    }

    /**
     * 从回收站回收对象
     *
     * @param objects
     * @return
     */
    @PostMapping("/pages/action/recover")
    @ResponseBody
    public String objectsRecover(@RequestBody List<StorageObject> objects) {
        eCloudService.objectsRecover(objects);
        return "success";
    }

    /**
     * 将图片对象批量压缩下载至本地
     *
     * @param object
     * @return
     */
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
