package xyz.synse.database.core.base.utils;

public class Constant {
    public static final String KEY_VALUE_CHARACTERS = " -=- ";

    public static String computeKey(Object key){
        int hash = key.hashCode();
        return hash + "" + key.getClass().getName();
    }
}
