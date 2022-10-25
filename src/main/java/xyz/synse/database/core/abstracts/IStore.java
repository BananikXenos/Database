package xyz.synse.database.core.abstracts;

public interface IStore {
    Object get(String key);
    void set(String key, Object object);

    void saveAll();
    void clearAll();
    void close();
}
