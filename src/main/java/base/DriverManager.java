package base;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.flutter.FlutterFinder;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.net.URL;
import java.time.Duration;

public class DriverManager {
    private static ThreadLocal<AppiumDriver> driver = new ThreadLocal<>();
    private static final String APPIUM_SERVER_URL = "http://127.0.0.1:4723/wd/hub";
    
    public static void initializeDriver(DesiredCapabilities capabilities) {
        try {
            AppiumDriver appiumDriver = new AndroidDriver(new URL(APPIUM_SERVER_URL), capabilities);
            appiumDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
            driver.set(appiumDriver);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize driver", e);
        }
    }
    
    public static AppiumDriver getDriver() {
        return driver.get();
    }
    
    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
    
    public static FlutterFinder getFlutterFinder() {
        return new FlutterFinder(getDriver());
    }
}