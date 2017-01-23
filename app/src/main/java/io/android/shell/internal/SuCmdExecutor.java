package io.android.shell.internal;

import java.io.DataOutputStream;
import java.io.IOException;

import io.android.shell.ICmdExecutor;

/**
 * 使用root权限执行shell
 *
 * @author act262@gmail.com
 */
public class SuCmdExecutor implements ICmdExecutor {

    @Override
    public void exec(String... cmd) throws IOException {
        Process process = Runtime.getRuntime().exec("su");
        DataOutputStream os = new DataOutputStream(process.getOutputStream());
        // exec shell with root
        os.writeBytes(cmd[0] + " \n");
        os.writeBytes("exit\n");
        os.flush();
    }
}
