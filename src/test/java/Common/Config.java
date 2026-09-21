package Common;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config { private static final String PROPS_FILE = "/Common/config.properties";
    private static final Properties PROPS = new Properties();

    private static String baseUrl;
    private static String baseApi;
    private static int timeoutFindElements;
    private static String loggingMode;
    private static String adminUsername;
    private static String adminPassword;
    private static String startProductName;
    private static String startProductName2;
    private static String startProductName3;
    private static String startProductPrice;
    private static String startProductPrice2;
    private static String startProductPrice3;

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
        startProductName2 = PROPS.getProperty("startProduct.name2");
        startProductName3 = PROPS.getProperty("startProduct.name3");
        startProductPrice = PROPS.getProperty("startProduct.price");
        startProductPrice2 = PROPS.getProperty("startProduct.price2");
        startProductPrice3 = PROPS.getProperty("startProduct.price3");

        System.out.println("Config:");
        System.out.println("  baseUrl=" + baseUrl);
        System.out.println("  baseApi=" + baseApi);
        System.out.println("  timeoutFindElements=" + timeoutFindElements);
        System.out.println("  loggingMode=" + loggingMode);
        System.out.println("  startProduct.name=" + startProductName);
        System.out.println("  startProduct.name2=" + startProductName2);
        System.out.println("  startProduct.name2=" + startProductName3);
        System.out.println("  startProduct.price=" + startProductPrice);
        System.out.println("  startProduct.price=" + startProductPrice2);
        System.out.println("  startProduct.price=" + startProductPrice3);
    }

    public static String getBaseUrl() { return baseUrl; }
    public static String getBaseApi() { return baseApi; }
    public static int getTimeoutFindElements() { return timeoutFindElements; }
    public static String getLoggingMode() { return loggingMode; }
    public static String getAdminUsername() { return adminUsername; }
    public static String getAdminPassword() { return adminPassword; }
    public static String getStartProductName() { return startProductName; }
    public static String getStartProductPrice() { return startProductPrice; }
    public static String getStartProductName2() { return startProductName2; }
    public static String getStartProductName3() { return startProductName3; }
    public static String getStartProductPrice2() { return startProductPrice2; }
    public static String getStartProductPrice3() { return startProductPrice3; }
}