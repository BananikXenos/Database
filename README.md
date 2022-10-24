# Database

Simple database library, with [encryption](https://en.wikipedia.org/wiki/Encryption), [serialization](https://en.wikipedia.org/wiki/Serialization) and exception handling.

## Roadmap

- More features
- Optimization
- Wrong encryption detection
- One file database

## Features

- Optional Encryption
- Interface for custom encryption (IEncryption)
- Caching
- Unloading cache - Use Database#clearCache();
- Database builder with settings
- Runtime exceptions
- Default value with Database#getOrElse(key, defaultValue);
- Serialization
- Interface for custom serialization (ISerialization)

## Donate

- [PayPal](https://www.paypal.com/paypalme/scgxenos) Donate would be cool :)

## Usage/Examples

Simple usage of encrypted Database that keeps cached values for 3 seconds and auto-saves after modifying value

```java
package example.cached.objects;

import example.User;
import xyz.synse.database.Database;
import xyz.synse.database.encryption.SimpleEncryption;
import xyz.synse.database.serialization.JavaSerialization;
import xyz.synse.database.store.CachedFileStore;

import java.util.Random;

public class SimpleEncryptObjectExample {
    public static void main(String[] args) throws InterruptedException {
        Database database = new Database(new CachedFileStore("database.dat", new JavaSerialization(), new SimpleEncryption("xU711Td8uL3D3O67!p$K7$5nLlea#kVZ")));

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

```

## Authors

- [BananikXenos](https://github.com/BananikXenos)

## License

[MIT](https://choosealicense.com/licenses/mit/)

