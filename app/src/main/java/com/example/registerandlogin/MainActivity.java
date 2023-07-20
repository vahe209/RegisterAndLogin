package com.example.registerandlogin;

import static com.example.registerandlogin.Retrofit.RetrofitClient.BASE_URL;
import static com.example.registerandlogin.Retrofit.RetrofitClient.IMG_BASE_URL;
import static com.example.registerandlogin.Retrofit.RetrofitClient.KEY;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.example.registerandlogin.Icons.GetIconFromDirectory;
import com.example.registerandlogin.Retrofit.Api;
import com.example.registerandlogin.Retrofit.RetrofitClient;
import com.example.registerandlogin.baseColors.ColorsGetter;
import com.example.registerandlogin.models.DataModel;
import com.example.registerandlogin.models.UserDataModel;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ConstraintLayout mainActivityConstraint;
    private Api api;
    private Map<String, String> color;
    private ImageView iconView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivityConstraint = findViewById(R.id.mainActivityConstraint);
        api = RetrofitClient.getRetrofit().create(Api.class);
        color = new HashMap<>();
        iconView = findViewById(R.id.icon);
        setBackgroundColor();
        System.out.println("HashMap" + color);
        Button register = findViewById(R.id.register_btn);
        Button login = findViewById(R.id.login_btn);
        SharedPreferences sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);
        String token = sharedPreferences.getString("tokenOfSignedAccount", null);
        register.setOnClickListener(v -> createRegisterActivity());
        login.setOnClickListener(v -> checkInfo());

        if (token != null) {
            Call<DataModel> call = api.getData(KEY, token);
            call.enqueue(new Callback<DataModel>() {
                @Override
                public void onResponse(@NonNull Call<DataModel> call, @NonNull Response<DataModel> response) {
                    if (response.isSuccessful()) {
                        createPageActivity(response.body().getUserDataModel().getFirstName(), response.body().getUserDataModel().getLastName(), response.body().getUserDataModel().getEmail(), response.body().getUserDataModel().getPhone(), (String) response.body().getUserDataModel().getImg());
                    } else {
                        System.out.println(response.code());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<DataModel> call, @NonNull Throwable t) {
                    System.out.println(t.getMessage());
                }
            });
        }
    }

    private void setBackgroundColor() {
        Call<ColorsGetter> call = api.getBgColor(KEY);
        call.enqueue(new Callback<ColorsGetter>() {
            @Override
            public void onResponse(Call<ColorsGetter> call, Response<ColorsGetter> response) {
                if (response.isSuccessful()) {
                    for (int i = 0; i < response.body().getColors().size(); i++) {
                        color.put(response.body().getColors().get(i).getName(), response.body().getColors().get(i).getValue());
                    }
                }
                mainActivityConstraint.setBackgroundColor(Color.parseColor("#" + color.get("accentMain")));
            }

            @Override
            public void onFailure(Call<ColorsGetter> call, Throwable t) {
                System.out.println("Error");
            }
        });

        Call<GetIconFromDirectory> callIcon = api.getBgIcon(KEY);
        callIcon.enqueue(new Callback<GetIconFromDirectory>() {
            @Override
            public void onResponse(Call<GetIconFromDirectory> call, Response<GetIconFromDirectory> response) {
                if (response.isSuccessful()) {
                    String icon = IMG_BASE_URL + response.body().getIcons().getIcon();
                    Glide.with(MainActivity.this).
                            load(icon).
                            centerCrop().
                            into(iconView);
                    mainActivityConstraint.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFailure(Call<GetIconFromDirectory> call, Throwable t) {

            }
        });


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
        userInfo.put("image", file);

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