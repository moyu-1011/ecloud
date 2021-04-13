package com.ecloud.app.utils;


import com.ecloud.app.comparator.DateAscComparator;
import com.ecloud.app.comparator.DateDescComparator;
import com.ecloud.app.comparator.SizeAscComparator;
import com.ecloud.app.comparator.SizeDescComparator;
import com.ecloud.app.enums.Sort;
import com.ecloud.app.pojo.PictureInfo;

import java.util.List;

public class SortUtils {
    public static List<PictureInfo> sort(List<PictureInfo> o, Sort sort) {
        if (Sort.DateAsc.equals(sort)) {
            o.sort(new DateAscComparator());
        }
        if (Sort.DateDesc.equals(sort)) {
            o.sort(new DateDescComparator());
        }
        if (Sort.SizeAsc.equals(sort)) {
            o.sort(new SizeAscComparator());
        }
        if (Sort.SizeDesc.equals(sort)) {
            o.sort(new SizeDescComparator());
        }
        return o;
    }

    public static List<PictureInfo> sort(List<PictureInfo> o, String sort) {
        if (Sort.DateAsc.toString().equals(sort)) {
            o.sort(new DateAscComparator());
        }
        if (Sort.DateDesc.toString().equals(sort)) {
            o.sort(new DateDescComparator());
        }
        if (Sort.SizeAsc.toString().equals(sort)) {
            o.sort(new SizeAscComparator());
        }
        if (Sort.SizeDesc.toString().equals(sort)) {
            o.sort(new SizeDescComparator());
        }
        return o;
    }
}
