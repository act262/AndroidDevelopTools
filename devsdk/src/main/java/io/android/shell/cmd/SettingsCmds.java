package io.android.shell.cmd;

import android.content.ContentResolver;

import java.io.IOException;

/**
 * Settings 设置相关的命令
 *
 * @author act262@gmail.com
 * @see android.provider.Settings
 */
public class SettingsCmds {

    /**
     * @see android.provider.Settings.Global#putString(ContentResolver, String, String)
     */
    public static void putStringGlobal(String key, String value) throws IOException {
        CmdSet.suExecutor.exec("settings put global " + key + " " + value);
    }

    /**
     * @see android.provider.Settings.Secure#putString(ContentResolver, String, String)
     */
    public static void putStringSecure(String key, String value) throws IOException {
        CmdSet.suExecutor.exec("settings put secure " + key + " " + value);
    }

    /**
     * @see android.provider.Settings.System#putString(ContentResolver, String, String)
     */
    public static void putStringSystem(String key, String value) throws IOException {
        CmdSet.suExecutor.exec("settings put system " + key + " " + value);
    }

}
