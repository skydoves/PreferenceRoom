package com.skydoves.preferenceroomdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PreferenceComponent_PrefsComponent.inject(this);

        PreferenceComponent_PrefsComponent.UserProfile().putUserName("PreferenceRoom Test");

        TextView textView = findViewById(R.id.textView);
        textView.setText(PreferenceComponent_PrefsComponent.UserProfile().getUserName());
    }
}
