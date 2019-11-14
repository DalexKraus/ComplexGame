package at.dalex.grape.resource;

import at.dalex.grape.info.Logger;

import java.util.HashMap;

/**
 * This class was written by dalex on 13.09.2017.
 * You are not permitted to edit this file.
 *
 * @author dalex
 */

public class Assets {

    private static HashMap<String, Object> storage = new HashMap<String, Object>();

    public static void store(Object object, String key) {
        storage.put(key, object);
        Logger.info("Stored object with key '" + key + "'");
    }

    public static <T> T get(String key, Class<T> objectClass) {
        if (storage.containsKey(key)) {
            return objectClass.cast(storage.get(key));
        }
        else {
            Logger.info("Could not find an object with key '" + key + "' in the engine's storage!");
            return null;
        }
    }

    public static void removeObject(String key) {
        storage.remove(key);
    }
}
