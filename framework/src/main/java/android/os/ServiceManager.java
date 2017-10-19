package android.os;

import java.util.Map;

/**
 * Framework stub api.
 */
public final class ServiceManager {

    public static IBinder getService(String name) {
        throw new RuntimeException("Stub");
    }


    public static IBinder getServiceOrThrow(String name) throws ServiceNotFoundException {
        throw new RuntimeException("Stub");
    }


    public static void addService(String name, IBinder service) {
        throw new RuntimeException("Stub");
    }


    public static void addService(String name, IBinder service, boolean allowIsolated) {
        throw new RuntimeException("Stub");
    }


    public static IBinder checkService(String name) {
        throw new RuntimeException("Stub");
    }


    public static String[] listServices() {
        throw new RuntimeException("Stub");
    }


    public static void initServiceCache(Map<String, IBinder> cache) {
        throw new RuntimeException("Stub");
    }


    public static class ServiceNotFoundException extends Exception {
        public ServiceNotFoundException(String name) {
            super("No service published for: " + name);
        }
    }
}
