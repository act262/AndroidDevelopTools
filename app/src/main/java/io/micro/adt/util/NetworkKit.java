package io.micro.adt.util;

import android.content.Context;
import android.content.Intent;
import android.net.ProxyInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
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

    private static WifiManager getWifiManager(Context context) {
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

    public static String getProxyHost(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getProxyHostL(context);
        }
        return "";
    }

    public static String getProxyPort(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getProxyPortL(context);
        }
        return "";
    }

    private static ProxyInfo getProxyInfo(Context context) {
        ProxyInfo info = null;
        WifiManager wifiManager = getWifiManager(context);
        WifiConfiguration wifiConfiguration = getCurrentConfiguration(wifiManager);
        // fix wifi disabled
        if (wifiConfiguration == null) {
            return null;
        }
        try {
            Method httpProxy = Class.forName("android.net.wifi.WifiConfiguration").getMethod("getHttpProxy");
            info = (ProxyInfo) httpProxy.invoke(wifiConfiguration);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return info;
    }

    // 需要System Uid才能修改?
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static void setProxyL(Context context, boolean enabled, String host, int port) {
        WifiManager wifiManager = getWifiManager(context);
        WifiConfiguration wifiConfiguration = getCurrentConfiguration(wifiManager);
        try {
            // ProxyInfo
            ProxyInfo proxyInfo = ProxyInfo.buildDirectProxy(host, port);

            //  enum: NONE,STATIC,UNASSIGNED,PAC
            Class<?> settingsCls = Class.forName("android.net.IpConfiguration$ProxySettings");
            Object[] enumConstants = settingsCls.getEnumConstants();

            // WifiConfiguration.setHttpProxy
            Method setHttpProxy = Class.forName("android.net.wifi.WifiConfiguration")
                    .getMethod("setProxy", settingsCls, ProxyInfo.class);
            if (enabled) {
                setHttpProxy.invoke(wifiConfiguration, enumConstants[1], proxyInfo); // setProxy(STATIC,proxy)
            } else {
                setHttpProxy.invoke(wifiConfiguration, enumConstants[0], proxyInfo); // setProxy(NONE,proxy)
            }

            Class<?> listenerCls = Class.forName("android.net.wifi.WifiManager$ActionListener");
            Method save = Class.forName("android.net.wifi.WifiManager")
                    .getMethod("save", WifiConfiguration.class, listenerCls);
            save.invoke(wifiManager, wifiConfiguration, null);
//            wifiManager.saveConfiguration(); // 不知道为啥没效果这个?
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static String getProxyHostL(Context context) {
        ProxyInfo proxyInfo = getProxyInfo(context);
        return proxyInfo == null ? "" : proxyInfo.getHost();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static String getProxyPortL(Context context) {
        ProxyInfo proxyInfo = getProxyInfo(context);
        return proxyInfo == null ? "" : String.valueOf(proxyInfo.getPort());
    }

    /**
     * 当前连接的 WiFi 配置
     */
    private static WifiConfiguration getCurrentConfiguration(WifiManager wifiManager) {
        WifiInfo connectionInfo = wifiManager.getConnectionInfo();
        List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
        // fix wifi disabled
        if (configuredNetworks != null) {
            for (WifiConfiguration configuredNetwork : configuredNetworks) {
                if (configuredNetwork.networkId == connectionInfo.getNetworkId()) {
                    return configuredNetwork;
                }
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

    /**
     * 打开WiFi设置页面
     */
    public static void openWifiSettings(Context context) {
        try {
            context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void dumpWifi(WifiManager wifiManager) {
        List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration configuredNetwork : configuredNetworks) {
            System.out.println("configuredNetwork = " + configuredNetwork);
        }
    }
}
