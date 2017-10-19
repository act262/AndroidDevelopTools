package io.android.shell;

import java.io.DataOutputStream;

/**
 * Root Utility
 */
public class RootHelper {

    /**
     * 测试是否有root权限,会阻塞调用线程,所以需要在异步线程中调用
     *
     * @return 返回0表示获得root权限;其他没有root权限
     */
    public static int root() {
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream outputStream = new DataOutputStream(process.getOutputStream());
            outputStream.writeBytes("exit\n");
            outputStream.flush();
            process.waitFor();
            return process.exitValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

}
