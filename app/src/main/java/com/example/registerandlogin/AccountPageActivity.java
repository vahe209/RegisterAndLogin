package com.example.registerandlogin;

import static com.example.registerandlogin.Retrofit.RetrofitClient.IMG_BASE_URL;
import static com.example.registerandlogin.Retrofit.RetrofitClient.KEY;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.registerandlogin.Retrofit.Api;
import com.example.registerandlogin.Retrofit.RetrofitClient;
import com.example.registerandlogin.models.DataModel;
import com.example.registerandlogin.models.UserDataModel;

import java.io.File;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountPageActivity extends AppCompatActivity {
    private Api api;

    private static final int GALLERY_REQ_CODE = 1000;
    private ImageView profileImage;
    private Button changeDataBtn,
            cancelChanges,
            savePngBtn;
    private String path;
    private Uri uri;
    private EditText firstNameEdit,
            lastNameEdit,
            emailEdit,
            phoneEdit,
            submitEdit;
    private Bitmap bitmap;
    private ConstraintLayout editLayout;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private String token;
    private String image;


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
        token = (String) userInfo.get("token");
        image = (String) userInfo.get("image");

        api = RetrofitClient.getRetrofit().create(Api.class);

        firstNameEdit = findViewById(R.id.firstName);
        lastNameEdit = findViewById(R.id.lastName);
        emailEdit = findViewById(R.id.email);
        phoneEdit = findViewById(R.id.phone);
        profileImage = findViewById(R.id.account_img);

        ConstraintLayout imageLoader = findViewById(R.id.imgLoaderLayout);
        imageLoader.setOnClickListener(v -> loadImage());
        setUserData();

        changeDataBtn = findViewById(R.id.change_user_data_btn);
        makeEditTextClickable(false);
        submitEdit = findViewById(R.id.submit_edit);
        Button submitChangesBtn = findViewById(R.id.submit_btn);
        submitChangesBtn.setOnClickListener(v -> checkPassword());
        editLayout = findViewById(R.id.edit_layout);
        cancelChanges = findViewById(R.id.cancel_changes_user_data_btn);
        cancelChanges.setOnClickListener(v -> cancelChanges());
        changeDataBtn.setText("Change data");
        savePngBtn = findViewById(R.id.save_png);
        savePngBtn.setVisibility(View.GONE);
        savePngBtn.setOnClickListener(v -> saveImage());
        changeDataBtn.setOnClickListener(v -> changeData());


        //setProfileImage();
    }

    private void saveImage() {
        File f = new File(path);
        RequestBody reqFile = RequestBody.create(MediaType.parse(getContentResolver().getType(uri)), f);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", f.getName(), reqFile);
        Call<UserDataModel> call = api.upload(KEY, token, body);
        call.enqueue(new Callback<UserDataModel>() {
            @Override
            public void onResponse(Call<UserDataModel> call, Response<UserDataModel> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AccountPageActivity.this, "Image Successfully added", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(AccountPageActivity.this, "Something gone incorrect", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserDataModel> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    private void checkPassword() {
        password = submitEdit.getText().toString();
        if (!password.isEmpty()) {
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
        Glide.with(AccountPageActivity.this)
                .load(IMG_BASE_URL+ image)
                .into(profileImage);
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
        launcher.launch(intent);
    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                uri = data.getData();
                Context context = AccountPageActivity.this;
                path = RealPathUtil.getRealPath(context, uri);


                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    profileImage.setImageBitmap(bitmap);
                    savePngBtn.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    });

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

//    }


}
