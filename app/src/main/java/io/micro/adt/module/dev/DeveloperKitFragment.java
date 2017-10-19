package io.micro.adt.module.dev;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.micro.adt.R;
import io.micro.adt.model.DevItem;
import io.micro.adt.model.DevOptFactory;
import io.micro.adt.ui.BaseFragment;
import io.micro.adt.util.DeveloperKit;

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

        RecyclerView recyclerView = findView(R.id.rv_developer_kit);
        List<DevItem> devItems = DevOptFactory.createAll();
        DevKitAdapter adapter = new DevKitAdapter(devItems);
        recyclerView.setAdapter(adapter);
    }
}
