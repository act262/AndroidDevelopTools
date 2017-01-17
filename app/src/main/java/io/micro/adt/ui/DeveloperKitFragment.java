package io.micro.adt.ui;

import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.micro.adt.R;
import io.micro.adt.model.DevItem;
import io.micro.adt.util.ColorUtil;

/**
 * 开发者选项页面
 */
public class DeveloperKitFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private GridView gridView;
    private DevAdapter mAdapter;

    private List<DevItem> itemList;

    // TODO: 2017/1/15 0015 不用记录状态 ，改为实时的获取状态
    private SharedPreferences preferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        itemList = new ArrayList<>();
        int[] idArray = getResources().getIntArray(R.array.dev_id);
        String[] descArray = getResources().getStringArray(R.array.dev_desc);
        TypedArray typedArray = getResources().obtainTypedArray(R.array.dev_icon);

        for (int i = 0; i < idArray.length; i++) {
            DevItem devItem = new DevItem();
            devItem.id = idArray[i];
            devItem.desc = descArray[i];
            devItem.icon = typedArray.getResourceId(i, 0);
            devItem.activated = getDevItemActivated(devItem);
            itemList.add(devItem);
        }
        typedArray.recycle();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_developer_kit, container, false);
        gridView = (GridView) inflate.findViewById(R.id.listView);
        return inflate;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new DevAdapter();
        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DevItem item = mAdapter.getItem(position);
        item.activated = !item.activated;
        DevItem.handle(getActivity(), item);
        mAdapter.notifyDataSetChanged();
        setDevItemActivated(item);
    }

    private boolean getDevItemActivated(DevItem item) {
        return preferences.getBoolean(generateKey(item), false);
    }

    private void setDevItemActivated(DevItem item) {
        preferences.edit()
                .putBoolean(generateKey(item), item.activated)
                .apply();
    }

    private String generateKey(DevItem item) {
        return "activated_" + item.id;
    }

    private class DevAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return itemList.size();
        }

        @Override
        public DevItem getItem(int position) {
            return itemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).id;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            DevAdapter.ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(parent.getContext(), R.layout.item_dev, null);
                holder = new DevAdapter.ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (DevAdapter.ViewHolder) convertView.getTag();
            }
            holder.set(getItem(position));
            return convertView;
        }

        private class ViewHolder {
            ImageView icon;
            TextView text;

            ViewHolder(View itemView) {
                icon = (ImageView) itemView.findViewById(android.R.id.icon);
                text = (TextView) itemView.findViewById(android.R.id.text1);
            }

            void set(DevItem item) {
                icon.setImageResource(item.icon);
                icon.setColorFilter(item.activated ?
                        ColorUtil.getEnabledFilter1() : ColorUtil.getDisabledFilter1());
                text.setText(item.desc);
                text.setActivated(item.activated);
            }
        }
    }
}
