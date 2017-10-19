package android.os;

/**
 * Framework stub api.
 */
public class SystemProperties {

    public static String get(String key) {
        throw new RuntimeException("Stub");
    }

    public static String get(String key, String def) {
        throw new RuntimeException("Stub");
    }

    public static int getInt(String key, int def) {
        throw new RuntimeException("Stub");
    }

    public static long getLong(String key, long def) {
        throw new RuntimeException("Stub");
    }

    public static boolean getBoolean(String key, boolean def) {
        throw new RuntimeException("Stub");
    }

    public static void set(String key, String val) {
        throw new RuntimeException("Stub");
    }

    public static void addChangeCallback(Runnable callback) {
        throw new RuntimeException("Stub");
    }

    public static void reportSyspropChanged() {
        throw new RuntimeException("Stub");
    }
}
