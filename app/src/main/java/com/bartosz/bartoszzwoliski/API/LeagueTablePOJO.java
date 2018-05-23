package com.bartosz.bartoszzwoliski.API;

import java.util.ArrayList;

public class LeagueTablePOJO {

    String leagueCaption;
    ArrayList<Standing> standing = new ArrayList<>();


    public String getLeagueCaption() {
        return leagueCaption;
    }

    public void setLeagueCaption(String leagueCaption) {
        this.leagueCaption = leagueCaption;
    }

    public ArrayList<Standing> getStanding() {
        return standing;
    }

    public void setStanding(ArrayList<Standing> standing) {
        this.standing = standing;
    }

    public class Standing {
        String teamName, points, playedGames, teamId, wins, draws, crestURI;
        int losses, position;

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public String getTeamName() {
            return teamName;
        }

        public void setTeamName(String teamName) {
            this.teamName = teamName;
        }

        public String getPoints() {
            return points;
        }

        public void setPoints(String points) {
            this.points = points;
        }

        public String getPlayedGames() {
            return playedGames;
        }

        public void setPlayedGames(String playedGames) {
            this.playedGames = playedGames;
        }

        public String getTeamId() {
            return teamId;
        }

        public void setTeamId(String teamId) {
            this.teamId = teamId;
        }


        public String getWins() {
            return wins;
        }

        public void setWins(String wins) {
            this.wins = wins;
        }

        public String getDraws() {
            return draws;
        }

        public void setDraws(String draws) {
            this.draws = draws;
        }

        public int getLosses() {
            return losses;
        }

        public void setLosses(int losses) {
            this.losses = losses;
        }

        public String getCrestURI() {
            return crestURI;
        }

        public void setCrestURI(String crestURI) {
            this.crestURI = crestURI;
        }
    }

}
