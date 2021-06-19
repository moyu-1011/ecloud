package com.ecloud.app.common;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 大小格式转换
 **/
public class SizeUtils {

    public static String exchange(long size) {
        BigDecimal bigDecimal = new BigDecimal(size);
        BigDecimal divide;
        // 小于1M
        if (size < 1024 * 1024) {
            divide = bigDecimal.divide(BigDecimal.valueOf(1024L), 2, RoundingMode.HALF_UP);
            return divide.toString() + " KB";
        }
        // 1M以上
        divide = bigDecimal.divide(BigDecimal.valueOf(1024L * 1024L), 2, RoundingMode.HALF_UP);
        return divide.toString() + " MB";
    }

}
