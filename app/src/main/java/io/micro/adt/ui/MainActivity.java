package io.micro.adt.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import io.micro.adt.R;
import io.micro.adt.service.FloatBallService;
import io.micro.adt.util.Developer;

public class MainActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {

    private SharedPreferences preferences;

    private static final String KEY_FLOAT_BALL_SWITCH = "floatBallSwitch";
    private StatusReceiver receiver;
    private Switch floatBallSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setOnMenuItemClickListener(this);

        floatBallSwitch = (Switch) findViewById(R.id.switchFloatBall);
        floatBallSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                preferences.edit().putBoolean(KEY_FLOAT_BALL_SWITCH, isChecked).apply();
                toggleFloatBallService(isChecked);
            }
        });
        floatBallSwitch.setChecked(preferences.getBoolean(KEY_FLOAT_BALL_SWITCH, false));

        receiver = new StatusReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_FLOAT_BALL_SWITCH_CHANGED);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                Developer.openDevelopmentSettings(MainActivity.this);
                break;
            case R.id.menu_float:
                break;
            default:
                // no-op
        }
        return true;
    }

    private void toggleFloatBallService(boolean checked) {
        Intent intent = new Intent(this, FloatBallService.class);
        if (checked) {
            startService(intent);
        } else {
            stopService(intent);
        }
    }

    public static final String ACTION_FLOAT_BALL_SWITCH_CHANGED = "io.micro.adt.FLOAT_BALL_SWITCH_CHANGED";

    private class StatusReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case ACTION_FLOAT_BALL_SWITCH_CHANGED:
                    boolean result = intent.getBooleanExtra("switch", false);
                    floatBallSwitch.setChecked(result);
                    break;
            }
        }
    }
}
