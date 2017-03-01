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

    public static ICmdExecutor suExecutor = new SuCmdExecutor();

    public static void clearAppData(String pkg) throws IOException {
        executor.exec("pm clear " + pkg);
    }

    public static void restartApp(String pkg) throws IOException {
        executor.exec("am start " + pkg + " -a android.intent.action.MAIN -c android.intent.category.LAUNCHER ");
    }

    public static void writeHosts(String newHostPath) {
        try {
//            CmdSet.suExecutor.exec("chmod 666 /system/etc/hosts");
            // first backup old host file
            CmdSet.suExecutor.exec("mv /system/etc/hosts /system/etc/hosts.bak");
            // then copy new host file to replace it
            CmdSet.suExecutor.exec("cp " + newHostPath + " /system/etc/hosts");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
