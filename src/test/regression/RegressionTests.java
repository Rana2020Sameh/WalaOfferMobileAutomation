package tests.regression;
import framework.base.BaseTest;
import framework.utils.ReportUtils;
import framework.utils.PerformanceUtils;
import framework.utils.DeviceUtils;
import pages.LoginPage;
import pages.HomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RegressionTests extends BaseTest {
    
    @Test(description = "Test app behavior during orientation changes")
    public void testOrientationChanges() {
        LoginPage loginPage = new LoginPage();
        
        ReportUtils.logInfo("Testing app behavior during orientation changes");
        
        // Test in portrait
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Login page should be displayed in portrait");
        
        // Change to landscape
        DeviceUtils.rotateToLandscape();
        ReportUtils.logInfo("Rotated to landscape");
        
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Login page should remain functional in landscape");
        
        // Login in landscape
        HomePage homePage = loginPage.performLogin("valid.user@example.com", "ValidPass123!");
        Assert.assertTrue(homePage.isHomePageDisplayed(), "Should be able to login in landscape mode");
        
        // Change back to portrait
        DeviceUtils.rotateToPortrait();
        ReportUtils.logInfo("Rotated back to portrait");
        
        Assert.assertTrue(homePage.isHomePageDisplayed(), "Home page should remain functional after rotation");
        
        ReportUtils.logPass("App handles orientation changes correctly");
    }
    
    @Test(description = "Test app performance metrics")
    public void testAppPerformance() {
        ReportUtils.logInfo("Testing app performance metrics");
        
        PerformanceUtils.startTimer("LoginProcess");
        
        LoginPage loginPage = new LoginPage();
        HomePage homePage = loginPage.performLogin("valid.user@example.com", "ValidPass123!");
        
        long loginDuration = PerformanceUtils.stopTimer("LoginProcess");
        
        ReportUtils.logInfo("Login process took: " + loginDuration + " milliseconds");
        
        // Assert login takes less than 10 seconds
        Assert.assertTrue(loginDuration < 10000, "Login should complete within 10 seconds");
        
        // Log performance data
        PerformanceUtils.logAppStartupTime();
        PerformanceUtils.logMemoryUsage();
        
        ReportUtils.logPass("Performance metrics collected successfully");
    }
    
    @Test(description = "Test app resilience to network issues")
    public void testNetworkResilience() {
        LoginPage loginPage = new LoginPage();
        
        ReportUtils.logInfo("Testing app behavior with network issues");
        
        // Simulate network disconn
    }
}
