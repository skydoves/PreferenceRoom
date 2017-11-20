package com.skydoves.preferenceroomdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Preference_UserProfile profile = Preference_UserProfile.getInstance(this);
        profile.putUserName("PreferenceRoom");

        TextView textView = findViewById(R.id.textView);
        textView.setText(profile.getUserName());
    }
}
