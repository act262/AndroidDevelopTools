package io.micro.adt.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import java.util.List;

import io.micro.adt.R;
import io.micro.adt.model.DevOptFactory;
import io.micro.adt.util.DeveloperKit;
import io.micro.adt.view.DevItemView;

/**
 * 开发者选项页面
 */
public class DeveloperKitFragment extends BaseFragment {

    private GridLayout mGridLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_developer_kit, container, false);
        mGridLayout = findView(R.id.gridLayout);
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

        for (DevItemView itemView : itemViews) {
            mGridLayout.addView(itemView);
        }
    }
}
