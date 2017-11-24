package com.skydoves.preferenceroomdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.skydoves.preferenceroom.InjectPreference;
import com.skydoves.preferenceroom.PreferenceRoom;

public class MainActivity extends AppCompatActivity {

    @InjectPreference
    public Preference_Test testsss;

    @InjectPreference
    public Preference_UserProfile userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceRoom.inject(this);

        TextView textView = findViewById(R.id.textView);
        textView.setText(userProfile.getUserName());
    }
}
