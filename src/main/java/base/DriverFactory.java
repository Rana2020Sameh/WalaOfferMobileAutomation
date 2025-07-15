package base;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import java.net.URL;

public class DriverFactory {
    public static AndroidDriver driver;
    //public static FlutterFinder flutterFinder;



    public static void initialize() throws Exception {
// Replaced deprecated MobileCapabilityType with modern UiAutomator2Options or DesiredCapabilities but uiAutomator is rocommended
UiAutomator2Options options = new UiAutomator2Options();
        options.setPlatformName("Android");
        options.setDeviceName("emulator-5554"); // or your device name
        options.setAppPackage("your.flutter.app.package");
        options.setAppActivity("your.flutter.app.MainActivity");
        options.setAutomationName("Flutter");
        options.setApp("/path/to/flutter-app.apk");

        // Additional Flutter-specific options
        options.setCapability("shouldUseCompactResponses", false);
        options.setCapability("elementResponseAttributes", "type,label");

        driver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), options);
      //  flutterFinder = new FlutterFinder(driver);
    }

    public static void quit() {
        if (driver != null) {
            driver.quit();
        }

    }
}