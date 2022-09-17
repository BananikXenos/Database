
# Database

Simple [hashcode](https://www.baeldung.com/java-hashcode) & [gson](https://github.com/google/gson) based database library, with [encryption](https://en.wikipedia.org/wiki/Encryption) and exception handling.


## Roadmap

- More features
- Optimalization
- Wrong encryption detection


## Features

- Simple Encryption & No Encryption
- Interface for custom encryption (IEncryption)
- Caching
- Unloading cache - Use Database#clearCache();
- Database builder with settings
- Runtime exceptions
- Default value with Database#getOrElse(key, defaultValue);



## Donate

- [Paypal](https://www.paypal.com/paypalme/scgxenos) Donate would be cool :)
## Usage/Examples

Simple usage of encrypted Database that keeps cached values for 3 seconds and auto-saves after modifying value
```java
// Create the database directory
new File("databaseEncrypted").mkdirs();
// Create the Database with DatabaseBuilder
Database<String, String> database = new DatabaseBuilder<String, String>("databaseEncrypted")
        .withEncryption(new SimpleEncryption("xU711Td8uL3D3O67!p$K7$5nLlea#kVZ"))
        .cacheKeepTime(3_000L)
        .autoSave()
        .build();

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
```


## Authors

- [BananikXenos](https://github.com/BananikXenos)


## License

[MIT](https://choosealicense.com/licenses/mit/)

