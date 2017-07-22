package io.android.shell;

import android.support.annotation.NonNull;

/**
 * Command execute exitValue callback.
 *
 * @author act262@gmail.com
 */
public interface ResultCallback<T> {
    /**
     * Invoked when the exitValue is available.
     *
     * @param result the exitValue
     */
    void onReceiveResult(@NonNull T result) throws Exception;
}
