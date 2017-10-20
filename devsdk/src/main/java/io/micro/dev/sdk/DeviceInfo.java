package io.micro.dev.sdk;

import android.os.Build;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 *
 */
public class DeviceInfo {

    public static String dump() {
        StringBuilder builder = new StringBuilder();
        builder.append(Build.VERSION.SDK_INT);
        builder.append("\n");
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                builder.append(field.getName());
                builder.append(":");
                Object value = field.get(null);
                if (value instanceof String[]) {
                    builder.append(Arrays.asList((String[]) value));
                } else {
                    builder.append(value);
                }
                builder.append("\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return builder.toString();
    }
}
