package io.micro.adt.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import io.android.adb.CmdSet;
import io.micro.adt.R;

/**
 * App 相关功能页面
 *
 * @author act262@gmail.com
 */
public class AppKitFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_app_kit, null);


        final CheckBox restartCheckBox = findView(R.id.cb_restart);
        final CheckBox clearCheckBox = findView(R.id.cb_clear);

        Button restartBtn = findView(R.id.btn_restart);
        restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clearCheckBox.isChecked()) {
                    CmdSet.clearAppData(getActivity().getPackageName());
                }
                if (restartCheckBox.isChecked()) {
                    CmdSet.restartApp(getActivity().getPackageName() + getActivity().getClass().getName());
                }
            }
        });
        return mRootView;
    }
}
