package io.micro.adt.app;

import android.app.Application;

import io.micro.thirdparty.BugManager;

/**
 * Application
 *
 * @author act262@gmail.com
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initReport();
    }

    private void initReport() {
        new BugManager().init(this);
    }
}
