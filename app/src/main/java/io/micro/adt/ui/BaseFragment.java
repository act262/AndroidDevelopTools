package io.micro.adt.ui;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

/**
 * Base Fragment
 */
public class BaseFragment extends Fragment {

    protected SharedPreferences preferences;

    protected View mRootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    protected <T extends View> T findView(int resId) {
        //noinspection unchecked
        return (T) mRootView.findViewById(resId);
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
