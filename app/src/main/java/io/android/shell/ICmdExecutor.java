package io.android.shell;

import java.io.IOException;

/**
 * Shell executor
 *
 * @author act262@gmail.com
 */
public interface ICmdExecutor {

    /**
     * 同步执行的命令行，会阻塞当前调用线程。
     *
     * @return exitValue = 0 正常执行结束
     * @throws IOException 执行异常
     */
    CmdResult exec(String... commands) throws IOException;

    /**
     * 异步执行命令
     *
     * @param commands 待执行的命令
     * @param callback 命令执行结果回调
     */
    void execAsync(String[] commands, ResultCallback<CmdResult> callback);
}
