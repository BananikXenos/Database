package xyz.synse.database.store;

public abstract class Store {
    public abstract Object read(String key);
    public abstract void save(String key, Object object);

    public abstract void saveAll();
    public abstract void close();
}
