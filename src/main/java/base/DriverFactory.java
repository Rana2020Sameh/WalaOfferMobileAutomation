package base;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.flutter.FlutterDriverOptions;
import io.appium.java_client.flutter.SupportsGestureOnFlutterElements;
import io.appium.java_client.flutter.android.FlutterAndroidDriver;

import java.net.URL;

public class DriverFactory {
    public static AndroidDriver driver;
    //public static FlutterFinder flutterFinder;
    public static FlutterDriverOptions flutterDriverOptions;
    public static FlutterAndroidDriver flutterAndroidDriver;


    public static SupportsGestureOnFlutterElements getFlutterDriver() {
        if (flutterAndroidDriver == null) {
            throw new IllegalStateException("Flutter driver is not initialized. Call initialize() first.");
        }
        return flutterAndroidDriver;
    }

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

      //  driver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), options);
        flutterAndroidDriver= new FlutterAndroidDriver(new URL("http://localhost:4723/wd/hub"), options);
    }

    public static void quit() {
        if (flutterAndroidDriver != null) {
            flutterAndroidDriver.quit();
        }

    }
}