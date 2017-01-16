package io.micro.adt.util;

import android.text.TextUtils;

/**
 * 格式化工具
 *
 * @author act262@gmail.com
 */
public class DFormatter {

    public static int parseInt(String s) {
        if (TextUtils.isEmpty(s)) {
            return 0;
        } else {
            return Integer.parseInt(s);
        }
    }
}
