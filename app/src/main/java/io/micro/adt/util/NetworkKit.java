package io.micro.adt.util;

import android.content.Context;
import android.net.ProxyInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 网络相关的工具
 *
 * @author act262@gmail.com
 */
public class NetworkKit {

    public static WifiManager getWifiManager(Context context) {
        return (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    /**
     * 设置网络代理
     *
     * @param host 主机名或者IP
     * @param port 端口
     */
    public static void setProxy(Context context, boolean enabled, String host, int port) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setProxyL(context, enabled, host, port);
        } else {
            Toast.makeText(context, "current device not support setup proxy", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static void setProxyL(Context context, boolean enabled, String host, int port) {
        WifiManager wifiManager = getWifiManager(context);
        WifiConfiguration wifiConfiguration = getCurrentConfiguration(wifiManager);
        try {
            // ProxyInfo
            ProxyInfo proxyInfo = ProxyInfo.buildDirectProxy(host, port);

            // WifiConfiguration.setHttpProxy
            Method setHttpProxy = Class.forName("android.net.wifi.WifiConfiguration")
                    .getMethod("setHttpProxy", ProxyInfo.class);
            setHttpProxy.invoke(wifiConfiguration, enabled ? proxyInfo : null);

//            wifiManager.saveConfiguration(); // 不知道为啥没效果这个?
            Class<?> listenerCls = Class.forName("android.net.wifi.WifiManager$ActionListener");
            Method save = Class.forName("android.net.wifi.WifiManager")
                    .getMethod("save", WifiConfiguration.class, listenerCls);
            save.invoke(wifiManager, wifiConfiguration, null);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 当前连接的 WiFi 配置
     */
    public static WifiConfiguration getCurrentConfiguration(WifiManager wifiManager) {
        WifiInfo connectionInfo = wifiManager.getConnectionInfo();
        List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration configuredNetwork : configuredNetworks) {
            if (configuredNetwork.networkId == connectionInfo.getNetworkId()) {
                return configuredNetwork;
            }
        }
        return null;
    }

    /**
     * 关闭/启用 WiFi
     */
    public static void setWifiEnabled(Context context, boolean enabled) {
        getWifiManager(context).setWifiEnabled(enabled);
    }

    private static void dumpWifi(WifiManager wifiManager) {
        List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration configuredNetwork : configuredNetworks) {
            System.out.println("configuredNetwork = " + configuredNetwork);
        }
    }
}
