package com.example.registerandlogin;

import static com.example.registerandlogin.Retrofit.RetrofitClient.KEY;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.registerandlogin.Retrofit.Api;
import com.example.registerandlogin.Retrofit.RetrofitClient;
import com.example.registerandlogin.models.UserDataModel;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountPageActivity extends AppCompatActivity {
    private Api api;

    private static final int GALLERY_REQ_CODE = 1000;
    private ImageView profileImage;
    private Button changeDataBtn,
                   cancelChanges;
    private EditText firstNameEdit,
                     lastNameEdit,
                     emailEdit,
                     phoneEdit,
                     submitEdit;
    private ConstraintLayout editLayout;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_page);

        Intent intent = getIntent();
        intent.getExtras();
        Map<String, Object> userInfo = (HashMap<String, Object>) intent.getSerializableExtra("map");
        firstName = (String) userInfo.get("first_name");
        lastName = (String) userInfo.get("last_name");
        email = (String) userInfo.get("email");
        phone = (String) userInfo.get("phone");
        api = RetrofitClient.getRetrofit().create(Api.class);

        firstNameEdit = findViewById(R.id.firstName);
        lastNameEdit = findViewById(R.id.lastName);
        emailEdit = findViewById(R.id.email);
        phoneEdit = findViewById(R.id.phone);
        ConstraintLayout imageLoader = findViewById(R.id.imgLoaderLayout);
        imageLoader.setOnClickListener(v -> loadImage());
        setUserData();
        profileImage = findViewById(R.id.account_img);
        changeDataBtn = findViewById(R.id.change_user_data_btn);
        makeEditTextClickable(false);
        submitEdit = findViewById(R.id.submit_edit);
        Button submitChangesBtn = findViewById(R.id.submit_btn);
        submitChangesBtn.setOnClickListener(v -> checkPassword());
        editLayout = findViewById(R.id.edit_layout);
        cancelChanges = findViewById(R.id.cancel_changes_user_data_btn);
        cancelChanges.setOnClickListener(v -> cancelChanges());
        changeDataBtn.setText("Change data");
        changeDataBtn.setOnClickListener(v -> changeData());

    }

    private void checkPassword() {
        password = submitEdit.getText().toString();
        if (!password.isEmpty()) {
            api = RetrofitClient.getRetrofit().create(Api.class);
            Call<UserDataModel> call = api.loginUser(KEY, email, password);
            call.enqueue(new Callback<UserDataModel>() {
                @Override
                public void onResponse(Call<UserDataModel> call, Response<UserDataModel> response) {
                    if (!response.isSuccessful()) {
                        System.out.println(response.code());
                    }
                    if (response.body() != null) {
                        updateData();
                        postData(response.body().getToken());
                    } else {
                        Toast.makeText(AccountPageActivity.this, "Password is incorrect", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UserDataModel> call, Throwable t) {
                    System.out.println(t.getMessage());
                }
            });
        }
    }

    private void updateData() {
        email = emailEdit.getText().toString();
        firstName = firstNameEdit.getText().toString();
        lastName = lastNameEdit.getText().toString();
        phone = phoneEdit.getText().toString();
        setUserData();
    }

    private void postData(String token) {
        Map<String, Object> userInfo = new HashMap();
        userInfo.put("first_name", firstName);
        userInfo.put("last_name", lastName);
        userInfo.put("username", email);
        userInfo.put("email", email);
        userInfo.put("password", password);
        userInfo.put("phone", phone);
        Call<UserDataModel> call = api.updateUserData(KEY, token, userInfo);
        call.enqueue(new Callback<UserDataModel>() {

            @Override
            public void onResponse(@NonNull Call<UserDataModel> call, @NonNull Response<UserDataModel> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AccountPageActivity.this, "Data added to API", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserDataModel> call, @NonNull Throwable t) {
                System.out.println("Error found is : " + t.getMessage());
            }
        });
    }

    private void setUserData() {
        firstNameEdit.setText(firstName);
        lastNameEdit.setText(lastName);
        emailEdit.setText(email);
        phoneEdit.setText(phone);
    }

    private void cancelChanges() {
        makeEditTextClickable(false);
        setUserData();
        cancelChanges.setVisibility(View.GONE);
        editLayout.setVisibility(View.GONE);
        changeDataBtn.setText("Change data");
    }

    private void makeEditTextClickable(Boolean condition) {
        firstNameEdit.setFocusableInTouchMode(condition);
        lastNameEdit.setFocusableInTouchMode(condition);
        emailEdit.setFocusableInTouchMode(condition);
        phoneEdit.setFocusableInTouchMode(condition);
        firstNameEdit.clearFocus();
        lastNameEdit.clearFocus();
        emailEdit.clearFocus();
        phoneEdit.clearFocus();
    }

    private void loadImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_REQ_CODE) {
                profileImage.setImageURI(data.getData());
            }
        }
    }

    private void changeData() {
        makeEditTextClickable(true);
        changeDataBtn.setText("Save changes");
        editLayout.setVisibility(View.VISIBLE);
        cancelChanges.setVisibility(View.VISIBLE);
    }

}
