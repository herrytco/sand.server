package systems.nope.discord.eventlistener.dice;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Secret {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    public static String now() {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        return sdf.format(timestamp);
    }

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    private String key;
    private String time;
    private String value;

    public Secret() {}

    public Secret(String message) {
        value = message;
        time = now();
        String keymaterial = String.format("%d-%s", System.currentTimeMillis(), message);
        String generatedKey = "";

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(keymaterial.getBytes());
            generatedKey = bytesToHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            generatedKey = "";
            System.out.println("Algorithm 'MD5' was not found.");
        }

        key = generatedKey;
    }

    public String getKey() {
        return key;
    }

    public String getTime() {
        return time;
    }

    public String getValue() {
        return value;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Secret{" +
                "key='" + key + '\'' +
                ", time='" + time + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
