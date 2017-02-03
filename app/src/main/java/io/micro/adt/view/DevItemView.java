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

    public DevItemView(Context context) {
        this(context, new DevItem());
    }

    public DevItemView(Context context, DevItem devItem) {
        super(context);
        View.inflate(context, R.layout.item_dev, this);
        icon = (ImageView) findViewById(android.R.id.icon);
        text = (TextView) findViewById(android.R.id.text1);

        this.setOnClickListener(this);
        set(devItem);
    }

    private void set(DevItem item) {
        this.item = item;
        boolean activated = item.isActivated(getContext());

        icon.setImageResource(item.icon);
        text.setText(item.desc);
        this.setActivated(activated);
    }

    @Override
    public void onClick(View v) {
        boolean activated = item.isActivated(getContext());
        this.setActivated(!activated);
        item.setActivated(getContext(), !activated);
    }

    @Override
    public void dispatchSetActivated(boolean activated) {
        super.dispatchSetActivated(activated);
        // 这里控制不同状态下的图标颜色变化
        ColorFilter colorFilter = activated ?
                ColorUtil.getEnabledFilter1() : ColorUtil.getDisabledFilter1();
        icon.setColorFilter(colorFilter);
    }
}