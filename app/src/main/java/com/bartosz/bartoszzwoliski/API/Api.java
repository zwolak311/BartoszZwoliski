package com.bartosz.bartoszzwoliski.API;

import com.bartosz.bartoszzwoliski.POJO.LeagueSimpleNamePOJO;
import com.bartosz.bartoszzwoliski.POJO.LeagueTablePOJO;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Api {


    @GET("v1/competitions/?season=2016")
    Call<ArrayList<LeagueSimpleNamePOJO>> getLeagueList();

    @GET("v1/competitions/{leagueId}/leagueTable")
    Call<LeagueTablePOJO> getLeagueTable(@Path("leagueId")String leagueId);

}
