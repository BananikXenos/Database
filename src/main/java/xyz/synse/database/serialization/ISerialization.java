package xyz.synse.database.serialization;

public interface ISerialization {
    String serialize(Object object) throws Exception;
    Object deserialize(String serialized) throws Exception;
}
