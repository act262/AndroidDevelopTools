package io.micro.adt.ui;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Base Fragment
 *
 * @author act262@gmail.com
 */
public class BaseFragment extends Fragment {

    // TODO: 2017/10/19 分离开存储模块
    protected SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(layoutResId(), container, false);
    }

    @LayoutRes
    protected int layoutResId() {
        return 0;
    }

    protected <T extends View> T findView(int resId) {
        //noinspection unchecked,ConstantConditions
        return (T) getView().findViewById(resId);
    }

    protected void save(String key, String value) {
        preferences.edit()
                .putString(key, value)
                .apply();
    }

    protected String get(String key) {
        return preferences.getString(key, "");
    }
}
