package io.android.shell.internal;

/**
 * 普通命令执行shell,非root权限
 *
 * @author act262@gmail.com
 */
public class CmdExecutor extends AbstractCmdExecutor {

    @Override
    protected String execEnv() {
        return COMMAND_SH;
    }

}
