package com.example.registerandlogin.Retrofit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;
    public static final String BASE_URL = "https://dev.apirequests.com/api/v2/";
    public static final String KEY = "098WXjnytf!rFN0lX7RinOA2hz@KkeDloMei2CRwRPXtzXPu1DMppkrGTqobF@w0";
    public static final String IMG_BASE_URL = "https://imsba.s3.amazonaws.com/";

    public static Retrofit getRetrofit() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
//                .client(client)
                .build();
        return retrofit;
    }
}
