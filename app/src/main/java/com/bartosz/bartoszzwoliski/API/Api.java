package com.bartosz.bartoszzwoliski.API;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Api {


    @GET("v1/competitions/?season=2015")
    Call<ArrayList<CurrentLeauge>> getLeagueList();

    @GET("v1/competitions/{leagueId}/leagueTable")
    Call<LeagueTablePOJO> getLeagueTable(@Path("leagueId")String leagueId);

}
