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

    public static void progressPercentage(int done, int total) {
        int size = 50;
        String iconLeftBoundary = "[";
        String iconDone = "#";
        String iconRemain = "-";
        String iconRightBoundary = "]";

        if (done > total) {
            throw new IllegalArgumentException();
        }
        int donePercents = (100 * done) / total;
        int doneLength = size * donePercents / 100;

        StringBuilder bar = new StringBuilder(iconLeftBoundary);
        for (int i = 0; i < size; i++) {
            if (i < doneLength) {
                bar.append(iconDone);
            } else {
                bar.append(iconRemain);
            }
        }
        bar.append(iconRightBoundary);

        System.out.print("\r" + bar + " " + donePercents + "%");

        if (done == total) {
            System.out.print("\n");
        }
    }

    @Override
    public Object get(String key) {
        if (cachedValues.containsKey(key))
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
            if (cachedValues.containsKey(key)) {
                CachedValue cV = cachedValues.get(key);
                cV.value = object;
                cV.isSaved = true;
            } else {
                cachedValues.put(key, new CachedValue(object));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void saveAll() {
        try {
            if (!dbFile.exists())
                dbFile.createNewFile();

            FileReader fr = new FileReader(dbFile);
            BufferedReader br = new BufferedReader(fr);

            int progress = 0;

            ArrayList<String> added = new ArrayList<>();
            ArrayList<String> lines = new ArrayList<>();
            for (String line; (line = br.readLine()) != null; ) {
                String[] vLine = line.split(Constant.KEY_VALUE_CHARACTERS);
                String vKey = vLine[0];

                for (Map.Entry<String, CachedValue> entry : cachedValues.entrySet()) {
                    if (Objects.equals(vKey, entry.getKey())) {
                        String serialized = encryption.encrypt(serialization.serialize(entry.getValue().value));
                        line = vKey + Constant.KEY_VALUE_CHARACTERS + serialized;
                        added.add(entry.getKey());

                        progressPercentage(progress++, cachedValues.size() - 1);
                        break;
                    }
                }

                lines.add(line);
            }
            fr.close();
            br.close();

            for (Map.Entry<String, CachedValue> entry : cachedValues.entrySet()) {
                if (!added.contains(entry.getKey())) {
                    String serialized = encryption.encrypt(serialization.serialize(entry.getValue().value));
                    lines.add(entry.getKey() + Constant.KEY_VALUE_CHARACTERS + serialized);

                    progressPercentage(progress++, cachedValues.size() - 1);
                }
            }

            FileWriter fw = new FileWriter(dbFile);
            BufferedWriter out = new BufferedWriter(fw);
            for (String s : lines)
                out.write(s + "\n");
            out.flush();
            out.close();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
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
