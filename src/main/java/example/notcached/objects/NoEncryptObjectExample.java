package example.notcached.objects;

import example.User;
import xyz.synse.database.Database;
import xyz.synse.database.encryption.NoEncryption;
import xyz.synse.database.serialization.JavaSerialization;
import xyz.synse.database.store.FileStore;

import java.util.Random;

public class NoEncryptObjectExample {
    public static void main(String[] args) throws InterruptedException {
        Database database = new Database(new FileStore("database.dat", new JavaSerialization(), new NoEncryption()));

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
