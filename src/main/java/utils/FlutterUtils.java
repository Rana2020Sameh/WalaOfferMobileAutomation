package utils;


import framework.base.DriverManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.flutter.FlutterFinder;
import org.openqa.selenium.WebElement;
import java.time.Duration;

public class FlutterUtils {
    private static AppiumDriver driver = DriverManager.getDriver();
    private static FlutterFinder finder = DriverManager.getFlutterFinder();
    
    public static void waitForFlutterWidget(String key, String value, int timeoutSeconds) {
        long startTime = System.currentTimeMillis();
        long timeout = timeoutSeconds * 1000;
        
        while (System.currentTimeMillis() - startTime < timeout) {
            try {
                WebElement element = findFlutterElement(key, value);
                if (element.isDisplayed()) {
                    return;
                }
            } catch (Exception e) {
                // Element not found, continue waiting
            }
            
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        throw new RuntimeException("Flutter widget not found: " + key + "=" + value);
    }
    
    public static WebElement findFlutterElement(String key, String value) {
        switch (key.toLowerCase()) {
            case "text":
                return driver.findElement(finder.text(value));
            case "key":
                return driver.findElement(finder.byValueKey(value));
            case "type":
                return driver.findElement(finder.byType(value));
            case "tooltip":
                return driver.findElement(finder.byTooltip(value));
            case "semantics":
                return driver.findElement(finder.bySemanticsLabel(value));
            default:
                throw new IllegalArgumentException("Unsupported finder type: " + key);
        }
    }
    
    public static void scrollToElement(String key, String value) {
        // Flutter-specific scrolling implementation
        driver.executeScript("flutter:scrollUntilVisible", 
            findFlutterElement(key, value), 
            finder.byType("ListView"));
    }
    
    public static void waitForFlutterToSettle() {
        driver.executeScript("flutter:waitFor", finder.byType("MaterialApp"));
    }
}
