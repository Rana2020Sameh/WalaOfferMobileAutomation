package utils;


import framework.base.DriverManager;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.Dimension;

public class DeviceUtils {
    private static AppiumDriver driver = DriverManager.getDriver();
    
    public static void rotateToLandscape() {
        driver.rotate(ScreenOrientation.LANDSCAPE);
        FlutterUtils.waitForFlutterToSettle();
    }
    
    public static void rotateToPortrait() {
        driver.rotate(ScreenOrientation.PORTRAIT);
        FlutterUtils.waitForFlutterToSettle();
    }
    
    public static Dimension getScreenSize() {
        return driver.manage().window().getSize();
    }
    
    public static void hideKeyboard() {
        try {
            driver.hideKeyboard();
        } catch (Exception e) {
            // Keyboard might not be visible
            System.out.println("Keyboard not visible or could not be hidden");
        }
    }
    
    public static void openNotifications() {
        driver.openNotifications();
    }
    
    public static void pressBack() {
        driver.navigate().back();
    }
    
    public static void launchApp() {
        driver.launchApp();
    }
    
    public static void closeApp() {
        driver.closeApp();
    }
    
    public static void resetApp() {
        driver.resetApp();
    }
    
    public static boolean isKeyboardShown() {
        try {
            return driver.isKeyboardShown();
        } catch (Exception e) {
            return false;
        }
    }
    
    public static void setNetworkConnection(boolean wifi, boolean data) {
        // This is a placeholder - actual implementation depends on device capabilities
        System.out.println("Setting network - WiFi: " + wifi + ", Data: " + data);
    }
}
