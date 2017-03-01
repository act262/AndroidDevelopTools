package io.micro.adt.ui.hosts;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.micro.adt.R;

/**
 * Hosts Content
 *
 * @author act262@gmail.com
 */
public class HostsContentFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams")
        View inflate = inflater.inflate(R.layout.fragment_hosts_content, null);
        return inflate;
    }
}
