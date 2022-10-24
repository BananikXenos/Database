package example.cached.simple;

import xyz.synse.database.Database;
import xyz.synse.database.encryption.NoEncryption;
import xyz.synse.database.serialization.JavaSerialization;
import xyz.synse.database.store.CachedFileStore;

import java.util.Random;

public class NoEncryptExample {
    public static void main(String[] args) throws InterruptedException {
        Database database = new Database(new CachedFileStore("database.dat", new JavaSerialization(), new NoEncryption()));

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
        database.save();

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
