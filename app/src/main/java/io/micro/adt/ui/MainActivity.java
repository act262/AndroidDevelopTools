package io.micro.adt.ui;

import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.micro.adt.Developer;
import io.micro.adt.R;
import io.micro.adt.model.DevItem;
import io.micro.adt.util.ColorUtil;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, Toolbar.OnMenuItemClickListener {

    private GridView gridView;
    private DevAdapter mAdapter;

    private List<DevItem> itemList;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setOnMenuItemClickListener(this);

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

        gridView = (GridView) findViewById(R.id.listView);
        mAdapter = new DevAdapter();
        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                Developer.openDevelopmentSettings(MainActivity.this);
                break;
            default:
                // no-op
        }
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DevItem item = mAdapter.getItem(position);
        item.activated = !item.activated;
        DevItem.handle(getApplicationContext(), item);
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
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(parent.getContext(), R.layout.item_dev, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
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
                        ColorUtil.getEnabledFilter() : ColorUtil.getDisabledFilter());
                text.setText(item.desc);
                text.setActivated(item.activated);
            }
        }
    }

}
