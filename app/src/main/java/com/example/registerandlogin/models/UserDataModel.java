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

    public String getPassword() {
        return password;
    }

    @SerializedName("password")
    private String password;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFirstName() {
        return first_name;
    }

    public void setFirstName(String firstName) {
        this.first_name = firstName;
    }

    public String getLastName() {
        return last_name;
    }

    public void setLastName(String lastName) {
        this.last_name = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
