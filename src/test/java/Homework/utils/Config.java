package Homework.utils;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config { private static final String PROPS_FILE = "/config.properties";
    private static final Properties PROPS = new Properties();

    private static String baseUrl;
    private static String baseApi;
    private static int timeoutFindElements;
    private static String loggingMode;
    private static String adminUsername;
    private static String adminPassword;
    private static String startProductName;
    private static String startProductPrice;

    static {
        try (InputStream is = Config.class.getResourceAsStream(PROPS_FILE)) {
            if (is == null) {
                throw new IllegalStateException("Config file not found: " + PROPS_FILE);
            }
            PROPS.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config", e);
        }

        baseUrl = PROPS.getProperty("base.url");
        baseApi = PROPS.getProperty("base.api");
        timeoutFindElements = Integer.parseInt(PROPS.getProperty("timeout.findElements", "10000"));
        loggingMode = PROPS.getProperty("logging.mode", "NONE");
        adminUsername = PROPS.getProperty("admin.username");
        adminPassword = PROPS.getProperty("admin.password");
        startProductName = PROPS.getProperty("startProduct.name");
        startProductPrice = PROPS.getProperty("startProduct.price");

        System.out.println("Config:");
        System.out.println("  baseUrl=" + baseUrl);
        System.out.println("  baseApi=" + baseApi);
        System.out.println("  timeoutFindElements=" + timeoutFindElements);
        System.out.println("  loggingMode=" + loggingMode);
        System.out.println("  startProduct.name=" + startProductName);
        System.out.println("  startProduct.price=" + startProductPrice);
    }

    public static String getBaseUrl() { return baseUrl; }
    public static String getBaseApi() { return baseApi; }
    public static int getTimeoutFindElements() { return timeoutFindElements; }
    public static String getLoggingMode() { return loggingMode; }
    public static String getAdminUsername() { return adminUsername; }
    public static String getAdminPassword() { return adminPassword; }
    public static String getStartProductName() { return startProductName; }
    public static String getStartProductPrice() { return startProductPrice; }

}