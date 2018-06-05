package com.bartosz.bartoszzwoliski.API;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.bartosz.bartoszzwoliski.POJO.LeagueSimpleNamePOJO;
import com.bartosz.bartoszzwoliski.POJO.LeagueTablePOJO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyRetrofit {
    private Api api;
    private LeagueListInterface leagueListInterface;
    private LeagueTableInterface leagueTableInterface;

    public MyRetrofit(LeagueListInterface leagueListInterface) {
        this.leagueListInterface = leagueListInterface;
        setupApi();
    }


    public MyRetrofit(LeagueTableInterface leagueTableInterface) {
        this.leagueTableInterface = leagueTableInterface;
        setupApi();
    }

    private void setupApi() {
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


    public void getLeagueList() {


        Call<ArrayList<LeagueSimpleNamePOJO>> callLeagueList = api.getLeagueList();

        callLeagueList.clone().enqueue(new Callback<ArrayList<LeagueSimpleNamePOJO>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<LeagueSimpleNamePOJO>> call, @NonNull Response<ArrayList<LeagueSimpleNamePOJO>> response) {

                if (response.body() != null)
                    leagueListInterface.onResponse(response.body());
                else
                    leagueListInterface.onFailure("Błąd połączenia z API");
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<LeagueSimpleNamePOJO>> call, @NonNull Throwable t) {
                leagueListInterface.onFailure(t.getMessage());

            }
        });

    }


    public void getLeagueTable(String leagueId) {

        Call<LeagueTablePOJO> callLeagueTable = api.getLeagueTable(leagueId);

        callLeagueTable.clone().enqueue(new Callback<LeagueTablePOJO>() {
            @Override
            public void onResponse(@NonNull Call<LeagueTablePOJO> call, @NonNull Response<LeagueTablePOJO> response) {

                if (response.body() != null) {
                    leagueTableInterface.onResponse(response.body());
                } else
                    leagueTableInterface.onFailure("Błąd połączenia z API");

            }

            @Override
            public void onFailure(@NonNull Call<LeagueTablePOJO> call, @NonNull Throwable t) {

                leagueTableInterface.onFailure(t.getMessage());

            }
        });

    }
}
