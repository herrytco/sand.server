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

    public HashMap<String, Object> getStorage() throws IOException {
        return getStorage("data.json");
    }

    public HashMap<String, Object> getStorage(String storageFileName) throws IOException {
        File storage = resolve(storageFileName);

        String data = Files.readString(storage.toPath());

        if (data.length() == 0)
            data = "{}";

        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(data, HashMap.class);
    }

    public void storeStorage(HashMap<String, Object> data) throws IOException {
        storeStorage(data, "data.json");
    }

    public void storeStorage(HashMap<String, Object> data, String storageFileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(data);

        System.out.println("content: " + json);

        File storage = resolve(storageFileName);
        Files.write(storage.toPath(), json.getBytes());
    }

    public void putKeyValuePair(String key, Object value, String storageFileName) throws IOException {
        HashMap<String, Object> data = getStorage(storageFileName);
        data.put(key, value);
        storeStorage(data, storageFileName);
    }

    public void putKeyValuePair(String key, Object value) throws IOException {
        putKeyValuePair(key, value, "data.json");
    }

    public Object getValue(String key) throws IOException {
        return getValue(key, "data.json");
    }

    public Object getValue(String key, String storageFileName) throws IOException {
        HashMap<String, Object> data = getStorage(storageFileName);

        return data.get(key);
    }

    public Object deleteKey(String key) throws IOException {
        return deleteKey(key, "data.json");
    }

    public Object deleteKey(String key, String storageFileName) throws IOException {
        HashMap<String, Object> data = getStorage(storageFileName);
        Object value = data.remove(key);
        storeStorage(data, storageFileName);

        return value;
    }
}
