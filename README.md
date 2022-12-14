# Database

Simple file or in memory database library, with [encryption](https://en.wikipedia.org/wiki/Encryption), [serialization](https://en.wikipedia.org/wiki/Serialization).

## Roadmap

- More features
- Wrong encryption detection

## Features

- Optional Encryption - for custom use IEncryption
- Serialization - for custom use ISerialization
- Multiple Database Stores
- Default value with Database#getOrElse(key, defaultValue);
- Set value if missing with Database#getOrSet(key, defaultValue);
- Database builder 

## Stores
- Memory Store
- File Store
- Cached File Store
- For custom one use IStore

## Donate

- [PayPal](https://www.paypal.com/paypalme/scgxenos) Donate would be cool :)

## Usage/Examples

Simple usage of encrypted Database that keeps cached values for 3 seconds and auto-saves after modifying value

```java
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
```

## Authors

- [BananikXenos](https://github.com/BananikXenos)

## License

[MIT](https://choosealicense.com/licenses/mit/)

