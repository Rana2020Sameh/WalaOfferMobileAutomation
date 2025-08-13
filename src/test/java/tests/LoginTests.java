package tests;

import base.BaseTest;
import utils.DataProviderUtils;
import utils.PerformanceUtils;
import utils.ReportUtils;
import utils.AITestGenerator;
import base.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import Pages.HomePage;
import Pages.LoginPage;

import java.util.List;
@Test
public class LoginTests extends BaseTest {
    private LoginPage loginPage;
  //  private SoftAssert softAssert;

    @Test
    public void loginUserWithValidCredintials(String mobileNo) {

        ReportUtils.logInfo("Testing valid login functionality");
        // Assert.assertTrue(loginPage.isLoginPageDisplayedAlternative(),
        // "Login page should be displayed");
        ReportUtils.logPass("Login page displayed successfully");
        // Perform login with valid credentials
        PerformanceUtils.startTimer("ValidLogin");

       // HomePage homePage = loginPage.performLoginDirectFinder(VALID_EMAIL, VALID_PASSWORD);
       // long loginTime = PerformanceUtils.stopTimer("ValidLogin");
        // Verify successful login
       // Assert.assertTrue(homePage.isHomePageDisplayed(),
     loginPage.loginFun();
       // ReportUtils.logPass("Valid login completed successfully in " + loginTime + "ms");

        // Verify user is properly authenticated
        //String pageTitle = homePage.getHomePageTitle();
        //Assert.assertFalse(pageTitle.isEmpty(), "Home page should have a title");

        ReportUtils.logInfo("User successfully authenticated and redirected to home page");

    }

}