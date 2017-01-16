package io.micro.adt.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ToggleButton;

import io.micro.adt.R;
import io.micro.adt.util.DFormatter;
import io.micro.adt.util.NetworkKit;
import io.micro.adt.util.text.SimpleTextWatcher;

/**
 * 网络设置页
 */
public class NetworkKitFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_network_kit, container, false);
        ToggleButton proxyButton = findView(R.id.proxySwitch);
        final EditText hostText = findView(R.id.et_host);
        final EditText portText = findView(R.id.et_port);

        hostText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO: 2017/1/16 状态变化设置
            }
        });
        proxyButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String host = hostText.getText().toString().trim();
                String port = portText.getText().toString().trim();
                if (isChecked) {
                    if (TextUtils.isEmpty(host)) {
                        hostText.setError("Host can't empty");
                        return;
                    }
                    if (TextUtils.isEmpty(port)) {
                        portText.setError("Port can't empty");
                        return;
                    }
                }
                // TODO: 2017/1/16 校验IP,端口
                NetworkKit.setProxy(getActivity(), isChecked, host, DFormatter.parseInt(port));
            }
        });
        return mRootView;
    }
}
