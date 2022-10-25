package xyz.synse.database.core.base.store;

import xyz.synse.database.core.abstracts.IStore;

import java.util.HashMap;

public class MemoryStore implements IStore {
    private final HashMap<String, Object> values = new HashMap<>();

    @Override
    public Object get(String key) {
        return values.get(key);
    }

    @Override
    public void set(String key, Object object) {
        if(values.containsKey(key))
            values.replace(key, object);
        else
            values.put(key, object);
    }

    @Override
    public void saveAll() {

    }

    @Override
    public void clearAll() {

    }

    @Override
    public void close() {
        values.clear();
    }
}
