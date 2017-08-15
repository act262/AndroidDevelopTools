package io.micro.adt.module.network.hosts;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import io.micro.adt.R;
import io.micro.adt.model.HostsListItem;

/**
 * Hosts Content
 *
 * @author act262@gmail.com
 */
public class HostsContentFragment extends Fragment {

    private EditText contentText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams")
        View inflate = inflater.inflate(R.layout.fragment_hosts_content, null);
        contentText = (EditText) inflate.findViewById(R.id.et_content);
        return inflate;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void setHosts(HostsListItem item) {
        contentText.setText(item.content);
    }
}
