package io.android.adb;

import java.io.IOException;

/**
 * 命令执行
 *
 * @author act262@gmail.com
 */
public class CmdExecutor implements ICmdExecutor {

    public CmdExecutor(String pkg) {
        this.mPackage = pkg;
    }

    @Override
    public void exec(ICmd cmd) {
        try {
            Runtime.getRuntime().exec(cmd.cmd(this));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getPackage() {
        return mPackage;
    }

    private String mPackage;
}
