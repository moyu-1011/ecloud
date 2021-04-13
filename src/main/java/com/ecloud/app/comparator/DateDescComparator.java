package com.ecloud.app.comparator;


import com.ecloud.app.pojo.PictureInfo;

import java.util.Comparator;

public class DateDescComparator implements Comparator<PictureInfo> {
    @Override
    public int compare(PictureInfo o1, PictureInfo o2) {
        return o2.getLastModified().compareTo(o1.getLastModified());
    }
}
