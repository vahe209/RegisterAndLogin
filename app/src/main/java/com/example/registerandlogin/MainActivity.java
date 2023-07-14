package com.example.registerandlogin;

import static com.example.registerandlogin.Retrofit.RetrofitClient.KEY;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.registerandlogin.Retrofit.Api;
import com.example.registerandlogin.Retrofit.RetrofitClient;
import com.example.registerandlogin.models.DataModel;
import com.example.registerandlogin.models.UserDataModel;

import java.io.File;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button register = findViewById(R.id.register_btn);
        Button login = findViewById(R.id.login_btn);
        register.setOnClickListener(v -> createRegisterActivity());
        login.setOnClickListener(v -> checkInfo());
        api = RetrofitClient.getRetrofit().create(Api.class);
        SharedPreferences sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);
        String token = sharedPreferences.getString("tokenOfSignedAccount", null);
        if (token != null) {
            Call<DataModel> call = api.getData(KEY, token);
            call.enqueue(new Callback<DataModel>() {
                @Override
                public void onResponse(@NonNull Call<DataModel> call, @NonNull Response<DataModel> response) {
                    if (!response.isSuccessful()) {
                        System.out.println(response.code());
                    }
                    createPageActivity(response.body().getUserDataModel().getFirstName(), response.body().getUserDataModel().getLastName(), response.body().getUserDataModel().getEmail(), response.body().getUserDataModel().getPhone(), (String) response.body().getUserDataModel().getImg());
                }

                @Override
                public void onFailure(@NonNull Call<DataModel> call, @NonNull Throwable t) {
                    System.out.println(t.getMessage());
                }
            });
        }
    }

    private void checkInfo() {
        EditText emailEdit = findViewById(R.id.email_ed_text);
        String email = emailEdit.getText().toString();
        EditText passwordEdit = findViewById(R.id.password_ed_text);
        String password = passwordEdit.getText().toString();
        if (!email.isEmpty() && !password.isEmpty()) {
            Call<UserDataModel> call = api.loginUser(KEY, email, password);
            call.enqueue(new Callback<UserDataModel>() {
                @Override
                public void onResponse(Call<UserDataModel> call, @NonNull Response<UserDataModel> response) {
                    TextView incorrectData = findViewById(R.id.login_wrong_info);
                    if (!response.isSuccessful()) {
                        System.out.println("code:" + response.code());
                    }
                    if (response.body() != null) {
                        incorrectData.setVisibility(View.GONE);
                        SharedPreferences prefs = getSharedPreferences("userData", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("tokenOfSignedAccount", response.body().getToken()).apply();
                        createPageActivity(response.body().getFirstName(), response.body().getLastName(), response.body().getEmail(), response.body().getPhone(), (String) response.body().getImg());
                    } else {
                        incorrectData.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<UserDataModel> call, Throwable t) {
                    System.out.println("Error");
                }
            });
        } else {
            Toast.makeText(MainActivity.this, "Fill all fields", Toast.LENGTH_SHORT).show();
        }
    }

    private void createPageActivity(String firstName, String lastName, String email, String phone, String file) {
        HashMap<String, Object> userInfo = new HashMap<>();
        SharedPreferences prefs = getSharedPreferences("userData", MODE_PRIVATE);
        userInfo.put("first_name", firstName);
        userInfo.put("last_name", lastName);
        userInfo.put("email", email);
        userInfo.put("username", email);
        userInfo.put("phone", phone);
        userInfo.put("img", file);

        String token = prefs.getString("tokenOfSignedAccount", "eroooor");
        userInfo.put("token", token);
        Intent intent = new Intent(this, AccountPageActivity.class);
        intent.putExtra("map", userInfo);
        startActivity(intent);
    }

    private void createRegisterActivity() {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}