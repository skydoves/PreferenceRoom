package com.skydoves.preferenceroomdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.skydoves.preferenceroom.InjectPreference;

public class MainActivity extends AppCompatActivity {

    public int aaa = 10;

    @InjectPreference
    public Preference_Test test;

    @InjectPreference
    public Preference_UserProfile userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PreferenceComponent_PrefsComponent.init(this);

        PreferenceComponent_PrefsComponent component = PreferenceComponent_PrefsComponent.getInstance();

        Preference_UserProfile userProfile = PreferenceComponent_PrefsComponent.getInstance().UserProfile();

        component.UserProfile().putUserName("PreferenceRoom Test123");

        TextView textView = findViewById(R.id.textView);
        textView.setText(component.UserProfile().getUserName());
    }
}
