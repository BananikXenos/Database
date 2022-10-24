package xyz.synse.database.core.base;

import xyz.synse.database.core.abstracts.IStore;
import xyz.synse.database.core.base.utils.Constant;

public class Database {
    private final IStore store;

    public Database(IStore store) {
        this.store = store;
    }

    public Object set(Object key, Object value) {
        store.save(Constant.computeKey(key), value);

        return value;
    }

    public boolean has(Object key){
        return get(Constant.computeKey(key)) != null;
    }

    public Object get(Object key) {
        return store.read(Constant.computeKey(key));
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

        return set(key, defaultValue);
    }

    public void save() {
        store.saveAll();
    }

    public void clear(){
        store.clearAll();
    }

    public void close() {
        save();
        store.close();
    }
}
