package Pages;


import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public abstract class BasePage {
    protected AppiumDriver driver;
    protected WebDriverWait wait;
    protected AppiumBy appiumBy;
        
    public BasePage() {
        this.driver = DriverManager.getDriver();
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
                return driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().text(\"" + keyValue + "\")"));
            case "key":
                return driver.findElement(AppiumBy.accessibilityId(keyValue));
                case "xpath":
                return driver.findElement(appiumBy.xpath(keyValue));
                case "ID":
                return driver.findElement(appiumBy.id(keyValue));
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