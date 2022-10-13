package ncollins.model.espn;

import java.util.List;

public class Trade {
    private Member member0;
    private Member member1;
    private List<Player> playersToMember0;
    private List<Player> playersToMember1;

    public Trade(){}

    public Trade(Member member0, Member member1, List<Player> playersToMember0, List<Player> playersToMember1){
        this.member0 = member0;
        this.member1 = member1;
        this.playersToMember0 = playersToMember0;
        this.playersToMember1 = playersToMember1;
    }

    public Member getMember0(){
        return member0;
    }

    public Member getMember1(){
        return member1;
    }

    public List<Player> getPlayersToMember0(){
        return playersToMember0;
    }

    public List<Player> getPlayersToMember1(){
        return playersToMember1;
    }

    public void setMember0(Member member){
        member0 = member;
    }

    public void setMember1(Member member){
        member1 = member;
    }

    public void setPlayersToMember0(List<Player> players){
        playersToMember0 = players;
    }

    public void setPlayersToMember1(List<Player> players){
        playersToMember1 = players;
    }
}
