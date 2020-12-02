package ncollins.espn.comparators;

import ncollins.model.Order;
import ncollins.model.espn.Team;

import java.util.Comparator;

public class SortTeamsBySeasonId implements Comparator<Team> {
    private Order order;

    public SortTeamsBySeasonId(Order order){ this.order = order; }

    public int compare(Team a, Team b)
    {
        if(this.order.equals(Order.ASC)){
            return Integer.valueOf(a.getSeasonId()).compareTo(Integer.valueOf(b.getSeasonId()));
        } else {
            return Integer.valueOf(b.getSeasonId()).compareTo(Integer.valueOf(a.getSeasonId()));
        }

    }
}
