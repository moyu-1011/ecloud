package com.ecloud.app.controller;

import com.ecloud.app.pojo.ObjectCopy;
import com.ecloud.app.pojo.PictureInfo;
import com.ecloud.app.service.ECloudService;
import com.ecloud.app.util.DateUtils;
import com.ecloud.app.util.SortUtils;
import com.google.gson.Gson;
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


    @GetMapping("/pages/widgets")
    public String widgets(Model model) {
        List<PictureInfo> infos = eCloudService.objectsGet("base1");
        model.addAttribute("list", infos);
        return "pages/widgets";
    }

    @GetMapping("/pages/widgets/sortType={sortType}")
    public String widgetsSort(@PathVariable("sortType") String sortType, Model model) {
        List<PictureInfo> infos = eCloudService.objectsGet("base1");
        SortUtils.sort(infos, sortType);
        model.addAttribute("list", infos);
        return "pages/widgets";
    }

    @GetMapping("/pages/recycle")
    public String recyclePage(Model model) {
        List<PictureInfo> recycleInfo = eCloudService.objectsGet("recycle-bin");
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

    @PostMapping("/pages/delete_completely")
    @ResponseBody
    public String objectsDelete(@RequestBody List<ObjectCopy> listCopy) {
        System.out.println(listCopy.toString());
        eCloudService.objectsDelete(listCopy);
        return "success";
    }

    @PostMapping("/pages/action/delete")
    @ResponseBody
    public String objectsCopyAndDelete(@RequestBody List<ObjectCopy> listCopy) {
        System.out.println(listCopy.toString());
        eCloudService.objectsCopyAndDelete(listCopy);
        return "pages/widgets";
    }

    @PostMapping("/pages/action/recover")
    @ResponseBody
    public String objectsRecover(@RequestBody List<ObjectCopy> listCopy) {
        eCloudService.objectsRecover(listCopy);
        System.out.println(listCopy.toString());
        return "success";
    }

    @PostMapping("/pages/action/save")
    @ResponseBody
    public ResponseEntity<byte[]> getZipAsStream(@RequestParam("buckets") String buckets, @RequestParam("keys") String keys) throws IOException {
        Gson gson = new Gson();
        String[] bucketNames = gson.fromJson(buckets, String[].class);
        String[] keyNames = gson.fromJson(keys, String[].class);
        // 设置头消息
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Disposition", "attachment;filename=" + DateUtils.format(new Date()) + ".zip");
        byte[] bytes = eCloudService.objectsSave(bucketNames, keyNames);
        return new ResponseEntity<byte[]>(bytes, httpHeaders, HttpStatus.CREATED);
    }
}
