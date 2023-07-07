package com.example.registerandlogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private TextView incorrectPassText;
    private TextView incorrectEmailText;

    private EditText fNameEdit;
    private EditText lNameEdit;
    private EditText emailEdit;
    private EditText phoneEdit;
    private EditText passwordEdit;
    private EditText passConfirmEdit;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private String passConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ImageView prevArrow = findViewById(R.id.arrow);
        Button loginBtn = findViewById(R.id.login_btn);
        prevArrow.setOnClickListener(v -> onBackPressed());
        loginBtn.setOnClickListener(v -> onBackPressed());
        Button registerBtn = findViewById(R.id.register_btn);

        fNameEdit = findViewById(R.id.firstName);
        lNameEdit = findViewById(R.id.lastName);
        emailEdit = findViewById(R.id.email);
        phoneEdit = findViewById(R.id.phone);
        passwordEdit = findViewById(R.id.password);
        passConfirmEdit = findViewById(R.id.conf_pass);
        incorrectPassText = findViewById(R.id.incorrect_register_text);
        incorrectEmailText = findViewById(R.id.incorrect_email_text);


        registerBtn.setOnClickListener(v -> {
            firstName = fNameEdit.getText().toString();
            lastName = lNameEdit.getText().toString();
            email = emailEdit.getText().toString();
            phone = phoneEdit.getText().toString();
            password = passwordEdit.getText().toString();
            passConfirm = passConfirmEdit.getText().toString();
            if (isFilledAndValid()) {
            } else {
                Toast.makeText(RegisterActivity.this, "Fill all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void postData(String firstName, String lastName, String email, String password, String phone) {
        Map<String, Object> userInfo = new HashMap();
        userInfo.put("first_name", firstName);
        userInfo.put("last_name", lastName);
        userInfo.put("email", email);
        userInfo.put("password", password);
        userInfo.put("phone", phone);
        if (isValidEmail(email) && isValid(password)) {
            RetrofitClient client = new RetrofitClient();
            Call<ResponseBody> call = client.api.CreateUser(RetrofitClient.KEY, userInfo);
            call.enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Data added to API", Toast.LENGTH_SHORT).show();
                        ResponseBody body = response.body();
                        if (body != null) {
                            createActivity(userInfo);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    System.out.println("Error found is : " + t.getMessage());
                }
            });
        }
    }

    private boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            incorrectEmailText.setVisibility(View.GONE);
            return true;
        } else {
            incorrectEmailText.setVisibility(View.VISIBLE);
            return false;
        }
    }

    private boolean isValid(String password) {
        String regex = "^(?=.*[0-9])" + "(?=.*[a-z])(?=.*[A-Z])" + "(?=.*[@#$%^&+=])" + "(?=\\S+$).{8,20}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(password);
        if (m.matches()) {
            incorrectPassText.setVisibility(View.GONE);
            return true;
        } else {
            incorrectPassText.setVisibility(View.VISIBLE);
            return false;
        }
    }
    private boolean isFilledAndValid() {
        if (!firstName.isEmpty() && !lastName.isEmpty() && !email.isEmpty() && !phone.isEmpty() && !password.isEmpty() && !passConfirm.isEmpty()) {
            if (password.equals(passConfirm)) {
                postData(firstName, lastName, email, password, phone);
                return true;
            } else {
                Toast.makeText(RegisterActivity.this, "Password must be correct", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return false;
    }

    private void createActivity(Map<String, Object> map) {
        Intent intent = new Intent(this, AccountPageActivity.class);
        intent.putExtra("map", (HashMap) map);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        incorrectPassText.setVisibility(View.GONE);
    }
}
