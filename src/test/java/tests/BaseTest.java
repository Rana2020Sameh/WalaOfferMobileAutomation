package tests;

import io.appium.java_client.AppiumDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import utils.AppiumDriverManager;
import utils.PropertyReader;



public class BaseTest {

    protected AppiumDriver driver;
    private static final Logger logger = LoggerFactory.getLogger(BaseTest.class);

    @BeforeSuite
    public void setupSuite() {
        // Load properties once for the entire test suite
        PropertyReader.loadProperties();
        logger.info("Configuration properties loaded successfully.");
    }

    @BeforeMethod
    public void setup() {
        driver = AppiumDriverManager.getDriver();
        logger.info("Appium driver initialized for test: {}", getClass().getSimpleName());
    }

    @AfterMethod
    public void teardown() {
        AppiumDriverManager.quitDriver();
        logger.info("Appium driver quit.");
    }
}
