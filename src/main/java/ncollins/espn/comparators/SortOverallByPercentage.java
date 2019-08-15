package ncollins.espn.comparators;

import ncollins.model.Order;
import ncollins.model.espn.OwnerToOverall;

import java.util.Comparator;

public class SortOverallByPercentage implements Comparator<OwnerToOverall> {
    private Order order;

    public SortOverallByPercentage(Order order){ this.order = order; }

    public int compare(OwnerToOverall a, OwnerToOverall b)
    {
        if(this.order.equals(Order.ASC)){
            return a.getOverall().getPercentage().compareTo(
                    b.getOverall().getPercentage());
        } else {
            return b.getOverall().getPercentage().compareTo(
                    a.getOverall().getPercentage());
        }
    }
}
