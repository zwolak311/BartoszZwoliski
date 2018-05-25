package com.bartosz.bartoszzwoliski.API;

import com.bartosz.bartoszzwoliski.POJO.LeagueTablePOJO;

public interface LeagueTableInterface {

    void onResponse(LeagueTablePOJO leagueTable);

    void onFailure(String message);

}
