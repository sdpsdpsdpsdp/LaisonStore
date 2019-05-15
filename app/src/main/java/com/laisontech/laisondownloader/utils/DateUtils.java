package com.laisontech.laisondownloader.utils;

import android.text.TextUtils;

/**
 * Created by SDP
 * on 2019/5/13
 * Des：
 */
public class DateUtils {
    public static String formatDetailDate(String updateDate) {
        if (TextUtils.isEmpty(updateDate) || updateDate.length() < 14) return "";
        StringBuffer sb = new StringBuffer();
        sb.append(updateDate.substring(4, 6))
                .append("/")
                .append(updateDate.substring(6, 8))
                .append("/")
                .append(updateDate.substring(0, 4))
                .append(" ")
                .append(updateDate.substring(8, 10))
                .append(":")
                .append(updateDate.substring(10, 12))
                .append(":")
                .append(updateDate.substring(12, 14));
        return sb.toString();
    }
}
