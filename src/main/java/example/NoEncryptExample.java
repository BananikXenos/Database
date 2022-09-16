package example;

import xyz.synse.database.Database;

import java.io.File;
import java.util.UUID;

public class NoEncryptExample {
    public static void main(String[] args) {
        Database<UUID, String> database = new Database<>(new File("database.db"));
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
