package com.example.registerandlogin;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitClient {
    private static final String BASE_URL = "https://dev.apirequests.com/api/v2/";
    public static final String KEY = "098WXjnytf!rFN0lX7RinOA2hz@KkeDloMei2CRwRPXtzXPu1DMppkrGTqobF@w0";

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    Api api = retrofit.create(Api.class);
}
