package com.skydoves.preferenceroomdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.skydoves.preferenceroom.InjectPreference;
import com.skydoves.preferenceroomdemo.components.PreferenceComponent_UserProfileComponent;
import com.skydoves.preferenceroomdemo.entities.Preference_UserProfile;
import com.skydoves.preferenceroomdemo.models.PrivateInfo;

/**
 * Developed by skydoves on 2017-11-26.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class LoginActivity extends AppCompatActivity {

    /**
     * UserProfile entity.
     * {@link com.skydoves.preferenceroomdemo.entities.Profile}
     */
    @InjectPreference
    public Preference_UserProfile userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        PreferenceComponent_UserProfileComponent.getInstance().inject(this);

        final EditText editText_nick = findViewById(R.id.login_editText_nick);
        final EditText editText_age = findViewById(R.id.login_editText_age);
        final Button button = findViewById(R.id.login_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputNick = editText_nick.getText().toString();
                String inputAge = editText_age.getText().toString();
                if(!inputNick.equals("") || !inputAge.equals("")) {
                    userProfile.putLogin(true);
                    userProfile.putNickname(inputNick);
                    userProfile.putUserinfo(new PrivateInfo(inputNick, Integer.parseInt(inputAge)));

                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(getBaseContext(), "please fill all inputs", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
