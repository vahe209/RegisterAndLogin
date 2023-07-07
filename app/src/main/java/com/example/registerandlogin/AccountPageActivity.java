package com.example.registerandlogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

public class AccountPageActivity extends AppCompatActivity {
    private EditText firstNameEdit;
    private EditText lastNameEdit;
    private EditText emailEdit;
    private EditText phoneEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_page);
        Intent intent = getIntent();
        intent.getExtras();
        Map<String, Object> userInfo = (HashMap<String, Object>) intent.getSerializableExtra("map");
        String firstName = (String) userInfo.get("first_name");
        String lastName = (String) userInfo.get("last_name");
        String email = (String) userInfo.get("email");
        String phone = (String) userInfo.get("phone");

        firstNameEdit = findViewById(R.id.firstName);
        lastNameEdit = findViewById(R.id.lastName);
        emailEdit = findViewById(R.id.email);
        phoneEdit = findViewById(R.id.phone);
        firstNameEdit.setText(firstName);
        lastNameEdit.setText(lastName);
        emailEdit.setText(email);
        phoneEdit.setText(phone);
    }
}