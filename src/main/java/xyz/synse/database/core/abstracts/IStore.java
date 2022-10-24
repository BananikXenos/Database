package xyz.synse.database.core.abstracts;

public interface IStore {
    Object read(String key);
    void save(String key, Object object);

    void saveAll();
    void clearAll();
    void close();
}
