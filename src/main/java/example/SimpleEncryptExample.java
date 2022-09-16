package example;

import xyz.synse.database.Database;
import xyz.synse.database.encryption.SimpleEncryption;

import java.io.File;
import java.util.UUID;

public class SimpleEncryptExample {
    public static void main(String[] args) {
        // Key from https://www.lastpass.com/features/password-generator
        Database<UUID, String> database = new Database<>(new File("databaseEnc.db"), new SimpleEncryption("xU711Td8uL3D3O67!p$K7$5nLlea#kVZ"));
        database.load();

        for(int i = 0; i < 10; i++) {
            database.set(UUID.randomUUID(), getAlphaNumericString(20));
        }

        database.close();
    }

    // From https://www.geeksforgeeks.org/generate-random-string-of-given-size-in-java/
    static String getAlphaNumericString(int n)
    {

        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }
}
