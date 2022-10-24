package xyz.synse.database.core.abstracts;

public interface ISerialization {
    String serialize(Object object) throws Exception;
    Object deserialize(String serialized) throws Exception;
}
