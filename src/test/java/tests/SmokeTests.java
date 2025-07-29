package tests;

import framework.base.BaseTest;
import framework.utils.ReportUtils;
import pages.LoginPage;
import pages.HomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SmokeTests extends BaseTest {
    
    @Test(priority = 1, description = "Verify app launches successfully")
    public void testAppLaunch() {
        LoginPage loginPage = new LoginPage();
        ReportUtils.logInfo("Checking if app launches and login page is displayed");
        
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), 
            "App should launch successfully and show login page");
        
        ReportUtils.logPass("App launched successfully");
    }
    
    @Test(priority = 2, description = "Verify successful login flow")
    public void testLoginFlow() {
        LoginPage loginPage = new LoginPage();
        ReportUtils.logInfo("Testing login flow with valid credentials");
        
        HomePage homePage = loginPage.performLogin("valid.user@example.com", "ValidPass123!");
        
        ReportUtils.logInfo("Verifying home page is displayed after login");
        Assert.assertTrue(homePage.isHomePageDisplayed(), 
            "Home page should be displayed after successful login");
        
        ReportUtils.logPass("Login flow completed successfully");
    }
    
    @Test(priority = 3, description = "Verify main navigation works")
    public void testMainNavigation() {
        LoginPage loginPage = new LoginPage();
        HomePage homePage = loginPage.performLogin("valid.user@example.com", "ValidPass123!");
        
        ReportUtils.logInfo("Testing main navigation elements");
        
        // Test navigation drawer
        homePage.openNavigationDrawer();
        ReportUtils.logInfo("Navigation drawer opened successfully");
        
        // Test user profile access
        homePage.clickUserProfile();
        ReportUtils.logInfo("User profile accessed successfully");
        
        ReportUtils.logPass("Main navigation works correctly");
    }
    
    @Test(priority = 4, description = "Verify logout functionality")
    public void testLogout() {
        LoginPage loginPage = new LoginPage();
        HomePage homePage = loginPage.performLogin("valid.user@example.com", "ValidPass123!");
        
        ReportUtils.logInfo("Testing logout functionality");
        
        LoginPage returnedLoginPage = homePage.logout();
        
        Assert.assertTrue(returnedLoginPage.isLoginPageDisplayed(), 
            "Should return to login page after logout");
        
        ReportUtils.logPass("Logout functionality works correctly");
    }
}