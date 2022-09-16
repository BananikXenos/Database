package xyz.synse.database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import xyz.synse.database.encryption.IEncryption;
import xyz.synse.database.encryption.NoEncryption;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

public class Database<K, V> {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final File file;
    private HashMap<K, V> data = new HashMap<>();

    private IEncryption encryption;

    public Database(File file) {
        this.file = file;
        this.encryption = new NoEncryption();
    }

    public Database(File file, IEncryption encryption) {
        this.file = file;
        this.encryption = encryption;
    }

    public void load() {
        if (!file.exists())
            return;

        try {
            this.data = gson.fromJson(encryption.decrypt(readToString()), HashMap.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public V get(K key) {
        return data.get(key);
    }

    public boolean containsKey(K key) {
        return data.containsKey(key);
    }

    public boolean containsValue(V value) {
        return data.containsValue(value);
    }

    public void set(K key, V value) {
        this.data.put(key, value);
    }

    public void remove(K key) {
        this.data.remove(key);
    }

    public void remove(K key, V value) {
        this.data.remove(key, value);
    }

    public int size(){
        return this.data.size();
    }

    public void setEncryption(IEncryption encryption) {
        this.encryption = encryption;
    }

    public IEncryption getEncryption() {
        return encryption;
    }

    public boolean close() {
        if (save()) {
            this.data.clear();
            return true;
        }
        return true;
    }

    public boolean save() {
        try {
            if (!file.exists())
                file.createNewFile();

            FileWriter fw = new FileWriter(file, false);
            fw.write(encryption.encrypt(gson.toJson(data)));
            fw.flush();
            fw.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }

    private String readToString() {
        try {
            byte[] bytes = Files.readAllBytes(file.toPath());
            return new String(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
