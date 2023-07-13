package com.example.registerandlogin.Retrofit;

import android.service.autofill.UserData;

import com.example.registerandlogin.models.DataModel;
import com.example.registerandlogin.models.UserDataModel;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {
    @FormUrlEncoded
    @POST("users/register")
    Call<UserDataModel> CreateUser(
            @Query("key") String key,
            @FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST("users/login")
    Call<UserDataModel> loginUser(
            @Query("key") String key,
            @Field("email") String email,
            @Field("password") String password);

    @FormUrlEncoded
    @POST("users/profile")
    Call<UserDataModel> updateUserData(
            @Query("key") String key,
            @Header("token") String token,
            @FieldMap Map<String, Object> map);

    @GET("users/profile")
    Call<DataModel> getData(
            @Query("key") String key,
            @Header("token") String token);
}


