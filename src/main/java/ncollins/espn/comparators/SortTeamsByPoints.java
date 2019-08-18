package ncollins.espn.comparators;

import ncollins.model.Order;
import ncollins.model.espn.Team;

import java.util.Comparator;

public class SortTeamsByPoints implements Comparator<Team> {
    private Order order;

    public SortTeamsByPoints(Order order){ this.order = order; }

    public int compare(Team a, Team b)
    {

        if(this.order.equals(Order.ASC)){
            return a.getRecord().getOverall().getPointsFor().compareTo(
                    b.getRecord().getOverall().getPointsFor());
        } else {
            return b.getRecord().getOverall().getPointsFor().compareTo(
                    a.getRecord().getOverall().getPointsFor());
        }
    }
}
