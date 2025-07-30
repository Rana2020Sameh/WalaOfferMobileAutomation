package Pages;


import base.BasePage;
import io.appium.java_client.AppiumDriver;
import utils.FlutterUtils;
import org.openqa.selenium.WebElement;
import java.util.List;

public class HomePage extends BasePage {
    
    public HomePage(AppiumDriver driver) {
        super(driver);
        //TODO Auto-generated constructor stub
    }

    // Flutter widget keys
    private static final String HOME_TITLE_KEY = "home_title";
    private static final String USER_PROFILE_KEY = "user_profile_button";
    private static final String NAVIGATION_DRAWER_KEY = "navigation_drawer";
    private static final String LOGOUT_BUTTON_KEY = "logout_button";
    private static final String SEARCH_FIELD_KEY = "search_field";
    private static final String FLOATING_ACTION_BUTTON_KEY = "fab_button";
    
    public boolean isHomePageDisplayed() {
        try {
            FlutterUtils.waitForFlutterWidget("key", HOME_TITLE_KEY, 10);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public String getHomePageTitle() {
        WebElement titleElement = findFlutterElement("key", HOME_TITLE_KEY);
        return getElementText(titleElement);
    }
    
    public void openNavigationDrawer() {
        WebElement drawerButton = findFlutterElement("key", NAVIGATION_DRAWER_KEY);
        clickElement(drawerButton);
        FlutterUtils.waitForFlutterToSettle();
    }
    
    public void clickUserProfile() {
        WebElement profileButton = findFlutterElement("key", USER_PROFILE_KEY);
        clickElement(profileButton);
    }
    
    public void performSearch(String searchTerm) {
        WebElement searchField = findFlutterElement("key", SEARCH_FIELD_KEY);
        sendText(searchField, searchTerm);
        
        // Simulate enter key press in Flutter
        driver.executeScript("flutter:enterText", searchField, searchTerm);
    }
    
    public void clickFloatingActionButton() {
        WebElement fabButton = findFlutterElement("key", FLOATING_ACTION_BUTTON_KEY);
        clickElement(fabButton);
    }
    
    public LoginPage logout() {
        openNavigationDrawer();
        WebElement logoutButton = findFlutterElement("key", LOGOUT_BUTTON_KEY);
        clickElement(logoutButton);
        return new LoginPage();
    }
    
    // Method to handle Flutter-specific gestures
    public void swipeToRefresh() {
        driver.executeScript("flutter:swipe", 
            findFlutterElement("type", "RefreshIndicator"), 
            "down", 300);
    }
    
    public List<WebElement> getListItems() {
        return driver.findElements(finder.byType("ListTile"));
    }
}