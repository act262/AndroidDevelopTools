package io.android.shell.cmd;

import java.io.IOException;

/**
 * SystemProperties相关功能的兼容方法
 *
 * @author act262@gmail.com
 * @see android.os.SystemProperties
 */
public class SystemPropertiesCmds {

    /**
     * API19以前只能通过system以上的权限修改系统属性
     */
    public static void set(String key, String value) throws IOException {
        CmdSet.suExecutor.exec("setprop " + key + " " + value);
    }


}
