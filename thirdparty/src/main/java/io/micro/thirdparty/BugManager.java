package io.micro.thirdparty;

import android.content.Context;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * Bug/Crash report
 *
 * @author act262@gmail.com
 */
public class BugManager {

    public void init(Context context) {
        CrashReport.setIsDevelopmentDevice(context, BuildConfig.DEBUG);
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setEnableUserInfo(true);

        CrashReport.initCrashReport(context, "084c5528d7", BuildConfig.DEBUG, strategy);
    }

}
