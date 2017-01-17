package io.micro.adt.util;

import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;

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

    public static ColorFilter getEnabledFilter1() {
        return new LightingColorFilter(0xfffffff, 0x0000000);
    }

    public static ColorFilter getDisabledFilter1() {
        return new LightingColorFilter(0xffffff, 0x7f7f7f);
    }
}
