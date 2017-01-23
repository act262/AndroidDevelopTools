package io.micro.adt.model;

import android.content.Context;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

import io.micro.adt.R;
import io.micro.adt.ui.DevItemView;
import io.micro.adt.util.DeveloperKit;

/**
 * Developer Factory
 *
 * @author act262@gmail.com
 */
public class DevOptFactory {

    public static List<DevItemView> createAll(Context context) {
        ArrayList<DevItemView> list = new ArrayList<>();
        list.add(new DevItemView(context, new DebugUsb()));
        list.add(new DevItemView(context, new DebugKeepScreen()));
        list.add(new DevItemView(context, new DebugLayout()));
        list.add(new DevItemView(context, new DebugOverdraw()));
        list.add(new DevItemView(context, new DebugProfile()));
        list.add(new DevItemView(context, new DebugStrict()));
        // target api > 19
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            list.add(new DevItemView(context, new DebugDestroyActivity()));
        }
        return list;
    }

    private static class DebugUsb extends DevItem {

        DebugUsb() {
            icon = R.drawable.ic_developer_usb;
            desc = R.string.usb_debug;
        }

        @Override
        public boolean isActivated(Context context) {
            return DeveloperKit.isAdbEnabled(context);
        }

        @Override
        public void setActivated(Context context, boolean activated) {
            DeveloperKit.setAdbEnabled(activated);
        }
    }

    private static class DebugKeepScreen extends DevItem {

        DebugKeepScreen() {
            icon = R.drawable.ic_developer_keep_screen_on;
            desc = R.string.keep_screen;
        }

        @Override
        public boolean isActivated(Context context) {
            return DeveloperKit.isKeepScreenOn(context);
        }

        @Override
        public void setActivated(Context context, boolean activated) {
            DeveloperKit.keepScreenOn(activated);
        }
    }

    private static class DebugLayout extends DevItem {
        DebugLayout() {
            icon = R.drawable.ic_developer_debug_layout;
            desc = R.string.debug_layout;
        }

        @Override
        public boolean isActivated(Context context) {
            return DeveloperKit.debugLayout();
        }

        @Override
        public void setActivated(Context context, boolean activated) {
            DeveloperKit.setDebugLayout(activated);
        }
    }

    private static class DebugOverdraw extends DevItem {
        DebugOverdraw() {
            icon = R.drawable.ic_developer_overdraw;
            desc = R.string.debug_gpu_overdraw;
        }

        @Override
        public boolean isActivated(Context context) {
            return DeveloperKit.debugOverdraw();
        }

        @Override
        public void setActivated(Context context, boolean activated) {
            DeveloperKit.setDebugOverdraw(activated);
        }
    }

    private static class DebugProfile extends DevItem {
        DebugProfile() {
            icon = R.drawable.ic_developer_gpu_profile;
            desc = R.string.debug_gpu_profile;
        }

        @Override
        public boolean isActivated(Context context) {
            return DeveloperKit.debugProfile();
        }

        @Override
        public void setActivated(Context context, boolean activated) {
            DeveloperKit.setProfile(activated);
        }
    }

    private static class DebugStrict extends DevItem {
        DebugStrict() {
            icon = R.drawable.ic_developer_strict;
            desc = R.string.debug_stric_mode;
        }

        @Override
        public boolean isActivated(Context context) {
            return DeveloperKit.isStrictMode();
        }

        @Override
        public void setActivated(Context context, boolean activated) {
            DeveloperKit.setStrictMode(activated);
        }
    }

    private static class DebugDestroyActivity extends DevItem {
        DebugDestroyActivity() {
            icon = R.drawable.ic_developer_destroy_activity;
            desc = R.string.debug_destroy_activity;
        }

        @Override
        public boolean isActivated(Context context) {
            return DeveloperKit.isDestroyActivities();
        }

        @Override
        public void setActivated(Context context, boolean activated) {
            DeveloperKit.setDestroyActivities(activated);
        }
    }
}
