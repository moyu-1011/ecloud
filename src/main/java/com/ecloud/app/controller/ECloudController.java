package com.ecloud.app.controller;

import com.ecloud.app.pojo.PictureInfo;
import com.ecloud.app.pojo.RecoverInfo;
import com.ecloud.app.utils.DateUtils;
import com.ecloud.app.utils.ECloudUtils;
import com.ecloud.app.utils.SortUtils;
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
    private ECloudUtils eCloud;

    @GetMapping("/oss")
    public String objectsGet(Model model) {
        List<PictureInfo> infos = eCloud.objectsGet("base1");
        model.addAttribute("list", infos);
        return "oss";
    }

    @GetMapping("/oss/sortType={sortType}")
    public String objectsSort(@PathVariable("sortType") String sortType, Model model) {
        List<PictureInfo> infos = eCloud.objectsGet("base1");
        SortUtils.sort(infos, sortType);
        int length = infos.size();
        model.addAttribute("list", infos);
        return "oss";
    }


    @GetMapping("/pages/widgets")
    public String widgets(Model model) {
        List<PictureInfo> infos = eCloud.objectsGet("base1");
        model.addAttribute("list", infos);
        return "pages/widgets";
    }


    @GetMapping("/pages/widgets/sortType={sortType}")
    public String widgetsSort(@PathVariable("sortType") String sortType, Model model) {
        List<PictureInfo> infos = eCloud.objectsGet("base1");
        SortUtils.sort(infos, sortType);
        model.addAttribute("list", infos);
        return "pages/widgets";
    }


    @GetMapping("/pages/recycle")
    public String recyclePage(Model model) {
        List<PictureInfo> recycleInfo = eCloud.objectsGet("recycle-bin");
        model.addAttribute("list", recycleInfo);
        return "pages/recycle";
    }


    @GetMapping("/pages/recycle/sortType={sortType}")
    public String recycleSort(@PathVariable("sortType") String sortType, Model model) {
        List<PictureInfo> infos = eCloud.objectsGet("recycle-bin");
        SortUtils.sort(infos, sortType);
        model.addAttribute("list", infos);
        return "pages/recycle";
    }


    @PostMapping("/pages/delete_completely")
    @ResponseBody
    public String deleteCompletely(@RequestBody String keys) {
        Gson gson = new Gson();
        List<String> list = gson.fromJson(keys, List.class);

        list.forEach(key -> {
            eCloud.objectDelete("recycle-bin", key);
        });

        return keys;
    }


    @PostMapping("/pages/action/delete")
    @ResponseBody
    public String delete(@RequestBody List<RecoverInfo> infos) {
        for (RecoverInfo info : infos) {
            eCloud.objectCopyAndDelete(info.getOriginalBucketName(), info.getKeyName(), "recycle-bin", info.getKeyName());
        }
        return "pages/widgets";
    }


    @PostMapping("/pages/action/recover")
    @ResponseBody
    public String recover(@RequestParam("buckets") String bucket, @RequestParam("keys") String key) {
        Gson gson = new Gson();
        String[] buckets = gson.fromJson(bucket, String[].class);
        String[] keys = gson.fromJson(key, String[].class);

        for (int i = 0; i < buckets.length; i++) {
            eCloud.objectRecover(buckets[i], keys[i]);
        }

        return "success";
    }


    @PostMapping("/pages/action/save")
    @ResponseBody
    public ResponseEntity<byte[]> getZipStream(@RequestParam("buckets") String buckets, @RequestParam("keys") String keys) throws IOException {
        Gson gson = new Gson();
        String[] bucketNames = gson.fromJson(buckets, String[].class);
        String[] keyNames = gson.fromJson(keys, String[].class);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Disposition", "attachment;filename=" + DateUtils.format(new Date()) + ".zip");

        byte[] bytes = eCloud.objectsSave(bucketNames, keyNames);

        return new ResponseEntity<byte[]>(bytes, httpHeaders, HttpStatus.CREATED);
    }



}
