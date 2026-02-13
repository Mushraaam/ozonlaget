package no.uib.inf112.config;

import java.io.InputStream;
import java.util.Properties;

public final class Config {

    private static final Properties PROPS = new Properties();
    static {
        try (InputStream in =
                     Config.class.getResourceAsStream("/no/uib/inf112/config/config.properties")) {

            if (in == null) {
                throw new RuntimeException("config.properties not found");
            }
            PROPS.load(in);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private Config() {}

    public static String get(String key) {
        String value = PROPS.getProperty(key);
        if (value == null){
            throw new IllegalStateException(key + " not found in config.properties");
        }
        return value;
    }


    public static int getInt(String key) {
        String value = PROPS.getProperty(key);
        if (value == null){
            throw new IllegalStateException(key + " not found in config.properties");
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Make sure config key is an int. Got "+key);
    }
    //More methods here if we need to fetch different value types. One getter will be kinda against type safety here.
}
}
