package io.micro.adt.module.network.hosts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import io.micro.adt.R;

/**
 * Hosts list and content
 *
 * @author act262@gmail.com
 */
public class HostsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hosts);
    }
}
