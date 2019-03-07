package com.aptiv.watchdogapp;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class LoginActivity extends AppCompatActivity {

    private final static String SECRET_KEY_CODE = "11620922";

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editText = findViewById(R.id.editText);

        findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyCode = editText.getText().toString();
                if (keyCode.equals(SECRET_KEY_CODE)) {
                    openMainActivity();
                }
                else {
                    Toast.makeText(LoginActivity.this, "Invalid key code", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openMainActivity() {
        Intent myIntent = new Intent(this, MainActivity.class);
        startActivity(myIntent);
    }
}
