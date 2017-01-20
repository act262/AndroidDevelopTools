package io.android.adb.cmd;

import io.android.adb.ICmd;
import io.android.adb.ICmdExecutor;

/**
 * 清空数据命令
 *
 * @author act262@gmail.com
 */
public class ClearCommand implements ICmd {

    @Override
    public String cmd(ICmdExecutor executor) {
        return "pm clear " + executor.getPackage();
    }
}
