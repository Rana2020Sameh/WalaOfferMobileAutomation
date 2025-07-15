package utils;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.openqa.selenium.Platform;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import static utils.PropertyReader.*;

public class AppiumDriverManager {

    private static AppiumDriver driver;

    public static AppiumDriver getDriver() {
        if (driver == null) {
            setupDriver();
        }
        return driver;
    }

    private static void setupDriver() {
        String platformName = getProperty("platform.name");
        String appiumServerUrl = getProperty("appium.server.url");

        try {
            if (Platform.ANDROID.name().equalsIgnoreCase(platformName)) {
                UiAutomator2Options options = new UiAutomator2Options();
                options.setPlatformName(platformName);
                options.setDeviceName(getProperty("device.name"));
                options.setApp(getProperty("android.app.path"));
                options.setAutomationName(getProperty("automation.name")); // Crucial for Flutter
                options.setAppPackage(getProperty("android.app.package"));
                options.setAppActivity(getProperty("android.app.activity"));
                options.setNoReset(getBooleanProperty("no.reset"));
                options.setFullReset(getBooleanProperty("full.reset"));
                // Add more Android-specific capabilities if needed

                driver = new AndroidDriver(new URL(appiumServerUrl), options);

            } else if (Platform.IOS.name().equalsIgnoreCase(platformName)) {
                XCUITestOptions options = new XCUITestOptions();
                options.setPlatformName(platformName);
                options.setDeviceName(getProperty("device.name"));
                options.setApp(getProperty("ios.app.path"));
                options.setAutomationName(getProperty("automation.name")); // Crucial for Flutter
                options.setBundleId(getProperty("ios.bundle.id"));
                options.setNoReset(getBooleanProperty("no.reset"));
                options.setFullReset(getBooleanProperty("full.reset"));
                // Add more iOS-specific capabilities if needed

                driver = new IOSDriver(new URL(appiumServerUrl), options);
            } else {
                throw new IllegalArgumentException("Unsupported platform: " + platformName);
            }

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(getIntProperty("implicit.wait.timeout")));

        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid Appium Server URL: " + appiumServerUrl, e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up Appium driver: " + e.getMessage(), e);
        }
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null; // Reset driver instance
        }
    }
}