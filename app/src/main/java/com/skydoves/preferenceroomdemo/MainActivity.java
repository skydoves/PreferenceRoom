package com.skydoves.preferenceroomdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.skydoves.preferenceroom.InjectPreference;
import com.skydoves.preferenceroomdemo.components.PreferenceComponent_PrefsComponent;
import com.skydoves.preferenceroomdemo.converters.Preference_Test;
import com.skydoves.preferenceroomdemo.entities.Preference_UserProfile;
import com.skydoves.preferenceroomdemo.models.TestClass;

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

        TestClass testClass = new TestClass();
        testClass.init();
        TextView textView = findViewById(R.id.textView);
        textView.setText(testClass.getUserName());
    }
}
