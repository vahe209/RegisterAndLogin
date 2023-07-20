package com.example.registerandlogin.Retrofit;

import com.example.registerandlogin.Icons.GetIconFromDirectory;
import com.example.registerandlogin.baseColors.ColorsGetter;
import com.example.registerandlogin.models.DataModel;
import com.example.registerandlogin.models.UserDataModel;

import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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
            @Field("username") String username,
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

    @Multipart
    @POST("users/profile_photo")
    Call<UserDataModel> upload(
            @Query("key") String key,
            @Header("token") String token,
            @Part MultipartBody.Part image);

    @GET("website/settings")
    Call<ColorsGetter> getBgColor(
            @Query("key") String key);

    @GET("website/settings")
    Call<GetIconFromDirectory> getBgIcon(
            @Query("key") String key);

}


