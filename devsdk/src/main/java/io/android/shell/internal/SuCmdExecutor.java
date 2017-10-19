package io.android.shell.internal;

/**
 * 使用root权限执行shell
 *
 * @author act262@gmail.com
 */
public class SuCmdExecutor extends AbstractCmdExecutor {

    private final static String COMMAND_SU = "su";

    @Override
    protected String execEnv() {
        return COMMAND_SU;
    }

}
