package hw3;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DatedMapImpl implements DatedMap {
    private final Map<String, String> datedMap = new HashMap<>();
    private final Map<String, Date> keyToLastInsertionDate = new HashMap<>();


    @Override
    public void put(String key, String value) {
        datedMap.put(key, value);
        keyToLastInsertionDate.put(key, new Date());
    }

    @Override
    public String get(String key) {
        return datedMap.get(key);
    }

    @Override
    public boolean containsKey(String key) {
        return datedMap.containsKey(key);
    }

    @Override
    public void remove(String key) {
        datedMap.remove(key);
    }

    @Override
    public Set<String> keySet() {
        return datedMap.keySet();
    }

    @Override
    public Date getKeyLastInsertionDate(String key) {
        if (!datedMap.containsKey(key)) {
            return null;
        }
        return keyToLastInsertionDate.get(key);
    }

}
