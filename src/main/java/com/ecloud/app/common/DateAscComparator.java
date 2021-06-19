package com.ecloud.app.common;


import com.ecloud.app.pojo.PictureInfo;

import java.util.Comparator;

public class DateAscComparator implements Comparator<PictureInfo> {
    @Override
    public int compare(PictureInfo o1, PictureInfo o2) {
        return o1.getLastModified().compareTo(o2.getLastModified());
    }
}
