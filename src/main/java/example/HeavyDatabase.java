package example;

import xyz.synse.database.core.base.Database;
import xyz.synse.database.core.base.builders.DatabaseBuilder;

public class HeavyDatabase {
    public static void main(String[] args) throws InterruptedException {
        Database database = new DatabaseBuilder()
                .cachedFileDatabase()
                .withFile( "database.ldb")
                .withSimpleEncryption("tiyao5XJ2e^Cr5u81SK^Qt1p13HpG&9Z")
                .build();

        int count = 10_000;
        // Set some values
        for(int i = 0; i < count; i++){
            String str1 = getAlphaNumericString(1);
            String str2 = getAlphaNumericString(10);
            database.set(str1, str2);
        }

        Thread.sleep(1_000L);

        // Save all values
        long start = System.currentTimeMillis();
        database.close();
        System.out.println("Database saving took " + (System.currentTimeMillis() - start) + "ms");
    }

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
