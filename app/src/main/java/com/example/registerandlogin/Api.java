package com.example.registerandlogin;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {
    @FormUrlEncoded
    @POST("users/register")
    Call<ResponseBody> CreateUser(
            @Query("key") String key,
            @FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST("users/login")
    Call<ResponseBody> loginUser(
            @Query("key")String key,
            @Field("username") String email,
            @Field("password") String password
    );
}
