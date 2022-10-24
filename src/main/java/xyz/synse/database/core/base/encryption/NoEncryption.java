package xyz.synse.database.core.base.encryption;

import xyz.synse.database.core.abstracts.IEncryption;

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
