package io.micro.adt.module.dev.model;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import io.micro.adt.R;

/**
 * DevItem Model
 * <p>
 * 描述文字,描述图标,激活状态,是否需要root权限,SDK版本号
 * </p>
 *
 * @author act262@gmail.com
 */
public class DevItem {

    public int id;

    @StringRes
    public int desc = R.string.app_name;

    @DrawableRes
    public int icon = R.mipmap.ic_launcher;

    /**
     * 激活状态
     */
    public boolean activated;

    /**
     * 是否需要root权限
     */
    public boolean needRoot;

    /**
     * SDK 版本号
     */
    public int api;

    public boolean isActivated(Context context) {
        return activated;
    }

    public void setActivated(Context context, boolean activated) {
        this.activated = activated;
    }

}
