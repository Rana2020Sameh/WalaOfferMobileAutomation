package base;


import io.appium.java_client.AppiumDriver;
import io.appium.java_client.flutter.FlutterFinder;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public abstract class BasePage {
    protected AppiumDriver driver;
    protected FlutterFinder finder;
    protected WebDriverWait wait;
    
    public BasePage() {
        this.driver = DriverManager.getDriver();
        this.finder = DriverManager.getFlutterFinder();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }
    
    protected void waitForElementToBeVisible(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }
    
    protected void waitForElementToBeClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }
    
    protected WebElement findFlutterElement(String key, String keyValue) {
        switch (key.toLowerCase()) {
            case "text":
                return driver.findElement(finder.text(keyValue));
            case "key":
                return driver.findElement(finder.byValueKey(keyValue));
            case "type":
                return driver.findElement(finder.byType(keyValue));
            case "tooltip":
                return driver.findElement(finder.byTooltip(keyValue));
            default:
                throw new IllegalArgumentException("Unsupported finder type: " + key);
        }
    }
    
    protected void clickElement(WebElement element) {
        waitForElementToBeClickable(element);
        element.click();
    }
    
    protected void sendText(WebElement element, String text) {
        waitForElementToBeVisible(element);
        element.clear();
        element.sendKeys(text);
    }
    
    protected String getElementText(WebElement element) {
        waitForElementToBeVisible(element);
        return element.getText();
    }
}
