package ncollins.espn.comparators;

import ncollins.model.Order;
import ncollins.model.espn.Score;

import java.util.Comparator;

public class SortByScore implements Comparator<Score> {
    private Order order;

    public SortByScore(Order order){ this.order = order; }

    public int compare(Score a, Score b)
    {
        if(this.order.equals(Order.ASC)){
            return a.getPoints().compareTo(b.getPoints());
        } else {
            return b.getPoints().compareTo(a.getPoints());
        }

    }
}
