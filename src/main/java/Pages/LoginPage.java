package Pages;

import base.BasePage;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage<MobileElement> extends BasePage {
    
    public LoginPage(AppiumDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);

    }

    // Flutter element locators using keys
    private static final String EMAIL_FIELD_KEY = "email_field";
    private static final String MOBILENUMBER_FIELD_KEY="mobile_number";
    private static final String PASSWORD_FIELD_KEY = "password_field";
    private static final String LOGIN_BUTTON_KEY = "login_button";
    private static final String ERROR_MESSAGE_KEY = "error_message";
    private static final String  PERMESSION_ALLOW_BUTTON="permission_allow_button";
    private static final String  PERMESSION_NOTALLOW_BUTTON="permission_not_allow_button";
    private static final String  START_NOW_BUTTON="start_now_button";
    private static final String NEXT_BUTTON="newx_button";
    private static final String LOGIN_BUTTON="login_button";
    private static final String MobileFileLocator;
    private static final String AllowPermmisionId;
    private static final WebElement  mobileNumberField ;
    private static final WebElement startNowButton;
    private static final String startNowLocator;
    private static final WebElement nextButton;
    private static final String nextLocator;
public void allowPermmision()
{
    AllowPermmisionId=locators.getProperty("allowButtonID");
    driver.findElement(By.id(AllowPermmisionId));
    //driver.findElement(By.id("com.android.permissioncontroller:id/permission_allow_button")).click();
}
public void startNow()
{
    startNowLocator=locators.getProperty("startNow.Xpath");
    startNowButton=driver.findElement(appiumBy.xpath(startNowLocator));
    waitforpresenceOfElementLocated(startNowButton);
    startNowButton.click();

}
public void pressNextinOnboarding()
{
    nextLocator=locators.getProperty("NextButtonAccessibiltyID");
    for (int i = 0; i < 2; i++) {

        try {
            nextButton = driver.findElement(appiumBy.accessibilityId(nextLocator));
            waitForElementToBeClickable(nextButton);
            nextButton.click();
            Thread.sleep(1000); 
          
        }
        catch (Exception e) {
            System.out.println("Next button not found at iteration " + i);
            break;
        }
    }

}

    public void enterMobileNumber(String number) {
        //to read xpath value for mobile number filed from Locators Properties file
         MobileFileLocator=locators.getProperty("mobileNm");
          mobileNumberField =  driver.findElement(By.xpath(MobileFileLocator));
        mobileNumberField.click();
        mobileNumberField.sendKeys(number);
    }
    
    public void enterEmail(String email) {
        WebElement emailField = findFlutterElement("key", EMAIL_FIELD_KEY);
        sendText(emailField, email);
    }
    }
    
    public void enterPassword(String password) {
        WebElement passwordField = findFlutterElement("key", PASSWORD_FIELD_KEY);
        sendText(passwordField, password);
    }
    
    public void clickLoginButton() {
        WebElement loginButton = findFlutterElement("key", LOGIN_BUTTON_KEY);
        clickElement(loginButton);
    }
    
    public String getErrorMessage() {
        WebElement errorElement = findFlutterElement("key", ERROR_MESSAGE_KEY);
        return getElementText(errorElement);
    }
    
    public HomePage performLogin(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickLoginButton();
        return new HomePage();
    }
    
    public boolean isLoginPageDisplayed() {
        try {
            WebElement emailField = findFlutterElement("key", EMAIL_FIELD_KEY);
            return emailField.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}