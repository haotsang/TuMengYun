package com.example.myapplication.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;


import androidx.annotation.NonNull;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class RetrofitUtils {
    private static RetrofitUtils retrofitUtils;
    private RetrofitUtils() {
    }
    public static RetrofitUtils getInstance() {

        if (retrofitUtils == null) {
            synchronized (RetrofitUtils.class) {
                if (retrofitUtils == null) {
                    retrofitUtils = new RetrofitUtils();
                }
            }
        }
        return retrofitUtils;
    }
    //返回Retrofit
    public Retrofit getRetrofit() {
        // 屏蔽证书
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .sslSocketFactory(TrustAllCerts.createSSLSocketFactory(), new TrustAllCerts()) // 信任所有证书
//                .hostnameVerifier(new TrustAllHostnameVerifier())
                .connectTimeout(10, TimeUnit.SECONDS)  // 超时时间
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();

//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                // 向Request Header添加一些业务相关数据，如APP版本，token
//                .addInterceptor(new HeadInterceptor())
//                //日志Interceptor，可以打印日志
////                .addInterceptor(logging)
//                // 连接超时时间设置
//                .connectTimeout(10, TimeUnit.SECONDS)
//                // 读取超时时间设置
//                .readTimeout(10, TimeUnit.SECONDS)
//                // 失败重试
//                .retryOnConnectionFailure(true)
//                // 支持Https需要加入SSLSocketFactory
////                .sslSocketFactory(sslSocketFactory)
//                // 信任的主机名，返回true表示信任，可以根据主机名和SSLSession判断主机是否信任
//                .hostnameVerifier((hostname, session) -> true)
//                // 使用host name作为cookie保存的key
//                .cookieJar(new CookieJar() {
//                    private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();
//                    @Override
//                    public void saveFromResponse(@NonNull HttpUrl url, @NonNull List<Cookie> cookies) {
//                        cookieStore.put(HttpUrl.parse(url.host()), cookies);
//                    }
//                    @NonNull
//                    @Override
//                    public List<Cookie> loadForRequest(@NonNull HttpUrl url) {
//                        List<Cookie> cookies = cookieStore.get(HttpUrl.parse(url.host()));
//                        return cookies != null ? cookies : new ArrayList<>();
//                    }
//                })
//                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://106.15.94.206:8085/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit;
    }


    private static class HeadInterceptor implements Interceptor {
        @NonNull
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            Request compressedRequest = originalRequest.newBuilder()
                    .headers(Headers.of())
                    .build();
            return chain.proceed(compressedRequest);
        }
    }
}