package io.android.adb.cmd;

import io.android.adb.ICmd;
import io.android.adb.ICmdExecutor;

/**
 * 重启应用命令
 *
 * @author act262@gmail.com
 */
public class RestartCommand implements ICmd {

    @Override
    public String cmd(ICmdExecutor executor) {
        return "am start " + executor.getPackage() + " -a android.intent.action.MAIN -c android.intent.category.LAUNCHER -S";
    }
}
