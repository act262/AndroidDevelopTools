package io.micro.adt.view;

import android.content.Context;
import android.graphics.ColorFilter;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import io.micro.adt.R;
import io.micro.adt.model.DevItem;
import io.micro.adt.util.ColorUtil;

/**
 * Developer Options Item Layout
 */
public class DevItemView extends FrameLayout implements View.OnClickListener {

    private ImageView icon;
    private TextView text;

    protected DevItem item;

    private static final ColorFilter ENABLED_FILTER = ColorUtil.getEnabledFilter1();
    private static final ColorFilter DISABLED_FILTER = ColorUtil.getDisabledFilter1();

    public DevItemView(Context context) {
        super(context);
        View.inflate(context, R.layout.item_dev, this);
        icon = findViewById(android.R.id.icon);
        text = findViewById(android.R.id.text1);

        this.setOnClickListener(this);
    }

    public void bind(DevItem item) {
        this.item = item;
        boolean activated = item.isActivated(getContext());
        int iconRes = item.icon == 0 ? R.mipmap.ic_launcher : item.icon;
        icon.setImageResource(iconRes);
        int descRes = item.desc == 0 ? R.string.app_name : item.desc;
        text.setText(descRes);
        this.setActivated(activated);

        // 这里控制不同状态下的图标颜色变化
        icon.setColorFilter(activated ? ENABLED_FILTER : DISABLED_FILTER);
    }

    @Override
    public void onClick(View v) {
        boolean activated = item.isActivated(getContext());
        item.setActivated(getContext(), !activated);

        activated = item.isActivated(getContext());
        this.setActivated(activated);
        icon.setColorFilter(activated ? ENABLED_FILTER : DISABLED_FILTER);
    }

}
