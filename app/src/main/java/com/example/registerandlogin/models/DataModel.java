package com.example.registerandlogin.models;

import com.google.gson.annotations.SerializedName;

public class DataModel {
    @SerializedName("info")
    private UserDataModel userDataModel;

    public UserDataModel getUserDataModel() {
        return userDataModel;
    }

    public void setUserDataModel(UserDataModel userDataModel) {
        this.userDataModel = userDataModel;
    }
}


