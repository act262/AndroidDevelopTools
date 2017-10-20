package io.micro.adt.module.dev;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.util.Collections;
import java.util.List;

import io.micro.adt.R;
import io.micro.adt.module.dev.model.DevItem;
import io.micro.adt.module.dev.model.DevOptFactory;
import io.micro.adt.ui.BaseFragment;
import io.micro.dev.sdk.DeveloperKit;

/**
 * 开发者选项页面
 *
 * @author act262@gmail.com
 */
public class DeveloperKitFragment extends BaseFragment {

    @Override
    protected int layoutResId() {
        return R.layout.fragment_developer_kit;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findView(R.id.btn_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeveloperKit.openDevelopmentSettings(getActivity());
            }
        });

        RecyclerView recyclerView = findView(R.id.rv_developer_kit);
        final List<DevItem> devItems = DevOptFactory.createAll();
        DevKitAdapter adapter = new DevKitAdapter(devItems);
        recyclerView.setAdapter(adapter);

        // 实现拖动排序效果
        int dragDirs = ItemTouchHelper.LEFT | ItemTouchHelper.UP | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN;
        int swipeDirs = 0;
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int targetPosition = target.getAdapterPosition();
                Collections.swap(devItems, fromPosition, targetPosition);
                recyclerView.getAdapter().notifyItemMoved(fromPosition, targetPosition);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // no-op
            }
        }).attachToRecyclerView(recyclerView);
    }
}
