package com.skydoves.preferenceroomdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PreferenceComponent_PrefsComponent component = PreferenceComponent_PrefsComponent.inject(this);

        component.UserProfile().putUserName("PreferenceRoom Test");

        TextView textView = findViewById(R.id.textView);
        textView.setText(component.UserProfile().getUserName());
    }
}
