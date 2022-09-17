package example.objects;

import xyz.synse.database.Database;
import xyz.synse.database.DatabaseBuilder;
import xyz.synse.database.encryption.SimpleEncryption;

import java.io.File;
import java.util.Random;

public class SimpleEncryptObjectExample {
    public static void main(String[] args) throws InterruptedException {
        // Create the database directory
        new File("database").mkdirs();
        // Create the Database with DatabaseBuilder
        Database database = new DatabaseBuilder("database")
                .withEncryption(new SimpleEncryption("xU711Td8uL3D3O67!p$K7$5nLlea#kVZ"))
                .cacheKeepTime(3_000L)
                .autoSave()
                .build();

        // Set some values
        database.set("John", new User((int)(new Random().nextDouble() * 100D), "€"));
        database.set("Eva", new User((int)(new Random().nextDouble() * 100D), "¥"));
        database.set("Bob", new User((int)(new Random().nextDouble() * 100D), "₩"));

        Thread.sleep(4_000L);

        // Clears cached values that are in memory longer than 3000 millis
        database.clearCache();

        Thread.sleep(1_000L);

        System.out.println(((User)database.get("John")).getBalance() + ((User)database.get("John")).getCurrency());
        System.out.println(((User)database.get("Eva")).getBalance() + ((User)database.get("John")).getCurrency());
        System.out.println(((User)database.get("Bob")).getBalance() + ((User)database.get("John")).getCurrency());

        // Not existing value
        User alternative = (User) database.getOrElse("Mark", new User(0, "€"));
        System.out.println(alternative.getBalance() + alternative.getCurrency());

        // Save the database and clear cache
        database.close();
    }
}
