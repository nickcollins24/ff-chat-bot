package ncollins.model.espn;

import java.util.List;

public class Trade {
    private Team team0;
    private Team team1;
    private List<Player> playersToTeam0;
    private List<Player> playersToTeam1;

    public Trade(){}

    public Trade(Team team0, Team team1, List<Player> playersToTeam0, List<Player> playersToTeam1){
        this.team0 = team0;
        this.team1 = team1;
        this.playersToTeam0 = playersToTeam0;
        this.playersToTeam1 = playersToTeam1;
    }

    public Team getTeam0(){
        return team0;
    }

    public Team getTeam1(){
        return team1;
    }

    public List<Player> getPlayersToTeam0(){
        return playersToTeam0;
    }

    public List<Player> getPlayersToTeam1(){
        return playersToTeam1;
    }

    public void setTeam0(Team team){
        team0 = team;
    }

    public void setTeam1(Team team){
        team1 = team;
    }

    public void setPlayersToTeam0(List<Player> players){
        playersToTeam0 = players;
    }

    public void setPlayersToTeam1(List<Player> players){
        playersToTeam1 = players;
    }
}
