package example;

import xyz.synse.database.core.base.Database;
import xyz.synse.database.core.base.encryption.SimpleEncryption;
import xyz.synse.database.core.base.builders.DatabaseBuilder;

import java.util.Random;

public class SimpleExample {
    public static void main(String[] args) throws InterruptedException {
        Database database = new DatabaseBuilder()
                .cachedFileDatabase()
                .withFile("database.ldb")
                .withSimpleEncryption("tiyao5XJ2e^Cr5u81SK^Qt1p13HpG&9Z")
                .build();

        // Set some values
        database.set("John", new User((int)(new Random().nextDouble() * 100D), "€"));
        database.set("Eva", new User((int)(new Random().nextDouble() * 100D), "¥"));
        database.set("Bob", new User((int)(new Random().nextDouble() * 100D), "₩"));

        Thread.sleep(4_000L);

        // Clears cached values that are in memory longer than 3000 millis
        database.save();

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
