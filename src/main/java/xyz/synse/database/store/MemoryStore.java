package xyz.synse.database.store;

import java.util.HashMap;

public class MemoryStore extends Store {
    private final HashMap<String, Object> values = new HashMap<>();

    @Override
    public Object read(String key) {
        return values.get(key);
    }

    @Override
    public void save(String key, Object object) {
        if(values.containsKey(key))
            values.replace(key, object);
        else
            values.put(key, object);
    }

    @Override
    public void saveAll() {

    }

    @Override
    public void close() {
        values.clear();
    }
}
