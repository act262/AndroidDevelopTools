package io.micro.adt.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Toast;

import io.android.shell.RootHelper;
import io.micro.adt.R;
import io.micro.adt.service.FloatBallService;
import io.micro.dev.sdk.DeveloperKit;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private static final String EXTRA_FINISH = "EXTRA_FINISH";
    private static final int REQUEST_CODE_OVERLAY = 100;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        SwitchCompat floatBallSwitch = (SwitchCompat) findViewById(R.id.switchFloatBall);
        floatBallSwitch.setOnCheckedChangeListener(this);

        boolean checked = preferences.getBoolean(FloatBallService.KEY_FLOAT_BALL_SWITCH, false);
        floatBallSwitch.setChecked(checked);
        toggleFloatBallService(checked);

        checkRootPermission();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        boolean finish = intent.getBooleanExtra(EXTRA_FINISH, false);
        if (finish) {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_information:

                break;

            case R.id.menu_settings:
                DeveloperKit.openDevelopmentSettings(getApplicationContext());
                break;

            case R.id.menu_terminal:
                TerminalFragment.newInstance().show(getFragmentManager(), "terminal");
                break;

            default:
                return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE_OVERLAY == requestCode) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
                showToast("Not allow drawOverlay");
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        preferences.edit().putBoolean(FloatBallService.KEY_FLOAT_BALL_SWITCH, isChecked).apply();
        toggleFloatBallService(isChecked);
    }

    private void toggleFloatBallService(boolean checked) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            showToast("can not DrawOverlays");
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQUEST_CODE_OVERLAY);
            return;
        }
        Intent intent = new Intent(this, FloatBallService.class);
        if (checked) {
            startService(intent);
        } else {
            stopService(intent);
        }
    }

    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    // Root权限检查
    private void checkRootPermission() {
        final HandlerThread thread = new HandlerThread("root-permission");
        thread.start();
        Handler handler = new Handler(thread.getLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                int result = RootHelper.root();
                if (result == 0) {
                    Toast.makeText(MainActivity.this, "root permission ok", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "root permission fail", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void goHome(Context context, boolean needFinish) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_FINISH, needFinish);
        context.startActivity(intent);
    }
}
