package io.micro.adt.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import io.micro.adt.Developer;
import io.micro.adt.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CheckBox debugLayout = (CheckBox) findViewById(R.id.debugLayout);
        debugLayout.setChecked(Developer.debugLayout());
        debugLayout.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Developer.setDebugLayout(isChecked);
            }
        });

        CheckBox debugOverdraw = (CheckBox) findViewById(R.id.debugOverdraw);
        debugOverdraw.setChecked(Developer.debugOverdraw());
        debugOverdraw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Developer.setDebugOverdraw(isChecked);
            }
        });

        CheckBox debugProfile = (CheckBox) findViewById(R.id.debugProfile);
        debugProfile.setChecked(Developer.debugProfile());
        debugProfile.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Developer.setProfile(isChecked);
            }
        });

        Button settingsButton = (Button) findViewById(R.id.settings);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoDevelopmentSettings();
            }
        });
    }

    private void gotoDevelopmentSettings() {
        Intent intent = new Intent("com.android.settings.APPLICATION_DEVELOPMENT_SETTINGS");
        startActivity(intent);
    }

}
