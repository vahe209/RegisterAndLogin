package com.example.registerandlogin.baseColors;

import com.example.registerandlogin.baseColors.Colors;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColorsGetter {
    @SerializedName("custom_colors")
    private List<Colors> allColors;

    public List<Colors> getColors() {
        return allColors;
    }
}
