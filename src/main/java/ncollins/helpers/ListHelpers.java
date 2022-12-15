package ncollins.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListHelpers {
    public static <T> Map<Integer, List<T>> mostCommon(List<T> list) {
        Map<T, Integer> map = new HashMap<>();

        // build hashmap (T item -> Integer frequency)
        for (T t : list) {
            Integer val = map.get(t);
            map.put(t, val == null ? 1 : val + 1);
        }

        // get max frequency
        Integer max = null;
        for (Map.Entry<T, Integer> e : map.entrySet()) {
            if (max == null || e.getValue() > max)
                max = e.getValue();
        }

        // get all items with max frequency, add to hashmap
        Map<Integer, List<T>> maxFrequencyToPlayersMap = new HashMap<>();
        maxFrequencyToPlayersMap.put(max, new ArrayList());
        for (Map.Entry<T, Integer> e : map.entrySet()) {
            if (e.getValue().equals(max)) {
                maxFrequencyToPlayersMap.get(max).add(e.getKey());
            }
        }

        return maxFrequencyToPlayersMap;
    }
}
