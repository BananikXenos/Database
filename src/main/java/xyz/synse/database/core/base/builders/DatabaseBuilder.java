package xyz.synse.database.core.base.builders;

public class DatabaseBuilder {
    public CachedFileDatabaseBuilder cachedFileDatabase(){
        return new CachedFileDatabaseBuilder();
    }

    public FileDatabaseBuilder fileDatabase(){
        return new FileDatabaseBuilder();
    }

    public MemoryDatabaseBuilder memoryDatabase(){
        return new MemoryDatabaseBuilder();
    }
}
