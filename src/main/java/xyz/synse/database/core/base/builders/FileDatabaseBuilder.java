package xyz.synse.database.core.base.builders;

import xyz.synse.database.core.abstracts.IBuilder;
import xyz.synse.database.core.abstracts.IEncryption;
import xyz.synse.database.core.abstracts.ISerialization;
import xyz.synse.database.core.base.Database;
import xyz.synse.database.core.base.encryption.NoEncryption;
import xyz.synse.database.core.base.encryption.SimpleEncryption;
import xyz.synse.database.core.base.serialization.JavaSerialization;
import xyz.synse.database.core.base.store.FileStore;

import java.io.File;

public class FileDatabaseBuilder implements IBuilder<Database> {
    private File databaseFile = new File("database.ldb");
    private ISerialization serialization = new JavaSerialization();
    private IEncryption encryption = new NoEncryption();

    public FileDatabaseBuilder withFile(File databaseFile){
        this.databaseFile = databaseFile;
        return this;
    }

    public FileDatabaseBuilder withFile(String databaseFile){
        this.databaseFile = new File(databaseFile);
        return this;
    }

    public FileDatabaseBuilder withSerialization(ISerialization serialization){
        this.serialization = serialization;
        return this;
    }

    public FileDatabaseBuilder withEncryption(IEncryption encryption){
        this.encryption = encryption;
        return this;
    }

    public FileDatabaseBuilder withSimpleEncryption(String encryptionKey){
        this.encryption = new SimpleEncryption(encryptionKey);
        return this;
    }

    @Override
    public Database build() {
        return new Database(new FileStore(databaseFile, serialization, encryption));
    }
}
