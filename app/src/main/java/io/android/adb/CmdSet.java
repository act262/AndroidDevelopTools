package io.android.adb;

import io.android.adb.cmd.ClearCommand;
import io.android.adb.cmd.RestartCommand;

/**
 * adb shell 命令集合
 *
 * @author act262@gmail.com
 */
public class CmdSet {

    public static void clearAppData(String pkg) {
        CmdExecutor executor = new CmdExecutor(pkg);
        executor.exec(new ClearCommand());
    }

    public static void restartApp(String pkg) {
        CmdExecutor executor = new CmdExecutor(pkg);
        executor.exec(new RestartCommand());
    }

}
