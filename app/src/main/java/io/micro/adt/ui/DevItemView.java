package io.micro.adt.ui;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import io.micro.adt.R;
import io.micro.adt.model.DevItem;

/**
 * Developer Options Item Layout
 */
public class DevItemView extends FrameLayout implements View.OnClickListener {

    private ImageView icon;
    private TextView text;

    protected DevItem item;

    public DevItemView(Context context) {
        super(context);
        View.inflate(context, R.layout.item_dev, this);
        icon = (ImageView) findViewById(android.R.id.icon);
        text = (TextView) findViewById(android.R.id.text1);
        setOnClickListener(this);
        icon.setDuplicateParentStateEnabled(true);
        text.setDuplicateParentStateEnabled(true);
    }

    public DevItemView(Context context, DevItem devItem) {
        this(context);
        set(devItem);
    }

    public void set(DevItem item) {
        this.item = item;
        boolean activated = item.isActivated(getContext());

        icon.setImageResource(item.icon);
//        icon.setColorFilter(activated ?
//                ColorUtil.getEnabledFilter1() : ColorUtil.getDisabledFilter1());
        icon.setActivated(activated);
        text.setText(item.desc);
        text.setActivated(activated);
    }

    @Override
    public void onClick(View v) {
        boolean activated = item.isActivated(getContext());
        this.setActivated(!activated);
        item.setActivated(getContext(), !activated);
    }

}
