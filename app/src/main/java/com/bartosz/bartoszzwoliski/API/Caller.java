package com.bartosz.bartoszzwoliski.API;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Caller {
    Api api;

    public Caller() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);


        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.addInterceptor(logging);
        okHttpClient.readTimeout(5, TimeUnit.SECONDS);
        okHttpClient.connectTimeout(5, TimeUnit.SECONDS);


        Gson gson = new GsonBuilder()
                .setLenient()
                .create();


        Retrofit builder = new Retrofit.Builder()
                .baseUrl("http://api.football-data.org/")
                .client(okHttpClient.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        api = builder.create(Api.class);

    }




    public Api getApi() {
        return api;
    }
}
