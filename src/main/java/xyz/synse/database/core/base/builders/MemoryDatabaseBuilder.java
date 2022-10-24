package xyz.synse.database.core.base.builders;

import xyz.synse.database.core.abstracts.IBuilder;
import xyz.synse.database.core.base.Database;
import xyz.synse.database.core.base.store.MemoryStore;

public class MemoryDatabaseBuilder implements IBuilder<Database> {
    @Override
    public Database build() {
        return new Database(new MemoryStore());
    }
}
