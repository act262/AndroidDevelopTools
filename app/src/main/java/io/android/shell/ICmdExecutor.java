package io.android.shell;

import java.io.IOException;

/**
 * Shell executor
 *
 * @author act262@gmail.com
 */
public interface ICmdExecutor {

    /**
     * 执行的命令行
     *
     * @throws IOException
     */
    void exec(String... cmd) throws IOException;
}
