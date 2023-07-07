package com.example.registerandlogin;

import static com.example.registerandlogin.RetrofitClient.KEY;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private Button login;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        register = findViewById(R.id.register_btn);
        login = findViewById(R.id.login_btn);
        register.setOnClickListener(v -> createRegisterActivity());
        login.setOnClickListener(v -> checkInfo());
    }

    private void checkInfo() {
        EditText emailEdit = findViewById(R.id.email_ed_text);
        String email = emailEdit.getText().toString();
        EditText passwordEdit = findViewById(R.id.password_ed_text);
        String password = passwordEdit.getText().toString();

        RetrofitClient client = new RetrofitClient();
        Call<ResponseBody> call = client.api.loginUser(KEY, email, password);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                TextView incorrectData = findViewById(R.id.login_wrong_info);
                if (!response.isSuccessful()) {
                    System.out.println("code:" + response.code());
                }
                if (response.body() != null) {
                    incorrectData.setVisibility(View.GONE);
                    createActivity(response.body().getFirstName(), response.body().getLatName(), response.body().getEmail(), response.body().getPhone());
                } else {
                    incorrectData.setVisibility(View.VISIBLE);
                }
                return;
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("Error");
            }
        });
    }

    private void createActivity(String firstName, String lastName, String email, String phone) {
        Map<String, Object> userInfo = new HashMap();
        userInfo.put("first_name", firstName);
        userInfo.put("last_name", lastName);
        userInfo.put("email", email);
        userInfo.put("phone", phone);
        Intent intent = new Intent(this, AccountPageActivity.class);
        intent.putExtra("map", (HashMap) userInfo);
        startActivity(intent);
    }

    private void createRegisterActivity() {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}


