package io.android.shell.cmd;

import java.io.IOException;

import io.android.shell.CmdResult;
import io.android.shell.ICmdExecutor;
import io.android.shell.ResultCallback;
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

    /**
     * 以普通权限执行命令
     */
    public static void exec(String... cmd) throws IOException {
        executor.exec(cmd);
    }

    /**
     * 以root权限执行命令
     */
    public static void execSu(String... cmd) throws IOException {
        suExecutor.exec(cmd);
    }

    /**
     * 异步执行命令
     */
    public static void execAsync(String[] cmd, ResultCallback<CmdResult> callback) {
        executor.execAsync(cmd, callback);
    }

    /**
     * 异步执行命令
     */
    public static void execSuAsync(String[] cmd, ResultCallback<CmdResult> callback) {
        suExecutor.execAsync(cmd, callback);
    }
}
