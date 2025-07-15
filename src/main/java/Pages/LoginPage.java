package Pages;

import base.BasePage;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public class LoginPage extends BasePage {
    //private FlutterFinder flutterFinder = DriverFactory.flutterFinder;

    // Using AppiumBy.ByFlutterFinder for Flutter elements
    // Always use Flutter Inspector (DevTools) to get the correct keys/semantics
    private final By USERNAME_FIELD;
    private final By PASSWORD_FIELD = appiumBy.xpath("Password input field");
    private final By LOGIN_BUTTON = AppiumBy.accessibilityId("loginButton");

    private final By ERROR_MESSAGE = AppiumBy.xpath("Invalid credentials message");
    // You can also use @AndroidFindBy/@iOSXCUITFindBy if elements have platform-specific accessibility IDs
    // @AndroidFindBy(accessibility = "username_input")
    // @iOSXCUITFindBy(accessibility = "username_field")
    // private WebElement usernameField;

    public LoginPage(AppiumDriver driver) {
        super(driver);
        USERNAME_FIELD = AppiumBy.xpath("Username input field");
    }

    public void enterUsername(String username) {
        sendKeys(USERNAME_FIELD, username);
    }

    public void enterPassword(String password) {
        sendKeys(PASSWORD_FIELD, password);
    }

    public HomePage clickLoginButton() {
        click(LOGIN_BUTTON);
        return new HomePage(driver); // Assuming successful login navigates to HomePage
    }

    public String getErrorMessage() {
        return getText(ERROR_MESSAGE);
    }

    public HomePage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        return clickLoginButton();
    }

    public boolean isLoginPageDisplayed() {
        return waitForElementVisibility(LOGIN_BUTTON).isDisplayed();
    }

    // Locators


}
