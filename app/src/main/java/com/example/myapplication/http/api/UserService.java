package com.example.myapplication.http.api;

import com.example.myapplication.entity.CangKuItem;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface UserService {
    @POST("lingjian/getLingJianAll")
    Call<CangKuItem> loginGet (@Query("token") String token);

    @POST("lingjian/getLingJianAll")
    Call<ResponseBody> carReportList(@Body RequestBody parameter);
//    @POST("lingjian/getLingJianAll")
//    @FormUrlEncoded
//    Call<CangKuItem> loginPost (@Field("username") String username, @Field("password") String password);
}