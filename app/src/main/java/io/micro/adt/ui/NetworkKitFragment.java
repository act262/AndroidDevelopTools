package io.micro.adt.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.ToggleButton;

import io.micro.adt.R;
import io.micro.adt.ui.hosts.HostsActivity;
import io.micro.adt.util.DFormatter;
import io.micro.adt.util.NetworkKit;
import io.micro.adt.util.text.SimpleTextWatcher;

/**
 * 网络设置页
 */
public class NetworkKitFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {

    private static final String PORT_NUMBER = "\\d{1,5}";

    private ToggleButton wifiToggleBtn;
    private Switch proxyButton;
    private EditText hostText;
    private EditText portText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_network_kit, container, false);
        proxyButton = findView(R.id.proxySwitch);
        hostText = findView(R.id.et_host);
        portText = findView(R.id.et_port);

        hostText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String host = hostText.getText().toString().trim();
                // 输入无效且聚焦状态
                if (!validHost(host) && hasFocus) {
                    hostErrorTip();
                } else {
                    hostText.setError(null);
                }
            }
        });
        portText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String port = portText.getText().toString().trim();
                if (!validPort(port) && hasFocus) {
                    portErrorTip();
                } else {
                    portText.setError(null);
                }
            }
        });
        hostText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String host = hostText.getText().toString().trim();
                // 输入无效且聚焦状态
                if (!validHost(host)) {
                    hostErrorTip();
                }
            }
        });
        portText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String port = portText.getText().toString().trim();
                if (!validPort(port) && portText.hasFocus()) {
                    portErrorTip();
                }
            }
        });

        proxyButton.setOnCheckedChangeListener(this);

        View moreBtn = findView(R.id.btn_more);
        moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkKit.openWifiSettings(getActivity());
            }
        });

        wifiToggleBtn = findView(R.id.toggleWifi);
        wifiToggleBtn.setChecked(NetworkKit.isWifiEnabled(getActivity()));
        toggleWifi(NetworkKit.isWifiEnabled(getActivity()));
        wifiToggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                toggleWifi(isChecked);
            }
        });

        Button hostsBtn = findView(R.id.btn_hosts);
        hostsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), HostsActivity.class));
            }
        });
        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String proxyHost = NetworkKit.getProxyHost(getActivity());
        if (TextUtils.isEmpty(proxyHost)) {
            proxyHost = preferences.getString(KEY_NETWORK_PROXY_HOST, "");
        }
        hostText.setText(proxyHost);

        String proxyPort = NetworkKit.getProxyPort(getActivity());
        if (TextUtils.isEmpty(proxyPort)) {
            proxyPort = preferences.getString(KEY_NETWORK_PROXY_PORT, "8888");
        }
        portText.setText(proxyPort);

        boolean checked = preferences.getBoolean(KEY_NETWORK_PROXY_CHECK, false);
        proxyButton.setChecked(checked);
    }

    private boolean validHost(String host) {
        return Patterns.IP_ADDRESS.matcher(host).matches();
    }

    private boolean validPort(String port) {
        return port.matches(PORT_NUMBER);
    }

    private void hostErrorTip() {
        hostText.setError("Host invalid");
    }

    private void portErrorTip() {
        portText.setError("Port invalid");
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        String host = hostText.getText().toString().trim();
        String port = portText.getText().toString().trim();

        // 开启状态必须指定HOST和port
        if (isChecked) {
            if (!NetworkKit.isWifiEnabled(getActivity())) {
                proxyButton.setChecked(false);
                preferences.edit().putBoolean(KEY_NETWORK_PROXY_CHECK, false).apply();
                return;
            }
            if (TextUtils.isEmpty(host) || !validHost(host)) {
                hostErrorTip();
                proxyButton.setChecked(false);
                preferences.edit().putBoolean(KEY_NETWORK_PROXY_CHECK, false).apply();
                return;
            }
            if (TextUtils.isEmpty(port) || !validPort(port)) {
                portErrorTip();
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
        NetworkKit.setProxy(getActivity(), isChecked, host, DFormatter.parseInt(port));
    }


    private void toggleWifi(boolean isChecked) {
        proxyButton.setEnabled(isChecked);
        hostText.setEnabled(isChecked);
        portText.setEnabled(isChecked);

        NetworkKit.setWifiEnabled(getActivity(), isChecked);
    }

    public static final String KEY_NETWORK_PROXY_HOST = "proxy_host";
    public static final String KEY_NETWORK_PROXY_PORT = "proxy_port";
    public static final String KEY_NETWORK_PROXY_CHECK = "proxy_check";

}
