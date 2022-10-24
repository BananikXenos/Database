package xyz.synse.database.core.abstracts;

public interface IEncryption {
    String encrypt(String original);
    String decrypt(String encrypted);
}
