package ncollins.espn.comparators;

import ncollins.model.Order;
import ncollins.model.espn.Team;

import java.util.Comparator;

public class SortTeamsByPercentage implements Comparator<Team> {
    private Order order;

    public SortTeamsByPercentage(Order order){ this.order = order; }

    public int compare(Team a, Team b)
    {
        if(this.order.equals(Order.ASC)){
            return a.getRecord().getOverall().getPercentage().compareTo(
                    b.getRecord().getOverall().getPercentage());
        } else {
            return b.getRecord().getOverall().getPercentage().compareTo(
                    a.getRecord().getOverall().getPercentage());
        }
    }
}
