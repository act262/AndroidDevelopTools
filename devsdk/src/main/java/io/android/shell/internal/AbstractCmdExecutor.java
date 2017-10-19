package io.android.shell.internal;

import android.os.Handler;
import android.os.Looper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.android.shell.CmdResult;
import io.android.shell.ICmdExecutor;
import io.android.shell.ResultCallback;

/**
 * Abstract implementation.
 *
 * @author act262@gmail.com
 */
abstract class AbstractCmdExecutor implements ICmdExecutor {

    private final static String COMMAND_EXIT = "exit\n";
    private final static String COMMAND_LINE_END = "\n";

    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public CmdResult exec(String... commands) throws IOException {
        return execInternal(commands);
    }

    @Override
    public void execAsync(final String[] commands, final ResultCallback<CmdResult> callback) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final CmdResult result = execInternal(commands);
                    if (callback != null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onReceiveResult(result);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private CmdResult execInternal(String[] commands) throws IOException {
        CmdResult result = new CmdResult();
        Process process = Runtime.getRuntime().exec(execEnv());
        DataOutputStream dataOutputStream = new DataOutputStream(process.getOutputStream());
        for (String command : commands) {
            dataOutputStream.writeBytes(command);
            dataOutputStream.writeBytes(COMMAND_LINE_END);
        }
        dataOutputStream.writeBytes(COMMAND_EXIT);
        dataOutputStream.flush();
        try {
            result.exitValue = process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
            result.exitValue = -1;
        }

        BufferedReader resultReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        StringBuilder resultBuilder = new StringBuilder();
        StringBuilder errorBuilder = new StringBuilder();

        String line;
        while ((line = resultReader.readLine()) != null) {
            resultBuilder.append(line);
        }
        line = null;
        while ((line = errorReader.readLine()) != null) {
            errorBuilder.append(line);
        }
        result.successResult = resultBuilder.toString();
        result.errorResult = errorBuilder.toString();

        dataOutputStream.close();
        resultReader.close();
        errorReader.close();
        process.destroy();

        return result;
    }

    protected abstract String execEnv();

}
