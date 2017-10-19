package io.android.shell;

/**
 * Command execute exitValue.
 *
 * @author act262@gmail.com
 */
public class CmdResult {
    public int exitValue;
    public String successResult;
    public String errorResult;

    @Override
    public String toString() {
        return "CmdResult{" +
                "exitValue=" + exitValue +
                ", successResult='" + successResult + '\'' +
                ", errorResult='" + errorResult + '\'' +
                '}';
    }
}
