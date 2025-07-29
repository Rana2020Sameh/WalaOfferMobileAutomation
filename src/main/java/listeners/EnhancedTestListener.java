package listeners;


import utils.ReportUtils;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import base.DriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EnhancedTestListener implements ITestListener, ISuiteListener {
    
    @Override
    public void onStart(ISuite suite) {
        ReportUtils.initializeReport();
        System.out.println("Starting test suite: " + suite.getName());
    }
    
    @Override
    public void onFinish(ISuite suite) {
        ReportUtils.flushReport();
        System.out.println("Finished test suite: " + suite.getName());
    }
    
    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        ReportUtils.createTest(testName, description != null ? description : testName);
        ReportUtils.logInfo("Starting test: " + testName);
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        ReportUtils.logPass("Test completed successfully");
        ReportUtils.updateTestResult(result);
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        String screenshotPath = takeScreenshot(result.getMethod().getMethodName());
        if (screenshotPath != null) {
            ReportUtils.attachScreenshot(screenshotPath);
        }
        ReportUtils.updateTestResult(result);
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        ReportUtils.updateTestResult(result);
    }
    
    private String takeScreenshot(String testName) {
        try {
            if (DriverManager.getDriver() != null) {
                TakesScreenshot screenshot = (TakesScreenshot) DriverManager.getDriver();
                byte[] screenshotBytes = screenshot.getScreenshotAs(OutputType.BYTES);
                
                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String fileName = String.format("screenshot_%s_%s.png", testName, timestamp);
                String screenshotPath = System.getProperty("user.dir") + "/test-output/screenshots/" + fileName;
                
                Files.createDirectories(Paths.get(screenshotPath).getParent());
                Files.write(Paths.get(screenshotPath), screenshotBytes);
                
                return screenshotPath;
            }
        } catch (IOException e) {
            System.err.println("Failed to take screenshot: " + e.getMessage());
        }
        return null;
    }
}
