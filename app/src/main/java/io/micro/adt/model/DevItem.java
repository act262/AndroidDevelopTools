package io.micro.adt.model;

import android.content.Context;

import io.micro.adt.util.DeveloperKit;

/**
 * DevItem model
 */
public class DevItem {
    public int id;
    public String desc;
    public int icon;
    public boolean activated;

    public static void handle(Context context, DevItem item) {
        boolean activated = item.activated;
        switch (item.id) {
            case 0x01:
                DeveloperKit.setAdbEnable(context, activated);
                break;
            case 0x02:
                DeveloperKit.keepScreenOn(context, activated);
                break;
            case 0x03:
                DeveloperKit.setDebugLayout(activated);
                break;
            case 0x04:
                DeveloperKit.setDebugOverdraw(activated);
                break;
            case 0x05:
                DeveloperKit.setProfile(activated);
                break;
            case 0x06:
                DeveloperKit.setStrictMode(activated);
                break;
            case 0x07:
                DeveloperKit.setDestroyActivities(activated);
                break;
            case 0x08:
                DeveloperKit.openDevelopmentSettings(context);
                break;
            default:
                // no-op
        }
    }
}
