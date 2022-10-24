package xyz.synse.database.store;

import xyz.synse.database.encryption.IEncryption;
import xyz.synse.database.encryption.NoEncryption;
import xyz.synse.database.serialization.ISerialization;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class FileStore extends Store {
    private final ISerialization serialization;
    private final IEncryption encryption;
    private static final String keyValueCharacters = " :=; ";

    public FileStore(File dbFile, ISerialization serialization, IEncryption encryption) {
        super(dbFile);
        this.serialization = serialization;
        this.encryption = encryption;
    }

    public FileStore(String dbFile, ISerialization serialization, IEncryption encryption) {
        super(new File(dbFile));
        this.serialization = serialization;
        this.encryption = encryption;
    }

    public FileStore(File dbFile, ISerialization serialization) {
        super(dbFile);
        this.serialization = serialization;
        this.encryption = new NoEncryption();
    }

    public FileStore(String dbFile, ISerialization serialization) {
        super(new File(dbFile));
        this.serialization = serialization;
        this.encryption = new NoEncryption();
    }

    @Override
    public Object read(String key) {
        if (!dbFile.exists())
            return null;

        try (BufferedReader br = new BufferedReader(new FileReader(dbFile))) {
            for (String line; (line = br.readLine()) != null; ) {
                line = line.replaceAll("\n", "");
                String[] vLine = line.split(keyValueCharacters);
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
    public void save(String key, Object object) {
        try {
            if (!dbFile.exists())
                dbFile.createNewFile();

            FileReader fr = new FileReader(dbFile);
            BufferedReader br = new BufferedReader(fr);

            ArrayList<String> lines = new ArrayList<>();
            boolean hasOld = false;
            for (String line; (line = br.readLine()) != null; ) {
                String[] vLine = line.split(keyValueCharacters);
                String vKey = vLine[0];

                if (Objects.equals(vKey, key)) {
                    String serialized = encryption.encrypt(serialization.serialize(object));
                    line = vKey + keyValueCharacters + serialized;
                    hasOld = true;
                }

                lines.add(line);
            }
            fr.close();
            br.close();

            if(!hasOld){
                String serialized = encryption.encrypt(serialization.serialize(object));
                lines.add(key + keyValueCharacters + serialized);
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
    public void close() {

    }
}
