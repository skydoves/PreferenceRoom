package com.skydoves.preferenceroomdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.skydoves.preferenceroom.InjectPreference;

public class MainActivity extends AppCompatActivity {

    public int aaa = 10;

    @InjectPreference
    public Preference_Test testsss;

    @InjectPreference
    public Preference_UserProfile userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PreferenceComponent_PrefsComponent.init(this);
        Injector_MainActivity injector_mainActivity = new Injector_MainActivity(this);

        PreferenceComponent_PrefsComponent component = PreferenceComponent_PrefsComponent.getInstance();

        Preference_UserProfile userProfile = PreferenceComponent_PrefsComponent.getInstance().UserProfile();

        component.UserProfile().putUserName("PreferenceRoom Test567567");

        TextView textView = findViewById(R.id.textView);
        textView.setText(userProfile.getUserName());
    }
}
