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

    protected <T> T findView(int resId) {
        //noinspection unchecked
        return (T) mRootView.findViewById(resId);
    }
}
