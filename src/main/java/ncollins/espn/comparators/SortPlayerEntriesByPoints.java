package ncollins.espn.comparators;

import ncollins.model.Order;
import ncollins.model.espn.RosterForCurrentScoringPeriod;
import ncollins.model.espn.Score;

import java.util.Comparator;

public class SortPlayerEntriesByPoints implements Comparator<RosterForCurrentScoringPeriod.PlayerPoolEntry> {
    private Order order;

    public SortPlayerEntriesByPoints(Order order){ this.order = order; }

    public int compare(RosterForCurrentScoringPeriod.PlayerPoolEntry a,
                       RosterForCurrentScoringPeriod.PlayerPoolEntry b)
    {
        if(this.order.equals(Order.ASC)){
            return a.getAppliedStatTotal().compareTo(b.getAppliedStatTotal());
        } else {
            return b.getAppliedStatTotal().compareTo(a.getAppliedStatTotal());
        }

    }
}
