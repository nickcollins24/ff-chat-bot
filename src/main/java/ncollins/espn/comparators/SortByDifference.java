package ncollins.espn.comparators;

import ncollins.model.Order;
import ncollins.model.espn.Matchup;

import java.util.Comparator;

public class SortByDifference implements Comparator<Matchup> {
    private Order order;

    public SortByDifference(Order order){ this.order = order; }

    public int compare(Matchup a, Matchup b)
    {
        if(this.order.equals(Order.ASC)){
            return Double.valueOf(Math.abs(a.getScheduleItem().getHome().getTotalPoints() - a.getScheduleItem().getAway().getTotalPoints())).compareTo(
                    Double.valueOf(Math.abs(b.getScheduleItem().getHome().getTotalPoints() - b.getScheduleItem().getAway().getTotalPoints())));
        } else {
            return Double.valueOf(Math.abs(b.getScheduleItem().getHome().getTotalPoints() - b.getScheduleItem().getAway().getTotalPoints())).compareTo(
                    Double.valueOf(Math.abs(a.getScheduleItem().getHome().getTotalPoints() - a.getScheduleItem().getAway().getTotalPoints())));
        }

    }
}
