package parallel;



import base.BaseTest;
import utils.ReportUtils;
import LoginPage;
import HomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ParallelTests extends BaseTest {
    
    @Test(description = "Parallel test 1 - Login functionality", threadPoolSize = 2, invocationCount = 1)
    public void parallelLoginTest1() {
        ReportUtils.logInfo("Executing parallel login test 1");
        
        LoginPage loginPage = new LoginPage();
        HomePage homePage = loginPage.performLogin("user1@example.com", "password123");
        
        Assert.assertTrue(homePage.isHomePageDisplayed(), "Home page should be displayed");
        
        ReportUtils.logPass("Parallel login test 1 completed successfully");
    }
    
    @Test(description = "Parallel test 2 - Navigation functionality", threadPoolSize = 2, invocationCount = 1)
    public void parallelNavigationTest2() {
        ReportUtils.logInfo("Executing parallel navigation test 2");
        
        LoginPage loginPage = new LoginPage();
        HomePage homePage = loginPage.performLogin("user2@example.com", "password123");
        
        homePage.openNavigationDrawer();
        homePage.clickUserProfile();
        
        ReportUtils.logPass("Parallel navigation test 2 completed successfully");
    }
}
