package io.micro.adt.module.debug;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.android.shell.CmdResult;
import io.android.shell.ResultCallback;
import io.android.shell.cmd.CmdSet;
import io.micro.adt.R;
import io.micro.adt.service.TopActivityWatchService;
import io.micro.adt.ui.BaseFragment;
import io.micro.adt.util.AppUtil;
import io.micro.adt.util.NetworkKit;
import io.micro.adt.util.PackageUtil;
import io.micro.adt.util.text.SimpleTextWatcher;

/**
 * App 相关功能页面
 *
 * @author act262@gmail.com
 */
public class AppKitFragment extends BaseFragment {

    private static final String PREF_TARGET_PACKAGE_NAME = "target_package_name";

    private EditText pkgEditText;
    private TextView ipText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_app_kit, null);

        pkgEditText = findView(R.id.et_pkg);
        pkgEditText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String pkgName = pkgEditText.getText().toString();
                save(PREF_TARGET_PACKAGE_NAME, pkgName);
            }
        });

        findView(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pkg = pkgEditText.getText().toString();
                AppUtil.clearAppData(pkg);
            }
        });
        findView(R.id.btn_restart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pkg = pkgEditText.getText().toString();
                PackageUtil.startPkg(getActivity(), pkg);
            }
        });
        findView(R.id.btn_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pkg = pkgEditText.getText().toString();
                AppUtil.clearAppData(pkg);
                PackageUtil.startPkg(getActivity(), pkg);
            }
        });

        SwitchCompat wifiAdbSwitch = findView(R.id.switch_wifi_adb);
        wifiAdbSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ipText.setText(NetworkKit.getIp(getActivity()));
                } else {
                    ipText.setText("");
                }
                AppUtil.wifiAdb(isChecked);
            }
        });

        ipText = findView(R.id.tv_ip);

        findView(R.id.btn_reset_permission).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CmdSet.execSuAsync(new String[]{"pm reset-permissions"}, new ResultCallback<CmdResult>() {
                    @Override
                    public void onReceiveResult(@NonNull CmdResult result) throws Exception {
                        Toast.makeText(getActivity(), "reset permission ok", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        CheckBox topAty = findView(R.id.cb_top_activity);
        topAty.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent service = new Intent(getActivity(), TopActivityWatchService.class);
                if (isChecked) {
                    getActivity().startService(service);
                } else {
                    getActivity().stopService(service);
                }
            }
        });
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String pkgName = get(PREF_TARGET_PACKAGE_NAME);
        if (!TextUtils.isEmpty(pkgName)) {
            pkgEditText.setText(pkgName);
        }
    }

}
