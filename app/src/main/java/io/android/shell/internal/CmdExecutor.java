package io.android.shell.internal;

import java.io.IOException;

import io.android.shell.ICmdExecutor;

/**
 * 普通命令执行shell,非root权限
 *
 * @author act262@gmail.com
 */
public class CmdExecutor implements ICmdExecutor {

    @Override
    public void exec(String... cmd) throws IOException {
        Runtime.getRuntime().exec(cmd[0]);
    }

}
