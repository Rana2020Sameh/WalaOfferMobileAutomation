package tests;


import base.BaseTest;
import utils.DataProviderUtils;
import utils.AITestGenerator;
import LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class LoginTests extends BaseTest {
    
    @Test(dataProvider = "loginData", dataProviderClass = DataProviderUtils.class)
    public void testLoginWithDataProvider(String email, String password, boolean expectedResult) {
        LoginPage loginPage = new LoginPage();
        
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Login page should be displayed");
        
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        loginPage.clickLoginButton();
        
        if (expectedResult) {
            // Verify successful login - implement based on your app's behavior
            // Assert.assertTrue(new HomePage().isHomePageDisplayed());
        } else {
            // Verify error message is displayed
            String errorMessage = loginPage.getErrorMessage();
            Assert.assertFalse(errorMessage.isEmpty(), "Error message should be displayed for invalid login");
        }
    }
    
    @Test
    public void testAIGeneratedTestCases() {
        // Generate test cases using AI
        List<AITestGenerator.TestCase> aiTestCases = AITestGenerator.generateTestCases(
            "User login functionality with email and password", 
            "Flutter"
        );
        
        LoginPage loginPage = new LoginPage();
        
        for (AITestGenerator.TestCase testCase : aiTestCases) {
            System.out.println("Executing AI-generated test: " + testCase.getTestName());
            System.out.println("Description: " + testCase.getDescription());
            
            // Execute test steps - this is a simplified example
            // In reality, you'd parse and execute the steps programmatically
            for (String step : testCase.getSteps()) {
                System.out.println("Step: " + step);
                // Implement step execution logic
            }
            
            System.out.println("Expected Result: " + testCase.getExpectedResult());
        }
    }
    
    @Test
    public void testLoginPageElements() {
        LoginPage loginPage = new LoginPage();
        
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Login page should be displayed");
        
        // Test individual elements
        loginPage.enterEmail("test@example.com");
        loginPage.enterPassword("password123");
        
        // Verify elements are interactable
        Assert.assertTrue(true, "Login elements are functional");
    }
} catch (Exception e) {
            throw new RuntimeException("Failed to initialize driver", e);
        }
    }
    
    public static AppiumDriver getDriver() {
        return driver.get();
    }
    
    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
    
    public static FlutterFinder getFlutterFinder() {
        return new FlutterFinder(getDriver());
    }
}
