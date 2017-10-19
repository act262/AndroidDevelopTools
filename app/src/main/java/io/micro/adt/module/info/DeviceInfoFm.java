package io.micro.adt.module.info;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.Arrays;

import io.micro.adt.R;
import io.micro.adt.ui.BaseFragment;

/**
 * Some Device information
 *
 * @author act262@gmail.com
 */
public class DeviceInfoFm extends BaseFragment {

    private TextView infoText;

    @Override
    protected int layoutResId() {
        return R.layout.fragment_information;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        infoText = findView(R.id.tv_info_brief);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
        infoText.setText(builder);
    }

    public static String getProp(String key) {
        return System.getProperty(key, "");
    }
}
