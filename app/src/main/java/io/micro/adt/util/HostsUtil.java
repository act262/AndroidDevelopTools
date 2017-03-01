package io.micro.adt.util;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import io.android.shell.cmd.CmdSet;

/**
 * Hosts Util
 *
 * @author act262@gmail.com
 */
public class HostsUtil {

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
        CmdSet.writeHosts(newHostsFile);
    }
}
