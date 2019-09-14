package ncollins.espn.comparators;

import ncollins.model.Order;
import ncollins.model.espn.RosterForCurrentScoringPeriod;
import ncollins.model.espn.Score;

import java.util.Comparator;

public class SortRosterEntriesByPoints implements Comparator<RosterForCurrentScoringPeriod.RosterEntry> {
    private Order order;

    public SortRosterEntriesByPoints(Order order){ this.order = order; }

    public int compare(RosterForCurrentScoringPeriod.RosterEntry a,
                       RosterForCurrentScoringPeriod.RosterEntry b)
    {
        if(this.order.equals(Order.ASC)){
            return a.getPlayerPoolEntry().getAppliedStatTotal().compareTo(b.getPlayerPoolEntry().getAppliedStatTotal());
        } else {
            return b.getPlayerPoolEntry().getAppliedStatTotal().compareTo(a.getPlayerPoolEntry().getAppliedStatTotal());
        }

    }
}
