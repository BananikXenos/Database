package xyz.synse.database.store;

import java.io.File;

public abstract class Store {
    protected final File dbFile;

    public Store(File dbFile) {
        this.dbFile = dbFile;
    }

    public abstract Object read(String key);
    public abstract void save(String key, Object object);

    public abstract void saveAll();
    public abstract void close();
}
