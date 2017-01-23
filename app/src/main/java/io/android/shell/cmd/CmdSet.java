package io.android.shell.cmd;

import java.io.IOException;

import io.android.shell.ICmdExecutor;
import io.android.shell.internal.CmdExecutor;
import io.android.shell.internal.SuCmdExecutor;

/**
 * shell 命令集合
 *
 * @author act262@gmail.com
 */
public class CmdSet {

    public static ICmdExecutor executor = new CmdExecutor();

    static ICmdExecutor suExecutor = new SuCmdExecutor();

    public static void clearAppData(String pkg) throws IOException {
        executor.exec("pm clear " + pkg);
    }

    public static void restartApp(String pkg) throws IOException {
        executor.exec("am start " + pkg + " -a android.intent.action.MAIN -c android.intent.category.LAUNCHER ");
    }

}
