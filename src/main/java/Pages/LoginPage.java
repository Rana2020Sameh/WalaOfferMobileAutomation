package Pages;

import base.BasePage;
import org.openqa.selenium.WebElement;

public class LoginPage extends BasePage {
    
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
    
    public void enterEmail(String email) {
        WebElement emailField = findFlutterElement("key", EMAIL_FIELD_KEY);
        sendText(emailField, email);
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
