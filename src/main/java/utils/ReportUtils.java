   package utils;


   import com.aventstack.extentreports.ExtentReports;
   import com.aventstack.extentreports.ExtentTest;
   import com.aventstack.extentreports.reporter.ExtentSparkReporter;
   import com.aventstack.extentreports.reporter.configuration.Theme;
   import org.testng.ITestResult;
   
   import java.io.File;
   import java.text.SimpleDateFormat;
   import java.util.Date;
   
   public class ReportUtils {
       private static ExtentReports extent;
       private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
       
       public static void initializeReport() {
           if (extent == null) {
               String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
               String reportPath = System.getProperty("user.dir") + 
                   "/test-output/reports/FlutterAppTestReport_" + timestamp + ".html";
               
               // Create directories if they don't exist
               new File(reportPath).getParentFile().mkdirs();
               
               ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
               sparkReporter.config().setDocumentTitle("Flutter App Test Report");
               sparkReporter.config().setReportName("Automation Test Results");
               sparkReporter.config().setTheme(Theme.STANDARD);
               
               extent = new ExtentReports();
               extent.attachReporter(sparkReporter);
               
               // System information
               extent.setSystemInfo("Application", "Flutter QA App");
               extent.setSystemInfo("Operating System", System.getProperty("os.name"));
               extent.setSystemInfo("Java Version", System.getProperty("java.version"));
               extent.setSystemInfo("Appium Version", "8.5.1");
           }
       }
       
       public static ExtentTest createTest(String testName, String description) {
           ExtentTest extentTest = extent.createTest(testName, description);
           test.set(extentTest);
           return extentTest;
       }
       
       public static ExtentTest getTest() {
           return test.get();
       }
       
       public static void logInfo(String message) {
           if (getTest() != null) {
               getTest().info(message);
           }
       }
       
       public static void logPass(String message) {
           if (getTest() != null) {
               getTest().pass(message);
           }
       }
       
       public static void logFail(String message) {
           if (getTest() != null) {
               getTest().fail(message);
           }
       }
       
       public static void logSkip(String message) {
           if (getTest() != null) {
               getTest().skip(message);
           }
       }
       
       public static void attachScreenshot(String screenshotPath) {
           if (getTest() != null) {
               getTest().addScreenCaptureFromPath(screenshotPath);
           }
       }
       
       public static void flushReport() {
           if (extent != null) {
               extent.flush();
           }
       }
       
       public static void updateTestResult(ITestResult result) {
           if (getTest() != null) {
               switch (result.getStatus()) {
                   case ITestResult.SUCCESS:
                       logPass("Test passed successfully");
                       break;
                   case ITestResult.FAILURE:
                       logFail("Test failed: " + result.getThrowable().getMessage());
                       break;
                   case ITestResult.SKIP:
                       logSkip("Test skipped: " + result.getThrowable().getMessage());
                       break;
               }
           }
       }
   }
   