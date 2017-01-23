package io.micro.adt.model;

import android.content.Context;

/**
 * DevItem model
 */
public class DevItem {

    public int id;
    public int desc;
    public int icon;

    public boolean activated;

    public boolean isActivated(Context context) {
        return false;
    }

    public void setActivated(Context context, boolean activated) {
        this.activated = activated;
    }

}
