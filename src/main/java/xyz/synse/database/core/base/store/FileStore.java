package xyz.synse.database.core.base.store;

import xyz.synse.database.core.abstracts.IEncryption;
import xyz.synse.database.core.abstracts.IStore;
import xyz.synse.database.core.base.encryption.NoEncryption;
import xyz.synse.database.core.abstracts.ISerialization;
import xyz.synse.database.core.base.utils.Constant;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class FileStore implements IStore {
    private final File dbFile;
    private final ISerialization serialization;
    private final IEncryption encryption;

    public FileStore(File dbFile, ISerialization serialization, IEncryption encryption) {
        this.dbFile = dbFile;
        this.serialization = serialization;
        this.encryption = encryption;
    }

    public FileStore(String dbFile, ISerialization serialization, IEncryption encryption) {
        this.dbFile = new File(dbFile);
        this.serialization = serialization;
        this.encryption = encryption;
    }

    public FileStore(File dbFile, ISerialization serialization) {
        this.dbFile = dbFile;
        this.serialization = serialization;
        this.encryption = new NoEncryption();
    }

    public FileStore(String dbFile, ISerialization serialization) {
        this.dbFile = new File(dbFile);
        this.serialization = serialization;
        this.encryption = new NoEncryption();
    }

    @Override
    public Object get(String key) {
        if (!dbFile.exists())
            return null;

        try (BufferedReader br = new BufferedReader(new FileReader(dbFile))) {
            for (String line; (line = br.readLine()) != null; ) {
                line = line.replaceAll("\n", "");
                String[] vLine = line.split(Constant.KEY_VALUE_CHARACTERS);
                String vKey = vLine[0];
                String vVal = vLine[1];

                if (Objects.equals(vKey, key)) {
                    return serialization.deserialize(encryption.decrypt(vVal));
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

    }

    @Override
    public void clearAll() {

    }

    @Override
    public void close() {

    }
}
