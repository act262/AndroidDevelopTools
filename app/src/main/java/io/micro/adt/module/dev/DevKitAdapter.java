package io.micro.adt.module.dev;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.micro.adt.module.dev.model.DevItem;
import io.micro.adt.view.DevItemView;

/**
 * Adapter
 *
 * @author act262@gmail.com
 */
class DevKitAdapter extends RecyclerView.Adapter<DevKitAdapter.ItemViewHolder> {

    private List<DevItem> mDataSet;

    DevKitAdapter(List<DevItem> dataSet) {
        this.mDataSet = dataSet;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(new DevItemView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.bindView(mDataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        ItemViewHolder(View itemView) {
            super(itemView);
        }

        void bindView(DevItem devItem) {
            ((DevItemView) itemView).bind(devItem);
        }
    }
}
