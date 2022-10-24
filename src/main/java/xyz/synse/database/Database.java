package xyz.synse.database;

import xyz.synse.database.exceptions.DatabaseLoadException;
import xyz.synse.database.store.Store;

public class Database {
    private final Store store;

    public Database(Store store) {
        this.store = store;
    }

    public void set(Object key, Object value) {
        int hash = key.hashCode();
        store.save(hash + "" + key.getClass().getName(), value);
    }

    public Object get(Object key) {
        try {
            int hash = key.hashCode();
            return store.read(hash + "" + key.getClass().getName());
        } catch (Exception e) {
            throw new DatabaseLoadException("[Database] Failed to load value", e);
        }
    }

    public Object getOrElse(Object key, Object defaultValue) {
        Object loaded = get(key);

        if (loaded != null)
            return loaded;

        return defaultValue;
    }

    public Object getOrSet(Object key, Object defaultValue) {
        Object loaded = get(key);

        if (loaded != null)
            return loaded;

        set(key, defaultValue);
        return defaultValue;
    }

    public void save() {
        store.saveAll();
    }

    public void close() {
        save();
        store.close();
    }
}
