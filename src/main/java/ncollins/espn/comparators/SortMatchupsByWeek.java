package ncollins.espn.comparators;

import ncollins.model.Order;
import ncollins.model.espn.Matchup;

import java.util.Comparator;

public class SortMatchupsByWeek implements Comparator<Matchup> {
    private Order order;

    public SortMatchupsByWeek(Order order){ this.order = order; }

    public int compare(Matchup a, Matchup b)
    {
        if(this.order.equals(Order.ASC)){
            return Integer.valueOf(a.getScheduleItem().getMatchupPeriodId()).compareTo(b.getScheduleItem().getMatchupPeriodId());
        } else {
            return Integer.valueOf(b.getScheduleItem().getMatchupPeriodId()).compareTo(Integer.valueOf(a.getScheduleItem().getMatchupPeriodId()));
        }

    }
}
