package systems.nope.discord.eventlistener.dice.file;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

public abstract class FileManager {

    private final File root;

    public FileManager(String rootDir) {
        root = new File("data/" + rootDir);

        if (root.mkdirs())
            System.out.println("[FileManager] Root folder initialized.");
    }

    /**
     * @param filename - filename of the file to be created
     * @return a file from the subfolder. The file will be created if it doesn't exist.
     */
    public File resolve(String filename) {
        File target = new File(root.getPath() + "/" + filename);

        if (!target.exists()) {
            try {
                if (target.createNewFile())
                    System.out.println("[FileManager] Created file " + target.getAbsolutePath());
            } catch (IOException e) {
                System.out.println("[FileManager] failed to create file " + target.getAbsolutePath());
            }
        }

        return target;
    }

    /**
     * @return a map that is persisted to the filesystem via the storeStorage methods
     * @throws IOException - problem with the filesystem
     */
    public HashMap<String, Object> getStorage() throws IOException {
        return getStorage("data.json");
    }


    /**
     * @param storageFileName - name of the data storage (should end with .json)
     * @return a map that is persisted to the filesystem via the storeStorage methods
     * @throws IOException - problem with the filesystem
     */
    public HashMap<String, Object> getStorage(String storageFileName) throws IOException {
        File storage = resolve(storageFileName);

        String data = Files.readString(storage.toPath());

        if (data.length() == 0)
            data = "{}";

        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(data, HashMap.class);
    }

    /**
     * persists a map in the filesystem
     *
     * @param data - map retrieved from the getStorage methods
     * @throws IOException - problem with the filesystem
     */
    public void storeStorage(HashMap<String, Object> data) throws IOException {
        storeStorage(data, "data.json");
    }

    /**
     * persists a map in the filesystem
     *
     * @param data            - map retrieved from the getStorage methods
     * @param storageFileName - name of the data storage (should end with .json)
     * @throws IOException - problem with the filesystem
     */
    public void storeStorage(HashMap<String, Object> data, String storageFileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(data);

        System.out.println("content: " + json);

        File storage = resolve(storageFileName);
        Files.write(storage.toPath(), json.getBytes());
    }

    /**
     * Adds a value to a datastorage. If it existed before, it will be overwritten.
     *
     * @param key             - String key that identifies the Object
     * @param value           - Value to be stored in the storage
     * @param storageFileName - name of the data storage (should end with .json)
     * @throws IOException - problem with the filesystem
     */
    public void putKeyValuePair(String key, Object value, String storageFileName) throws IOException {
        HashMap<String, Object> data = getStorage(storageFileName);
        data.put(key, value);
        storeStorage(data, storageFileName);
    }

    /**
     * Adds a value to a datastorage. If it existed before, it will be overwritten.
     *
     * @param key   - String key that identifies the Object
     * @param value - Value to be stored in the storage
     * @throws IOException - problem with the filesystem
     */
    public void putKeyValuePair(String key, Object value) throws IOException {
        putKeyValuePair(key, value, "data.json");
    }

    /**
     * @param key - String key that identifies the Object
     * @return data from the storage or null if it is no key-value pair stored.
     * @throws IOException - problem with the filesystem
     */
    public Object getValue(String key) throws IOException {
        return getValue(key, "data.json");
    }

    /**
     * @param key             - String key that identifies the Object
     * @param storageFileName - name of the data storage (should end with .json)
     * @return data from the storage or null if it is no key-value pair stored.
     * @throws IOException - problem with the filesystem
     */
    public Object getValue(String key, String storageFileName) throws IOException {
        HashMap<String, Object> data = getStorage(storageFileName);

        return data.get(key);
    }

    /**
     * Removes a key-value pair from the storage.
     *
     * @param key - String key that identifies the Object
     * @return deleted object or null if no key-value pair is stored
     * @throws IOException - problem with the filesystem
     */
    public Object deleteKey(String key) throws IOException {
        return deleteKey(key, "data.json");
    }

    /**
     * Removes a key-value pair from the storage.
     *
     * @param key             - String key that identifies the Object
     * @param storageFileName - name of the data storage (should end with .json)
     * @return deleted object or null if no key-value pair is stored
     * @throws IOException - problem with the filesystem
     */
    public Object deleteKey(String key, String storageFileName) throws IOException {
        HashMap<String, Object> data = getStorage(storageFileName);
        Object value = data.remove(key);
        storeStorage(data, storageFileName);

        return value;
    }
}
