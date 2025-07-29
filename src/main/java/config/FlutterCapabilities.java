package config;

import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.android.options.UiAutomator2Options;

import java.io.File;

public class FlutterCapabilities {

    public static DesiredCapabilities getAndroidCapabilities() {

        // Replaced deprecated MobileCapabilityType with modern UiAutomator2Options or
        // DesiredCapabilities but UiAutomator2Options is recommended
        UiAutomator2Options options = new UiAutomator2Options();
        // // Basic Android capabilities
        options.setPlatformName("Android");
        options.setPlatformVersion(ConfigManager.getProperty("android.version")); // Use platform version from config
        options.setDeviceName(ConfigManager.getProperty("device.name")); // Use device name from config
        options.setAppPackage(ConfigManager.getProperty("app.package")); // Use app package from config
        options.setAppActivity(ConfigManager.getProperty("app.activity")); // Use app activity from config
        options.setAutomationName("Flutter");
        options.setApp("/path/to/flutter-app.apk");
        // App configuration
        String apkPath = System.getProperty("user.dir") +
                "/src/test/resources/apk/app-qa-release-universal.apk";
        options.setCapability("app", new File(apkPath).getAbsolutePath());
        // Additional Flutter-specific options
        options.setCapability("shouldUseCompactResponses", false);
        options.setCapability("elementResponseAttributes", "type,label");

        options.setCapability("autoGrantPermissions", true);
        options.setCapability("noReset", false);
        options.setCapability("fullReset", false);
        options.setCapability("newCommandTimeout", 300);

        // Performance capabilities
        options.setCapability("skipServerInstallation", true);
        options.setCapability("skipDeviceInitialization", true);

        return new DesiredCapabilities(options.asMap());
    }
}