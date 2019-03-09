package com.aptiv.watchdogapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.aptiv.watchdogapp.data.Factory;
import com.aptiv.watchdogapp.data.login.LoginManager;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity implements LoginManager.Callback {

    private EditText editText;

    private LoginManager loginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("WatchDog - Login");
        editText = findViewById(R.id.editText);

        loginManager = Factory.INSTANCE.createLoginManager();

        findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyCode = editText.getText().toString();
                loginManager.login(keyCode, LoginActivity.this);
            }
        });
    }

    @Override
    public void afterLogin(@NonNull String apiKey) {
        if (apiKey.length() > 0) {
            saveApiKey(apiKey);
            editText.setText("");
            Intent myIntent = new Intent(this, MainActivity.class);
            startActivity(myIntent);
        } else {
            Toast.makeText(LoginActivity.this, "Invalid key code", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveApiKey(String apiKey) {
        SharedPreferences sharedPref = getSharedPreferences("com.aptiv", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("API_KEY", apiKey);
        editor.commit();
    }
}
