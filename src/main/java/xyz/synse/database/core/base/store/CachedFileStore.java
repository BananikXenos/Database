package xyz.synse.database.core.base.store;

import xyz.synse.database.core.abstracts.IEncryption;
import xyz.synse.database.core.abstracts.IStore;
import xyz.synse.database.core.base.encryption.NoEncryption;
import xyz.synse.database.core.abstracts.ISerialization;
import xyz.synse.database.core.base.utils.Constant;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CachedFileStore implements IStore {
    private final File dbFile;
    private final HashMap<String, CachedValue> cachedValues = new HashMap<>();

    private final ISerialization serialization;
    private final IEncryption encryption;

    public CachedFileStore(File dbFile, ISerialization serialization, IEncryption encryption) {
        this.dbFile = dbFile;
        this.serialization = serialization;
        this.encryption = encryption;
    }

    public CachedFileStore(String dbFile, ISerialization serialization, IEncryption encryption) {
        this.dbFile = new File(dbFile);
        this.serialization = serialization;
        this.encryption = encryption;
    }

    public CachedFileStore(File dbFile, ISerialization serialization) {
        this.dbFile = dbFile;
        this.serialization = serialization;
        this.encryption = new NoEncryption();
    }

    public CachedFileStore(String dbFile, ISerialization serialization) {
        this.dbFile = new File(dbFile);
        this.serialization = serialization;
        this.encryption = new NoEncryption();
    }

    @Override
    public Object get(String key) {
        if(cachedValues.containsKey(key))
            return cachedValues.get(key).value;

        if (!dbFile.exists())
            return null;

        try (BufferedReader br = new BufferedReader(new FileReader(dbFile))) {
            for (String line; (line = br.readLine()) != null; ) {
                line = line.replaceAll("\n", "");
                String[] vLine = line.split(Constant.KEY_VALUE_CHARACTERS);
                String vKey = vLine[0];
                String vVal = vLine[1];

                if (Objects.equals(vKey, key)) {
                    Object value = serialization.deserialize(encryption.decrypt(vVal));
                    cachedValues.put(key, new CachedValue(value));

                    return value;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void set(String key, Object object) {
        try {
            if(cachedValues.containsKey(key)){
                CachedValue cV = cachedValues.get(key);
                cV.value = object;
                cV.isSaved = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void saveToFile(String key, Object object) {
        try {
            if (!dbFile.exists())
                dbFile.createNewFile();

            FileReader fr = new FileReader(dbFile);
            BufferedReader br = new BufferedReader(fr);

            ArrayList<String> lines = new ArrayList<>();
            boolean hasOld = false;
            for (String line; (line = br.readLine()) != null; ) {
                String[] vLine = line.split(Constant.KEY_VALUE_CHARACTERS);
                String vKey = vLine[0];

                if (Objects.equals(vKey, key)) {
                    String serialized = encryption.encrypt(serialization.serialize(object));
                    line = vKey + Constant.KEY_VALUE_CHARACTERS + serialized;
                    hasOld = true;
                }

                lines.add(line);
            }
            fr.close();
            br.close();

            if(!hasOld){
                String serialized = encryption.encrypt(serialization.serialize(object));
                lines.add(key + Constant.KEY_VALUE_CHARACTERS + serialized);
            }

            FileWriter fw = new FileWriter(dbFile);
            BufferedWriter out = new BufferedWriter(fw);
            for (String s : lines)
                out.write(s + "\n");
            out.flush();
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void saveAll() {
        for(Map.Entry<String, CachedValue> entry : cachedValues.entrySet()){
            saveToFile(entry.getKey(), entry.getValue().value);
        }
    }

    @Override
    public void clearAll() {
        cachedValues.entrySet().removeIf(entry -> entry.getValue().isSaved);
    }

    @Override
    public void close() {
        saveAll();
        clearAll();
    }

    static class CachedValue {
        private Object value;
        private boolean isSaved = false;

        CachedValue(Object value) {
            this.value = value;
        }
    }
}
