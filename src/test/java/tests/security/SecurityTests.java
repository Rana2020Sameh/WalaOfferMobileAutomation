package tests.security;
import framework.base.BaseTest;
import framework.utils.ReportUtils;
import framework.utils.APIUtils;
import pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SecurityTests extends BaseTest {
    
    @Test(description = "Test SQL injection resistance")
    public void testSQLInjectionResistance() {
        LoginPage loginPage = new LoginPage();
        
        ReportUtils.logInfo("Testing SQL injection resistance");
        
        String[] sqlInjectionPayloads = {
            "admin'; DROP TABLE users; --",
            "' OR '1'='1",
            "admin'/*",
            "' UNION SELECT * FROM users --"
        };
        
        for (String payload : sqlInjectionPayloads) {
            ReportUtils.logInfo("Testing payload: " + payload);
            
            loginPage.enterEmail(payload);
            loginPage.enterPassword("password");
            loginPage.clickLoginButton();
            
            // Should not allow login with SQL injection
            Assert.assertTrue(loginPage.isLoginPageDisplayed(), 
                "Should remain on login page - SQL injection blocked");
            
            String errorMessage = loginPage.getErrorMessage();
            Assert.assertFalse(errorMessage.isEmpty(), 
                "Should show error message for invalid login attempt");
        }
        
        ReportUtils.logPass("App is resistant to SQL injection attacks");
    }
    
    @Test(description = "Test XSS resistance")
    public void testXSSResistance() {
        LoginPage loginPage = new LoginPage();
        
        ReportUtils.logInfo("Testing XSS resistance");
        
        String[] xssPayloads = {
            "<script>alert('XSS')</script>",
            "javascript:alert('XSS')",
            "<img src=x onerror=alert('XSS')>",
            "'><script>alert('XSS')</script>"
        };
        
        for (String payload : xssPayloads) {
            ReportUtils.logInfo("Testing XSS payload: " + payload);
            
            loginPage.enterEmail(payload);
            loginPage.enterPassword("password");
            loginPage.clickLoginButton();
            
            // Should handle XSS payload safely
            Assert.assertTrue(loginPage.isLoginPageDisplayed(), 
                "Should handle XSS payload safely");
        }
        
        ReportUtils.logPass("App is resistant to XSS attacks");
    }
    
    @Test(description = "Test session timeout")
    public void testSessionTimeout() {
        LoginPage loginPage = new LoginPage();
        
        ReportUtils.logInfo("Testing session timeout behavior");
        
        // Login successfully
        loginPage.performLogin("valid.user@example.com", "ValidPass123!");
        
        // Simulate session timeout by waiting or manipulating session
        try {
            Thread.sleep(2000); // Simulate some time passing
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Try to access secured functionality
        // This would depend on your app's session management
        ReportUtils.logInfo("Testing access after potential session timeout");
        
        ReportUtils.logPass("Session timeout test completed");
    }
}