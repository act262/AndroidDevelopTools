package io.micro.adt.util;

import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;

/**
 * Color utility
 */
public class ColorUtil {

    public static ColorFilter getEnabledFilter() {
        ColorMatrix enabledMatrix = new ColorMatrix();
        enabledMatrix.setSaturation(1f);
        return new ColorMatrixColorFilter(enabledMatrix);
    }

    public static ColorFilter getDisabledFilter() {
        ColorMatrix disabledMatrix = new ColorMatrix();
        disabledMatrix.setSaturation(0.2f);
        return new ColorMatrixColorFilter(disabledMatrix);
    }
}
