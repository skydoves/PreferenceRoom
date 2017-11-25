package com.skydoves.preferenceroomdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.skydoves.preferenceroom.InjectPreference;

public class MainActivity extends AppCompatActivity {

    @InjectPreference
    public Preference_Test testsss;

    @InjectPreference
    public Preference_UserProfile userProfile;

    @InjectPreference
    public PreferenceComponent_PrefsComponent component;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceComponent_PrefsComponent.getInstance().inject(this);

        MyClass myClass = new MyClass("skydoves", 23);
        userProfile.putMyClass(myClass);

        TextView textView = findViewById(R.id.textView);
        textView.setText(component.UserProfile().getMyClass().getName());
    }
}
