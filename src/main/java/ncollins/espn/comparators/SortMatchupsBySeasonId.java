package ncollins.espn.comparators;

import ncollins.model.Order;
import ncollins.model.espn.Matchup;

import java.util.Comparator;

public class SortMatchupsBySeasonId implements Comparator<Matchup> {
    private Order order;

    public SortMatchupsBySeasonId(Order order){ this.order = order; }

    public int compare(Matchup a, Matchup b)
    {
        if(this.order.equals(Order.ASC)){
            return Integer.valueOf(a.getSeasonId()).compareTo(Integer.valueOf(b.getSeasonId()));
        } else {
            return Integer.valueOf(b.getSeasonId()).compareTo(Integer.valueOf(a.getSeasonId()));
        }

    }
}
