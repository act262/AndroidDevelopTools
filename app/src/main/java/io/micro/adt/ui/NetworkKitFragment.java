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
import android.widget.Switch;

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
        final Switch proxyButton = findView(R.id.proxySwitch);
        final EditText hostText = findView(R.id.et_host);
        final EditText portText = findView(R.id.et_port);

        hostText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String host = hostText.getText().toString().trim();
                String port = portText.getText().toString().trim();

            }
        });

        proxyButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String host = hostText.getText().toString().trim();
                String port = portText.getText().toString().trim();

                // 开启状态必须指定HOST和port
                if (isChecked) {
                    if (TextUtils.isEmpty(host)) {
                        hostText.setError("Host can't empty !");
                        proxyButton.setChecked(false);
                        preferences.edit().putBoolean(KEY_NETWORK_PROXY_CHECK, false).apply();
                        return;
                    }
                    if (TextUtils.isEmpty(port)) {
                        portText.setError("Port can't empty !");
                        proxyButton.setChecked(false);
                        preferences.edit().putBoolean(KEY_NETWORK_PROXY_CHECK, false).apply();
                        return;
                    }
                    preferences.edit()
                            .putString(KEY_NETWORK_PROXY_HOST, host)
                            .putString(KEY_NETWORK_PROXY_PORT, port)
                            .apply();
                }

                preferences.edit().putBoolean(KEY_NETWORK_PROXY_CHECK, isChecked).apply();
                // TODO: 2017/1/16 校验IP,端口
                NetworkKit.setProxy(getActivity(), isChecked, host, DFormatter.parseInt(port));
            }
        });

        String host = preferences.getString(KEY_NETWORK_PROXY_HOST, "");
        String port = preferences.getString(KEY_NETWORK_PROXY_PORT, "8888");

        String proxyHost = NetworkKit.getProxyHost(getActivity());
        if (TextUtils.isEmpty(proxyHost)) {
            hostText.setText(host);
        } else {
            hostText.setText(proxyHost);
        }
        String proxyPort = NetworkKit.getProxyPort(getActivity());
        if (TextUtils.isEmpty(proxyPort)) {
            portText.setText(port);
        } else {
            portText.setText(proxyPort);
        }

        boolean checked = preferences.getBoolean(KEY_NETWORK_PROXY_CHECK, false);
        proxyButton.setChecked(checked);
        return mRootView;
    }

    public static final String KEY_NETWORK_PROXY_HOST = "proxy_host";
    public static final String KEY_NETWORK_PROXY_PORT = "proxy_port";
    public static final String KEY_NETWORK_PROXY_CHECK = "proxy_check";
}
