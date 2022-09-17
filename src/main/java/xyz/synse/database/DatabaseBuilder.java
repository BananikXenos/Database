package xyz.synse.database;

import xyz.synse.database.encryption.IEncryption;
import xyz.synse.database.encryption.NoEncryption;
import xyz.synse.database.serialization.ISerialization;
import xyz.synse.database.serialization.JavaSerialization;

import java.io.File;

public class DatabaseBuilder {
    private final File databaseDirectory;
    private IEncryption encryption = new NoEncryption();
    private boolean autoSave = false;
    private long cacheKeepTime = 30_000L;
    private boolean throwRuntimeExceptions = true;
    private ISerialization serialization = new JavaSerialization();

    public DatabaseBuilder(File databaseDirectory) {
        this.databaseDirectory = databaseDirectory;
    }

    public DatabaseBuilder(String location) {
        this.databaseDirectory = new File(location);
    }

    public DatabaseBuilder withEncryption(IEncryption encryption){
        this.encryption = encryption;
        return this;
    }

    public DatabaseBuilder autoSave(){
        this.autoSave = true;
        return this;
    }

    public DatabaseBuilder cacheKeepTime(long millis){
        this.cacheKeepTime = millis;
        return this;
    }

    public DatabaseBuilder ignoreRuntimeExceptions(){
        this.throwRuntimeExceptions = false;
        return this;
    }

    public DatabaseBuilder withSerialization(ISerialization serialization){
        this.serialization = serialization;
        return this;
    }

    public Database build(){
        return new Database(databaseDirectory, encryption, cacheKeepTime, serialization, autoSave, throwRuntimeExceptions);
    }
}
