package com.example.registerandlogin.baseColors;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class Colors {
    @SerializedName("value")
   private  String value;

    public String getValue() {
        return value;
    }

    @SerializedName("name")
    private  String name;

    public  String getName() {
        return name;
    }

}

