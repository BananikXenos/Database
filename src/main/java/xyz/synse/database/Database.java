package xyz.synse.database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import xyz.synse.database.encryption.IEncryption;
import xyz.synse.database.exceptions.DatabaseLoadException;
import xyz.synse.database.exceptions.DatabaseSaveException;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class Database<K, V> {
    private final File databaseFolder;
    private final long cacheKeepTime;
    private final IEncryption encryption;
    private final HashMap<K, CachedValue> cachedValues = new HashMap<>();
    private final boolean autoSave;
    private final boolean throwRuntimeExceptions;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public Database(File databaseFolder, IEncryption encryption, long cacheKeepTime, boolean autoSave, boolean throwRuntimeExceptions) {
        this.databaseFolder = databaseFolder;
        this.encryption = encryption;
        this.cacheKeepTime = cacheKeepTime;
        this.autoSave = autoSave;
        this.throwRuntimeExceptions = throwRuntimeExceptions;
    }

    public void set(K key, V value) {
        if(cachedValues.containsKey(key))
            this.cachedValues.replace(key, new CachedValue(value));
        else
            cachedValues.put(key, new CachedValue(value));

        if(autoSave) {
            try {
                save();
            } catch (IOException e) {
                System.err.printf("[Database] Error saving value %s with key %s during auto-save", value, key);
                if(throwRuntimeExceptions)
                    throw new DatabaseSaveException("Failed to save database during auto-save", e);
            }
        }
    }

    public V get(K key){
        // Load from cache
        if(cachedValues.containsKey(key))
            return cachedValues.get(key).value;

        //Load from file
        int hash = key.hashCode();
        File file = new File(databaseFolder, hash + "" + key.getClass().getName() + ".dat");

        if(!file.exists())
            return null;

        return (V) gson.fromJson(encryption.decrypt(readToString(file)), Object.class);
    }

    public V getOrElse(K key, V defaultValue){
        try {
            V loaded = get(key);

            if (loaded != null)
                return loaded;
        }catch (DatabaseLoadException e){
            System.err.printf("[Database] Error loading value with key %s. Returning default value %s", key, defaultValue);
            if(throwRuntimeExceptions)
                throw e;
        }

        return defaultValue;
    }

    public void clearCache(){
        // Clears cache
        cachedValues.entrySet().removeIf(entry -> (System.currentTimeMillis() - entry.getValue().addDate) > cacheKeepTime && entry.getValue().isSaved);
    }

    public void save() throws IOException {
        // Loop
        for(Map.Entry<K, CachedValue> cachedValue : cachedValues.entrySet()){
            // File
            int hash = cachedValue.getKey().hashCode();
            File file = new File(databaseFolder, hash + "" + cachedValue.getKey().getClass().getName() + ".dat");

            // Make
            if(!file.exists())
                file.createNewFile();

            // Write
            FileWriter fileWriter = new FileWriter(file, false);
            fileWriter.write(encryption.encrypt(gson.toJson(cachedValue.getValue().value)));
            fileWriter.flush();
            fileWriter.close();

            cachedValue.getValue().isSaved = true;
        }
    }

    public void close(){
        try {
            save();
        } catch (IOException e) {
            System.err.print("[Database] Error closing database");
            if(throwRuntimeExceptions)
                throw new DatabaseSaveException("Failed to save database during close", e);
        }

        cachedValues.clear();
    }

    private String readToString(File file) {
        try {
            byte[] bytes = Files.readAllBytes(file.toPath());
            return new String(bytes);
        } catch (IOException e) {
            System.err.print("[Database] Error reading value from file");
            if(throwRuntimeExceptions)
                throw new DatabaseLoadException("Failed to read value from file", e);
        }

        return null;
    }

    class CachedValue {
        private final V value;
        private boolean isSaved = false;
        private final long addDate = System.currentTimeMillis();

        CachedValue(V value) {
            this.value = value;
        }
    }
}
