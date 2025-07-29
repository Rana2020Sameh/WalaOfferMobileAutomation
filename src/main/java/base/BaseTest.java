package base;


import config.FlutterCapabilities;
import listeners.TestListener;
import org.testng.annotations.*;

@Listeners(TestListener.class)
public abstract class BaseTest {
    
    @BeforeMethod
    public void setUp() {
        DriverManager.initializeDriver(FlutterCapabilities.getAndroidCapabilities());
    }
    
    @AfterMethod
    public void tearDown() {
        DriverManager.quitDriver();
    }
    
    @BeforeClass
    public void beforeClass() {
        System.out.println("Starting test class: " + this.getClass().getSimpleName());
    }
    
    @AfterClass
    public void afterClass() {
        System.out.println("Finished test class: " + this.getClass().getSimpleName());
    }
}