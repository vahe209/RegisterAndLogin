package com.example.registerandlogin.models;

import com.google.gson.annotations.SerializedName;

public class UserDataModel {
    @SerializedName("first_name")
    private String first_name;

    @SerializedName("last_name")
    private String last_name;

    @SerializedName("email")
    private String email;

    @SerializedName("phone")
    private String phone;
    @SerializedName("token")
    private String token;
    @SerializedName("profile_picture")
    private String img;

    public Object   getImg() {
        return img;
    }

    public String getToken() {
        return token;
    }

    public String getFirstName() {
        return first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
