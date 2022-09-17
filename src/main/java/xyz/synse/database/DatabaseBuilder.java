package xyz.synse.database;

import xyz.synse.database.encryption.IEncryption;
import xyz.synse.database.encryption.NoEncryption;

import java.io.File;

public class DatabaseBuilder<K, V> {
    private final File databaseDirectory;
    private IEncryption encryption = new NoEncryption();
    private boolean autoSave = false;
    private long cacheKeepTime = 30_000L;
    private boolean throwRuntimeExceptions = true;

    public DatabaseBuilder(File databaseDirectory) {
        this.databaseDirectory = databaseDirectory;
    }

    public DatabaseBuilder(String location) {
        this.databaseDirectory = new File(location);
    }

    public DatabaseBuilder<K, V> withEncryption(IEncryption encryption){
        this.encryption = encryption;
        return this;
    }

    public DatabaseBuilder<K, V> autoSave(){
        this.autoSave = true;
        return this;
    }

    public DatabaseBuilder<K, V> cacheKeepTime(long millis){
        this.cacheKeepTime = millis;
        return this;
    }

    public DatabaseBuilder<K, V> ignoreRuntimeExceptions(){
        this.throwRuntimeExceptions = false;
        return this;
    }

    public Database<K,V> build(){
        return new Database<>(databaseDirectory, encryption, cacheKeepTime, autoSave, throwRuntimeExceptions);
    }
}
