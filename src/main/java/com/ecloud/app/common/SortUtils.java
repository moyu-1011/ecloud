package com.ecloud.app.common;


import com.ecloud.app.enums.SortEnum;
import com.ecloud.app.pojo.PictureInfo;

import java.util.List;

/**
 * 自定义集合的排序
 */
public class SortUtils {
    public static List<PictureInfo> sort(List<PictureInfo> o, SortEnum sort) {
        if (SortEnum.DateAsc.equals(sort)) {
            o.sort(new DateAscComparator());
        }
        if (SortEnum.DateDesc.equals(sort)) {
            o.sort(new DateDescComparator());
        }
        if (SortEnum.SizeAsc.equals(sort)) {
            o.sort(new SizeAscComparator());
        }
        if (SortEnum.SizeDesc.equals(sort)) {
            o.sort(new SizeDescComparator());
        }
        return o;
    }

    public static List<PictureInfo> sort(List<PictureInfo> o, String sort) {
        if (SortEnum.DateAsc.toString().equals(sort)) {
            o.sort(new DateAscComparator());
        }
        if (SortEnum.DateDesc.toString().equals(sort)) {
            o.sort(new DateDescComparator());
        }
        if (SortEnum.SizeAsc.toString().equals(sort)) {
            o.sort(new SizeAscComparator());
        }
        if (SortEnum.SizeDesc.toString().equals(sort)) {
            o.sort(new SizeDescComparator());
        }
        return o;
    }
}
