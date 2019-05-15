package com.laisontech.laisondownloader.utils;

import com.laisontech.laisondownloader.R;
import com.laisontech.laisondownloader.global.GlobalData;
import com.laisontech.laisondownloader.global.GlobalData;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by SDP
 * on 2019/5/8
 * Des：
 */
public class CalculateUtils {
    public static String doubleToString(double val, int decimalPlace) {
        if (decimalPlace < 1) return String.valueOf(val);
        StringBuilder sb = new StringBuilder();
        sb.append("0.");
        for (int i = 0; i < decimalPlace; i++) {
            sb.append("0");
        }
        DecimalFormat df = new DecimalFormat(sb.toString());
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(val);
    }

    public static String getAppSize(double fileSize) {
        return doubleToString(fileSize, 2) + GlobalData.getInstance().getRes(R.string.MB);
    }
}
