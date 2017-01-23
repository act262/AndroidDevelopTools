package io.micro.adt.model;

import android.content.Context;

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
        list.add(new DevItemView(context, new UsbDevItem()));
        list.add(new DevItemView(context, new ScreenDevItem()));
        list.add(new DevItemView(context, new DebugLayoutDevItem()));
        list.add(new DevItemView(context, new DebugOverdraw()));
        list.add(new DevItemView(context, new DebugProfile()));
        return list;
    }

    private static class UsbDevItem extends DevItem {

        UsbDevItem() {
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

    private static class ScreenDevItem extends DevItem {

        ScreenDevItem() {
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

    private static class DebugLayoutDevItem extends DevItem {
        DebugLayoutDevItem() {
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
}
