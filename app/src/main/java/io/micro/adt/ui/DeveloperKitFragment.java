package io.micro.adt.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import io.micro.adt.R;
import io.micro.adt.model.DevOptFactory;
import io.micro.adt.util.DeveloperKit;
import io.micro.adt.view.DevItemView;

/**
 * 开发者选项页面
 */
public class DeveloperKitFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_developer_kit, container, false);
        View more = findView(R.id.btn_more);

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeveloperKit.openDevelopmentSettings(getActivity());
            }
        });
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<DevItemView> itemViews = DevOptFactory.createAll(getActivity());

        // 分为4列排布
        final int COLUMN = 4;
        // 可以展示的行数
        final int ROW = (itemViews.size() - 1) / COLUMN + 1;

        LinearLayout container = (LinearLayout) this.mRootView;

        for (int row = 0; row < ROW; row++) {
            LinearLayout rowLayout = new LinearLayout(getActivity());
            rowLayout.setWeightSum(COLUMN);

            for (int column = 0; column < COLUMN; column++) {
                int index = COLUMN * row + column;
                if (index >= itemViews.size()) {
                    break;
                }
                rowLayout.addView(itemViews.get(index), new LinearLayout.LayoutParams(0, -1, 1));
            }

            container.addView(rowLayout);
        }
    }
}
