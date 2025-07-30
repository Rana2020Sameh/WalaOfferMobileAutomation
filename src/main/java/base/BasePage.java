package base;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

public abstract class BasePage {
    protected AppiumDriver driver;
    protected AndroidDriver androidDriver;
    protected WebDriverWait wait;
    protected AppiumBy appiumBy;
    protected Properties locators;
    
    public BasePage(AppiumDriver driver) {
        this.driver = DriverManager.getDriver();
        this.driver = driver;
        locators = new Properties();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        try (FileInputStream fis = new FileInputStream("src/test/resources/locators.properties")) {
            locators.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    protected void waitForElementToBeVisible(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }
    protected void waitforpresenceOfElementLocated(WebElement element)
    {
        wait.until(ExpectedConditions.presenceOfElementLocated((By) element));
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