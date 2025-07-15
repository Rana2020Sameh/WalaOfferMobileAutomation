package base;


import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.flutter.FlutterDriverOptions;
import io.appium.java_client.flutter.FlutterIntegrationTestDriver;
import io.appium.java_client.flutter.android.FlutterAndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static utils.PropertyReader.getIntProperty;

public class BasePage {
    protected AppiumDriver driver;
    protected WebDriverWait wait;
    protected FlutterDriverOptions flutterDriverOptions;
    protected FlutterAndroidDriver flutterAndroidDriver;
    protected FlutterIntegrationTestDriver flutterIntegrationTestDriver;
    protected AppiumBy.FlutterBy flutterBy;
    protected AppiumBy appiumBy;

    public BasePage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(getIntProperty("implicit.wait.timeout")));
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    protected WebElement waitForElementVisibility(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            System.err.println("Element not visible after " + getIntProperty("implicit.wait.timeout") + " seconds: " + locator);
            throw e;
        }
    }

    protected WebElement waitForElementClickable(By locator) {
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(locator));
        } catch (TimeoutException e) {
            System.err.println("Element not clickable after " + getIntProperty("implicit.wait.timeout") + " seconds: " + locator);
            throw e;
        }
    }

    protected void click(By locator) {

        waitForElementClickable(locator).click();
    }

    protected void sendKeys(By locator, String text) {
        WebElement element = waitForElementVisibility(locator);
        element.clear(); // Clear existing text
        element.sendKeys(text);
    }

    protected String getText(By locator) {
        return waitForElementVisibility(locator).getText();
    }





}
