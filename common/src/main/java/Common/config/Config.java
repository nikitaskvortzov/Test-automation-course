package Common.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Config {

    private static final String PROPS_FILE = "config.properties";
    private static final Properties PROPS = loadProperties();

    private static final String BASE_URL =
            requiredProperty("base.url");

    private static final String BASE_API =
            requiredProperty("base.api");

    private static final int TIMEOUT_FIND_ELEMENTS =
            Integer.parseInt(
                    PROPS.getProperty(
                            "timeout.findElements",
                            "10000"
                    )
            );

    private static final String LOGGING_MODE =
            PROPS.getProperty("logging.mode", "NONE");

    private static final String ADMIN_USERNAME =
            PROPS.getProperty("admin.username");

    private static final String ADMIN_PASSWORD =
            PROPS.getProperty("admin.password");

    private static final String START_PRODUCT_NAME =
            PROPS.getProperty("startProduct.name");

    private static final String START_PRODUCT_NAME_2 =
            PROPS.getProperty("startProduct.name2");

    private static final String START_PRODUCT_NAME_3 =
            PROPS.getProperty("startProduct.name3");

    private static final String START_PRODUCT_PRICE =
            PROPS.getProperty("startProduct.price");

    private static final String START_PRODUCT_PRICE_2 =
            PROPS.getProperty("startProduct.price2");

    private static final String START_PRODUCT_PRICE_3 =
            PROPS.getProperty("startProduct.price3");

    private Config() {
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();

        try (InputStream inputStream =
                     Config.class
                             .getClassLoader()
                             .getResourceAsStream(PROPS_FILE)) {

            if (inputStream == null) {
                throw new IllegalStateException(
                        "Config file not found in classpath: "
                                + PROPS_FILE
                );
            }

            properties.load(inputStream);
            return properties;

        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Failed to load " + PROPS_FILE,
                    exception
            );
        }
    }

    private static String requiredProperty(String key) {
        String value = PROPS.getProperty(key);

        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Required property is missing or empty: " + key
            );
        }

        return value;
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static String getBaseApi() {
        return BASE_API;
    }

    public static int getTimeoutFindElements() {
        return TIMEOUT_FIND_ELEMENTS;
    }

    public static String getLoggingMode() {
        return LOGGING_MODE;
    }

    public static String getAdminUsername() {
        return ADMIN_USERNAME;
    }

    public static String getAdminPassword() {
        return ADMIN_PASSWORD;
    }

    public static String getStartProductName() {
        return START_PRODUCT_NAME;
    }

    public static String getStartProductName2() {
        return START_PRODUCT_NAME_2;
    }

    public static String getStartProductName3() {
        return START_PRODUCT_NAME_3;
    }

    public static String getStartProductPrice() {
        return START_PRODUCT_PRICE;
    }

    public static String getStartProductPrice2() {
        return START_PRODUCT_PRICE_2;
    }

    public static String getStartProductPrice3() {
        return START_PRODUCT_PRICE_3;
    }
}
