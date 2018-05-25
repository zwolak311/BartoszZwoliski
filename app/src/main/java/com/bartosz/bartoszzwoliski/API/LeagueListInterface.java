package com.bartosz.bartoszzwoliski.API;

import com.bartosz.bartoszzwoliski.POJO.LeagueSimpleNamePOJO;

import java.util.ArrayList;

public interface LeagueListInterface{

    void onResponse(ArrayList<LeagueSimpleNamePOJO> currentLeagues);

    void onFailure(String message);

}
