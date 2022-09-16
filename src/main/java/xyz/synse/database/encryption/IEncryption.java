package xyz.synse.database.encryption;

public interface IEncryption {
    String encrypt(String original);
    String decrypt(String encrypted);
}
