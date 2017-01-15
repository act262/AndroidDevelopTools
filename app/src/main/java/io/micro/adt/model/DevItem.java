package io.micro.adt.model;

import io.micro.adt.Developer;

/**
 * DevItem model
 */
public class DevItem {
    public int id;
    public String desc;
    public int icon;
    public boolean activated;

    public static void handle(DevItem item) {
        boolean activated = item.activated;
        switch (item.id) {
            case 0:
                Developer.setDebugLayout(activated);
                break;
            case 1:
                Developer.setDebugOverdraw(activated);
                break;
            case 2:
                Developer.setProfile(activated);
                break;
            default:
                // no-op
        }
    }
}
