package example.simple;

import xyz.synse.database.Database;
import xyz.synse.database.DatabaseBuilder;
import xyz.synse.database.encryption.SimpleEncryption;

import java.io.File;
import java.util.Random;

public class SimpleEncryptExample {
    public static void main(String[] args) throws InterruptedException {
        // Create the database directory
        new File("database").mkdirs();
        // Create the Database with DatabaseBuilder
        Database database = new DatabaseBuilder("database")
                .withEncryption(new SimpleEncryption("xU711Td8uL3D3O67!p$K7$5nLlea#kVZ"))
                .cacheKeepTime(3_000L)
                .autoSave()
                .build();

        // Load old values if there are any
        System.out.println(database.get("John"));
        System.out.println(database.get("Eva"));
        System.out.println(database.get("Bob"));

        // Set some values
        database.set("John", (int)(new Random().nextDouble() * 100D) + " Dollars");
        database.set("Eva", (int)(new Random().nextDouble() * 100D) + " Dollars");
        database.set("Bob", (int)(new Random().nextDouble() * 100D) + " Dollars");

        Thread.sleep(4_000L);

        // Clears cached values that are in memory longer than 3000 millis
        database.clearCache();

        Thread.sleep(1_000L);

        System.out.println(database.get("John"));
        System.out.println(database.get("Eva"));
        System.out.println(database.get("Bob"));

        // Not existing value
        System.out.println(database.getOrElse("Mark", "Alternative"));

        // Save the database and clear cache
        database.close();
    }
}
