package io.android.shell.internal;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

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
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        // exec shell with root
        os.writeBytes(cmd[0] + " \n");
        os.writeBytes("exit\n");
        os.flush();

        String line;
        // print execute result
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        // print error
        while ((line = errorReader.readLine()) != null) {
            System.out.println(line);
        }
    }
}
