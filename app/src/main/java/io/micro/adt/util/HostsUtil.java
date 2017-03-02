package io.micro.adt.util;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import io.android.shell.cmd.CmdSet;

/**
 * Hosts Util
 *
 * @author act262@gmail.com
 */
public class HostsUtil {

    public static String readSystemHosts() {
        StringBuilder sb = new StringBuilder();
        File file = new File("/system/etc/sysHosts");
        try {
            CmdSet.suExecutor.exec("chmod a+r /system/etc/sysHosts");

            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            // print execute result
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                sb.append(line);
                sb.append("\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    /**
     * 写入Host文件,
     * <br/>
     * 格式:<pre>ip domain \n</pre>
     */
    public static void writeHosts(Context context, String hosts) {
        File directory = Environment.getExternalStorageDirectory();
        directory = context.getFilesDir();
        File hostsFile = new File(directory, "hosts.tmp");
        if (!hostsFile.exists()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                hostsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//        try {
//            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(hostsFile));
//            writer.write(hosts);
//            writer.flush();
//            writer.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        try {
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(hostsFile)));
            writer.write(hosts);
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        handleHosts(hostsFile.getAbsolutePath());
    }

    public static void handleHosts(String newHostsFile) {
        writeHosts(newHostsFile);
    }


    public static void writeHosts(String newHostPath) {
        try {
//            CmdSet.suExecutor.exec("chmod 666 /system/etc/hosts");
            // first backup old host file
            CmdSet.suExecutor.exec("mv /system/etc/hosts /system/etc/hosts.bak");
            // then copy new host file to replace it
            CmdSet.suExecutor.exec("cp " + newHostPath + " /system/etc/hosts");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
