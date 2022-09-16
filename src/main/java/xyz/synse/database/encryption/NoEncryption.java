package xyz.synse.database.encryption;

public class NoEncryption implements IEncryption {
    @Override
    public String encrypt(String original) {
        return original;
    }

    @Override
    public String decrypt(String encrypted) {
        return encrypted;
    }
}
