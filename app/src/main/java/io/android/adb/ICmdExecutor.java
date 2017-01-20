package io.android.adb;

/**
 * @author act262@gmail.com
 */
public interface ICmdExecutor {

    void exec(ICmd cmd);

    String getPackage();
}
