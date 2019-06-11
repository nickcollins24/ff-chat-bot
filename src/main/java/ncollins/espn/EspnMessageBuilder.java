package ncollins.espn;

public class EspnMessageBuilder {
    /***
     *  scores {top|bottom} [TOTAL] [YEAR|ever]-- top/bottom scores this year or all-time. if year==null, show this year
     */
    public String buildScoresMessage(String text){
        return "TODO: " + text;
    }

    /***
     *  matchups [WEEK] -- matchups for given week. if week==null, show this week
     *  matchups [TEAM0] [TEAM1] -- all time matchup stats
     */
    public String buildMatchupsMessage(String text){
        return "TODO: " + text;
    }

    /***
     *  standings [YEAR|ever] -- standings for given year or all-time. if year==null, show this year.
     */
    public String buildStandingsMessage(String text){
        return "TODO: " + text;
    }

    /***
     *  points [for|against] through [WEEK] {top|bottom} [TOTAL] -- most/least points for or against through given week
     */
    public String buildPointsMessage(String text){
        return "TODO: " + text;
    }

    /***
     *  players {top|bottom} [TOTAL] [POSITION] [YEAR] -- years top/worst players. if year==null, show this week
     */
    public String buildPlayersMessage(String text){
        return "TODO: " + text;
    }

    /***
     *  streaks [POINT_TOTAL] points [TOTAL] -- longest streaks of point_total >= points
     *  streaks {win|loss} [TOTAL] -- longest regular season win/loss streaks
     */
    public String buildStreaksMessage(String text){
        return "TODO: " + text;
    }

    /***
     *  jujus -- this years jujus
     */
    public String buildJujusMessage(){
        return "TODO: jujus";
    }

    /***
     *  salties -- this years salties
     */
    public String buildSaltiesMessage(){
        return "TODO: salties";
    }

    /***
     *  blowouts [TOTAL] -- biggest blowouts ever
     */
    public String buildBlowoutsMessage(int count){
        return "TODO: blowouts " + count;
    }

    /***
     *  heartbreaks [TOTAL] -- closest games ever
     */
    public String buildHeartbreaksMessage(int count){
        return "TODO: heartbreaks " + count;
    }
}
