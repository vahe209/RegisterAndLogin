package com.example.registerandlogin;

import com.google.gson.annotations.SerializedName;

public class ResponseBody {
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

    @SerializedName("pass")
    private String pass;

    public String getPassword() {
        return pass;
    }

    public void setPassword(String password) {
        this.pass = password;
    }

    public String getToken() {
        return token;
    }
    public String getFirstName() {
        return first_name;
    }



    public void setFirstName(String fName) {
        this.first_name = fName;
    }

    public String getLatName() {
        return last_name;
    }

    public void setLastName(String lName) {
        this.last_name = lName;
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


