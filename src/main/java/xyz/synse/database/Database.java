package xyz.synse.database;

import xyz.synse.database.encryption.IEncryption;
import xyz.synse.database.exceptions.DatabaseLoadException;
import xyz.synse.database.exceptions.DatabaseSaveException;
import xyz.synse.database.serialization.ISerialization;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class Database {
    private final File databaseFolder;
    private final long cacheKeepTime;
    private final IEncryption encryption;
    private final ISerialization serialization;
    private final HashMap<Object, CachedValue> cachedValues = new HashMap<>();
    private final boolean autoSave;
    private final boolean throwRuntimeExceptions;

    public Database(File databaseFolder, IEncryption encryption, long cacheKeepTime, ISerialization serialization, boolean autoSave, boolean throwRuntimeExceptions) {
        this.databaseFolder = databaseFolder;
        this.encryption = encryption;
        this.cacheKeepTime = cacheKeepTime;
        this.serialization = serialization;
        this.autoSave = autoSave;
        this.throwRuntimeExceptions = throwRuntimeExceptions;
    }

    public void set(Object key, Object value) {
        if(cachedValues.containsKey(key))
            this.cachedValues.replace(key, new CachedValue(value));
        else
            cachedValues.put(key, new CachedValue(value));

        if(autoSave) {
            try {
                save();
            } catch (Exception e) {
                System.err.printf("[Database] Error saving value %s with key %s during auto-save", value, key);
                if(throwRuntimeExceptions)
                    throw new DatabaseSaveException("[Database] Failed to save database during auto-save", e);
            }
        }
    }

    public Object get(Object key) {
        // Load from cache
        if(cachedValues.containsKey(key))
            return cachedValues.get(key).value;

        //Load from file
        int hash = key.hashCode();
        File file = new File(databaseFolder, hash + "" + key.getClass().getName() + ".dat");

        if(!file.exists())
            return null;

        try {
            return serialization.deserialize(encryption.decrypt(readToString(file)));
        } catch (Exception e) {
            throw new DatabaseLoadException("[Database] Failed to load value", e);
        }
    }

    public Object getOrElse(Object key, Object defaultValue){
        try {
            Object loaded = get(key);

            if (loaded != null)
                return loaded;
        }catch (Exception e){
            System.err.printf("[Database] Error loading value with key %s. Returning default value %s", key, defaultValue);
            if(throwRuntimeExceptions)
                throw new DatabaseLoadException("[Database] Error loading value", e);
        }

        return defaultValue;
    }

    public void clearCache(){
        // Clears cache
        cachedValues.entrySet().removeIf(entry -> (System.currentTimeMillis() - entry.getValue().addDate) > cacheKeepTime && entry.getValue().isSaved);
    }

    public void save() throws Exception {
        // Loop
        for(Map.Entry<Object, CachedValue> cachedValue : cachedValues.entrySet()){
            // File
            int hash = cachedValue.getKey().hashCode();
            File file = new File(databaseFolder, hash + "" + cachedValue.getKey().getClass().getName() + ".dat");

            // Make
            if(!file.exists())
                file.createNewFile();

            // Write
            FileWriter fileWriter = new FileWriter(file, false);
            fileWriter.write(encryption.encrypt(serialization.serialize(cachedValue.getValue().value)));
            fileWriter.flush();
            fileWriter.close();

            cachedValue.getValue().isSaved = true;
        }
    }

    public void close(){
        try {
            save();
        } catch (Exception e) {
            System.err.print("[Database] Error closing database");
            if(throwRuntimeExceptions)
                throw new DatabaseSaveException("[Database] Failed to save database during close", e);
        }

        cachedValues.clear();
    }

    private String readToString(File file) {
        try {
            byte[] bytes = Files.readAllBytes(file.toPath());
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.print("[Database] Error reading value from file");
            if(throwRuntimeExceptions)
                throw new DatabaseLoadException("[Database] Failed to read value from file", e);
        }

        return null;
    }

    static class CachedValue {
        private final Object value;
        private boolean isSaved = false;
        private final long addDate = System.currentTimeMillis();

        CachedValue(Object value) {
            this.value = value;
        }
    }
}
