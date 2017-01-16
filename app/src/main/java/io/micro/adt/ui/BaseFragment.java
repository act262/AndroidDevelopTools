package io.micro.adt.ui;

import android.app.Fragment;
import android.view.View;

/**
 * Base Fragment
 */
public class BaseFragment extends Fragment {

    protected View mRootView;

    protected <T> T findView(int resId) {
        //noinspection unchecked
        return (T) mRootView.findViewById(resId);
    }
}
