package xyz.synse.database.serialization;

import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.azrael.ReadException;
import ca.uqac.lif.azrael.json.JsonPrinter;
import ca.uqac.lif.azrael.json.JsonStringReader;
import ca.uqac.lif.json.JsonElement;
import xyz.synse.database.exceptions.DatabaseLoadException;

public class AzraelSerialization implements ISerialization {
    @Override
    public String serialize(Object object) {
        JsonPrinter printer = new JsonPrinter();
        JsonElement element;
        try {
            element = printer.print(object);
        } catch (PrintException e) {
            throw new DatabaseLoadException("[Database] Failed to save value", e);
        }
        return element.toString();
    }

    @Override
    public Object deserialize(String serialized) {
        JsonStringReader d = new JsonStringReader();
        try {
            return d.read(serialized);
        } catch (ReadException e) {
            throw new DatabaseLoadException("[Database] Failed to load value", e);
        }
    }
}
