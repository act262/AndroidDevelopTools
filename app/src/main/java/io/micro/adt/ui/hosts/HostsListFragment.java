package io.micro.adt.ui.hosts;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.micro.adt.R;
import io.micro.adt.model.HostsListItem;
import io.micro.adt.util.HostsUtil;

/**
 * Hosts List
 *
 * @author act262@gmail.com
 */
public class HostsListFragment extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private ArrayList<HostsListItem> mDataSet;
    private HostsAdapter mAdapter;

    // Current selected item position
    private int selectedIndex = 0;
    private HostsListItem systemHosts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataSet = new ArrayList<>(2);

        // TODO: 2017/3/1 mock data
        systemHosts = new HostsListItem();
        systemHosts.title = "system host";
        systemHosts.content = HostsUtil.readSystemHosts();

        for (int i = 0; i < 20; i++) {
            HostsListItem item = new HostsListItem();
            item.title = "host_title_" + i;
            item.checked = i % 2 == 0;
            item.content = "127.0.0.1 www.google.com\n127.0.0.1 localhost\n";
            mDataSet.add(item);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams")
        View inflate = inflater.inflate(R.layout.fragment_hosts_list, null);
        recyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), OrientationHelper.VERTICAL));
        mAdapter = new HostsAdapter();
        recyclerView.setAdapter(mAdapter);

        inflate.findViewById(R.id.tv_system).setOnClickListener(this);
        inflate.findViewById(R.id.btn_add).setOnClickListener(this);
        return inflate;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_system:
                switchItem(systemHosts);
                break;
            case R.id.btn_add:
                addNewItem();
                break;
        }
    }

    // 添加新的条目
    private void addNewItem() {
        NewHostsItemDialog.newInstance().show(getFragmentManager(), "newItem");
    }

    private class HostsAdapter extends RecyclerView.Adapter {

        @Override
        public int getItemCount() {
            return mDataSet.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CustomHostsViewHolder(parent.getContext());
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
            if (payloads.isEmpty()) {
                onBindViewHolder(holder, position);
            } else {
                holder.itemView.setSelected(position == selectedIndex);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((CustomHostsViewHolder) holder).onBindViewHolder(mDataSet.get(position));
            holder.itemView.setTag(position);
        }
    }


    private class CustomHostsViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        Switch aSwitch;

        @SuppressLint("InflateParams")
        CustomHostsViewHolder(Context context) {
            super(LayoutInflater.from(context).inflate(R.layout.item_hosts_list, null));

            title = (TextView) itemView.findViewById(R.id.tv_title);
            aSwitch = (Switch) itemView.findViewById(R.id.switch_);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickItem(itemView);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    popupMenu(itemView);
                    return true;
                }
            });
        }

        void onBindViewHolder(HostsListItem item) {
            title.setText(item.title);
            aSwitch.setChecked(item.checked);
        }
    }

    private void onClickItem(View itemView) {
        final int position = (int) itemView.getTag();
        selectItem(position);
    }

    private void popupMenu(View itemView) {
        final int position = (int) itemView.getTag();

        PopupMenu popupMenu = new PopupMenu(getActivity(), itemView);
        popupMenu.inflate(R.menu.menu_hosts_operation);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete:
                        deleteItem(position);
                        break;
                    case R.id.edit:

                        break;
                    default:
                        // no-op
                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void selectItem(int position) {
        // Update selected item state
        selectedIndex = position;
        mAdapter.notifyItemRangeChanged(0, mAdapter.getItemCount(), true);

        switchItem(mDataSet.get(position));
    }

    private void switchItem(HostsListItem item) {
        HostsContentFragment contentFragment = (HostsContentFragment) getFragmentManager().findFragmentByTag("content");
        if (contentFragment != null) {
            contentFragment.setHosts(item);
        }
    }

    private void deleteItem(int position) {
        mDataSet.remove(position);
        mAdapter.notifyItemRemoved(position);

        // Handle IndexOutOfBoundsException
        if (position == selectedIndex) {
            selectedIndex = 0;
            mAdapter.notifyItemRangeChanged(position, mDataSet.size() - position, true);
        } else {
            mAdapter.notifyItemRangeChanged(position, mDataSet.size() - position);
        }
    }
}
