package xyz.synse.database.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

public class JavaSerialization implements ISerialization {
    @Override
    public String serialize(Object object) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.flush();
        objectOutputStream.close();

        return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
    }

    @Override
    public Object deserialize(String serialized) throws Exception {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(serialized));
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

        Object object = objectInputStream.readObject();
        objectInputStream.close();

        return object;
    }
}
