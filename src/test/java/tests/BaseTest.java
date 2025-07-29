package tests;


import config.FlutterCapabilities;
import config.ConfigManager;
import listeners.EnhancedTestListener;
import utils.ReportUtils;
import utils.PerformanceUtils;
import utils.DatabaseUtils;
import utils.APIUtils;

import org.testng.annotations.*;
import org.testng.ITestResult;
import org.testng.Assert;

import org.openqa.selenium.remote.DesiredCapabilities;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.HashMap;

@Listeners(EnhancedTestListener.class)
public abstract class BaseTest {
    
    // Test configuration properties
    protected String testEnvironment;
    protected String deviceName;
    protected String platformVersion;
    protected boolean enablePerformanceMonitoring;
    protected boolean enableAPIValidation;
    protected boolean enableDatabaseCleanup;
    
    // Test execution context
    protected ThreadLocal<String> currentTestName = new ThreadLocal<>();
    protected ThreadLocal<String> currentTestDescription = new ThreadLocal<>();
    protected ThreadLocal<Map<String, Object>> testContext = new ThreadLocal<>();
    
    /**
     * Suite-level setup - executed once before all tests in the suite
     * Initializes global configurations and resources
     */
    @BeforeSuite(alwaysRun = true)
    public void suiteSetup() {
        System.out.println("=== Starting Test Suite Execution ===");
        
        // Initialize reporting system
        ReportUtils.initializeReport();
        
        // Load and validate configuration
        loadTestConfiguration();
        
        // Validate test environment connectivity
        validateTestEnvironment();
        
        // Initialize performance monitoring if enabled
        if (enablePerformanceMonitoring) {
            PerformanceUtils.startTimer("SuiteExecution");
            System.out.println("Performance monitoring enabled");
        }
        
        System.out.println("Suite setup completed successfully");
    }
    
    /**
     * Class-level setup - executed once before each test class
     * Sets up class-specific configurations
     */
    @BeforeClass(alwaysRun = true)
    public void classSetup() {
        String className = this.getClass().getSimpleName();
        System.out.println("Starting test class: " + className);
        
        // Initialize test context for the class
        Map<String, Object> context = new HashMap<>();
        context.put("className", className);
        context.put("startTime", System.currentTimeMillis());
        testContext.set(context);
        
        // Class-specific performance monitoring
        if (enablePerformanceMonitoring) {
            PerformanceUtils.startTimer("ClassExecution_" + className);
        }
        
        // Prepare test data for the class if needed
        prepareClassTestData();
    }
    
    /**
     * Method-level setup - executed before each test method
     * Initializes driver and sets up test-specific configurations
     */
    @BeforeMethod(alwaysRun = true)
    public void methodSetup(Method method) {
        // Extract test information
        String testName = method.getName();
        String testDescription = getTestDescription(method);
        
        currentTestName.set(testName);
        currentTestDescription.set(testDescription);
        
        System.out.println("\n--- Starting Test: " + testName + " ---");
        
        // Initialize Appium driver with appropriate capabilities
        initializeDriver();
        
        // Start performance monitoring for the test
        if (enablePerformanceMonitoring) {
            PerformanceUtils.startTimer("TestExecution_" + testName);
        }
        
        // Set up test-specific context
        setupTestContext(method);
        
        // Pre-test validations
        performPreTestValidations();
        
        System.out.println("Test setup completed for: " + testName);
    }
    
    /**
     * Method-level teardown - executed after each test method
     * Cleans up resources and captures test results
     */
    @AfterMethod(alwaysRun = true)
    public void methodTeardown(ITestResult result) {
        String testName = currentTestName.get();
        
        try {
            // Capture test execution metrics
            if (enablePerformanceMonitoring) {
                long executionTime = PerformanceUtils.stopTimer("TestExecution_" + testName);
                System.out.println("Test execution time: " + executionTime + " ms");
            }
            
            // Handle test result-specific actions
            handleTestResult(result);
            
            // Perform post-test cleanup
            performPostTestCleanup(result);
            
            // API validation if enabled
            if (enableAPIValidation && result.getStatus() == ITestResult.SUCCESS) {
                performPostTestAPIValidation();
            }
            
        } catch (Exception e) {
            System.err.println("Error during test teardown: " + e.getMessage());
        } finally {
            // Always quit driver to free resources
            DriverManager.quitDriver();
            
            // Clear thread-local variables
            cleanupThreadLocalVariables();
            
            System.out.println("--- Completed Test: " + testName + " ---\n");
        }
    }
    
    /**
     * Class-level teardown - executed once after each test class
     * Performs class-specific cleanup
     */
    @AfterClass(alwaysRun = true)
    public void classTeardown() {
        String className = this.getClass().getSimpleName();
        
        try {
            // Class-level performance metrics
            if (enablePerformanceMonitoring) {
                long classExecutionTime = PerformanceUtils.stopTimer("ClassExecution_" + className);
                System.out.println("Class execution time: " + classExecutionTime + " ms");
            }
            
            // Class-specific cleanup
            performClassCleanup();
            
            // Database cleanup if enabled
            if (enableDatabaseCleanup) {
                performDatabaseCleanup();
            }
            
        } catch (Exception e) {
            System.err.println("Error during class teardown: " + e.getMessage());
        }
        
        System.out.println("Finished test class: " + className);
    }
    
    /**
     * Suite-level teardown - executed once after all tests in the suite
     * Performs final cleanup and generates reports
     */
    @AfterSuite(alwaysRun = true)
    public void suiteTeardown() {
        try {
            // Suite-level performance metrics
            if (enablePerformanceMonitoring) {
                long suiteExecutionTime = PerformanceUtils.stopTimer("SuiteExecution");
                System.out.println("Total suite execution time: " + suiteExecutionTime + " ms");
                PerformanceUtils.printAllMetrics();
            }
            
            // Generate final reports
            ReportUtils.flushReport();
            
            // Perform final cleanup
            performFinalCleanup();
            
        } catch (Exception e) {
            System.err.println("Error during suite teardown: " + e.getMessage());
        }
        
        System.out.println("=== Test Suite Execution Completed ===");
    }
    
    // ==================== PRIVATE HELPER METHODS ====================
    
    /**
     * Loads test configuration from properties file and environment variables
     */
    private void loadTestConfiguration() {
        testEnvironment = ConfigManager.getProperty("test.environment", "qa");
        deviceName = ConfigManager.getProperty("device.name", "Android Emulator");
        platformVersion = ConfigManager.getProperty("android.version", "13");
        enablePerformanceMonitoring = Boolean.parseBoolean(
            ConfigManager.getProperty("enable.performance.monitoring", "true"));
        enableAPIValidation = Boolean.parseBoolean(
            ConfigManager.getProperty("enable.api.validation", "false"));
        enableDatabaseCleanup = Boolean.parseBoolean(
            ConfigManager.getProperty("enable.database.cleanup", "false"));
        
        System.out.println("Test Configuration Loaded:");
        System.out.println("- Environment: " + testEnvironment);
        System.out.println("- Device: " + deviceName);
        System.out.println("- Platform: " + platformVersion);
        System.out.println("- Performance Monitoring: " + enablePerformanceMonitoring);
    }
    
    /**
     * Validates test environment connectivity and prerequisites
     */
    private void validateTestEnvironment() {
        try {
            // Validate Appium server connectivity
            // This is a simplified check - you might want to implement actual connectivity test
            System.out.println("Validating test environment...");
            
            // Check if required files exist
            String apkPath = System.getProperty("user.dir") + 
                "/src/test/resources/apk/app-qa-release-universal.apk";
            java.io.File apkFile = new java.io.File(apkPath);
            
            if (!apkFile.exists()) {
                throw new RuntimeException("APK file not found at: " + apkPath);
            }
            
            System.out.println("Environment validation successful");
            
        } catch (Exception e) {
            throw new RuntimeException("Environment validation failed: " + e.getMessage());
        }
    }
    
    /**
     * Initializes Appium driver with appropriate capabilities
     */
    private void initializeDriver() {
        try {
            DesiredCapabilities capabilities = FlutterCapabilities.getAndroidCapabilities();
            
            // Add test-specific capabilities if needed
            capabilities.setCapability("testName", currentTestName.get());
            
            DriverManager.initializeDriver(capabilities);
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize driver: " + e.getMessage());
        }
    }
    
    /**
     * Sets up test-specific context and data
     */
    private void setupTestContext(Method method) {
        Map<String, Object> context = testContext.get();
        if (context == null) {
            context = new HashMap<>();
        }
        
        // Add method-specific context
        context.put("currentMethod", method.getName());
        context.put("methodStartTime", System.currentTimeMillis());
        
        // Extract test parameters if any
        Test testAnnotation = method.getAnnotation(Test.class);
        if (testAnnotation != null) {
            context.put("testGroups", testAnnotation.groups());
            context.put("testPriority", testAnnotation.priority());
            context.put("testDescription", testAnnotation.description());
        }
        
        testContext.set(context);
    }
    
    /**
     * Performs pre-test validations
     */
    private void performPreTestValidations() {
        // Validate driver is initialized
        if (DriverManager.getDriver() == null) {
            throw new RuntimeException("Driver not initialized properly");
        }
        
        // Add any other pre-test validations
        System.out.println("Pre-test validations passed");
    }
    
    /**
     * Handles different test result scenarios
     */
    private void handleTestResult(ITestResult result) {
        String testName = currentTestName.get();
        
        switch (result.getStatus()) {
            case ITestResult.SUCCESS:
                System.out.println("✅ Test PASSED: " + testName);
                ReportUtils.logPass("Test completed successfully");
                break;
                
            case ITestResult.FAILURE:
                System.out.println("❌ Test FAILED: " + testName);
                System.out.println("Failure reason: " + result.getThrowable().getMessage());
                
                // Capture screenshot on failure
                captureFailureEvidence(testName);
                
                // Log failure details
                ReportUtils.logFail("Test failed: " + result.getThrowable().getMessage());
                break;
                
            case ITestResult.SKIP:
                System.out.println("⏭️ Test SKIPPED: " + testName);
                ReportUtils.logSkip("Test skipped: " + 
                    (result.getThrowable() != null ? result.getThrowable().getMessage() : "Unknown reason"));
                break;
        }
    }
    
    /**
     * Captures evidence when test fails
     */
    private void captureFailureEvidence(String testName) {
        try {
            // The screenshot capture is handled by the TestListener
            // But we can add additional failure evidence here
            
            // Log app state
            if (DriverManager.getDriver() != null) {
                String currentActivity = DriverManager.getDriver().getCurrentPackage();
                System.out.println("App state at failure - Package: " + currentActivity);
                
                // Log performance data
                if (enablePerformanceMonitoring) {
                    PerformanceUtils.logMemoryUsage();
                }
            }
            
        } catch (Exception e) {
            System.err.println("Failed to capture failure evidence: " + e.getMessage());
        }
    }
    
    /**
     * Performs post-test cleanup
     */
    private void performPostTestCleanup(ITestResult result) {
        // Clear any temporary data created during test
        // Reset app state if needed
        // Clear cached data
        
        System.out.println("Post-test cleanup completed");
    }
    
    /**
     * Performs API validation after successful tests
     */
    private void performPostTestAPIValidation() {
        try {
            // Example: Validate that UI changes are reflected in backend
            // This is a placeholder - implement based on your app's API
            System.out.println("Performing post-test API validation...");
            
        } catch (Exception e) {
            System.err.println("API validation failed: " + e.getMessage());
            // Don't fail the test for API validation issues, just log
        }
    }
    
    /**
     * Prepares test data for the class
     */
    private void prepareClassTestData() {
        // This can include:
        // - Setting up test users
        // - Preparing test data in database
        // - Creating test files
        System.out.println("Class test data preparation completed");
    }
    
    /**
     * Performs class-level cleanup
     */
    private void performClassCleanup() {
        // Clean up class-level resources
        // Remove temporary test data
        // Reset configurations
        System.out.println("Class cleanup completed");
    }
    
    /**
     * Performs database cleanup
     */
    private void performDatabaseCleanup() {
        try {
            // Clean up test data from database
            String className = this.getClass().getSimpleName();
            DatabaseUtils.executeUpdate("DELETE FROM test_sessions WHERE test_class = '" + className + "'");
            System.out.println("Database cleanup completed for class: " + className);
            
        } catch (Exception e) {
            System.err.println("Database cleanup failed: " + e.getMessage());
        }
    }
    
    /**
     * Performs final suite-level cleanup
     */
    private void performFinalCleanup() {
        // Close any remaining resources
        // Clean up temporary files
        // Send notifications if configured
        System.out.println("Final cleanup completed");
    }
    
    /**
     * Cleans up thread-local variables
     */
    private void cleanupThreadLocalVariables() {
        currentTestName.remove();
        currentTestDescription.remove();
        testContext.remove();
    }
    
    /**
     * Extracts test description from method annotations
     */
    private String getTestDescription(Method method) {
        Test testAnnotation = method.getAnnotation(Test.class);
        if (testAnnotation != null && !testAnnotation.description().isEmpty()) {
            return testAnnotation.description();
        }
        return method.getName();
    }
    
    // ==================== PROTECTED HELPER METHODS FOR SUBCLASSES ====================
    
    /**
     * Allows subclasses to add custom setup logic
     */
    protected void customSetup() {
        // Override in subclasses if needed
    }
    
    /**
     * Allows subclasses to add custom teardown logic
     */
    protected void customTeardown() {
        // Override in subclasses if needed
    }
    
    /**
     * Provides access to current test context for subclasses
     */
    protected Map<String, Object> getTestContext() {
        return testContext.get();
    }
    
    /**
     * Allows subclasses to add data to test context
     */
    protected void addToTestContext(String key, Object value) {
        Map<String, Object> context = testContext.get();
        if (context != null) {
            context.put(key, value);
        }
    }
    
    /**
     * Soft assertion helper for subclasses
     */
    protected void softAssert(boolean condition, String message) {
        if (!condition) {
            ReportUtils.logFail("Soft assertion failed: " + message);
            System.err.println("Soft assertion failed: " + message);
            // Don't throw exception - continue test execution
        } else {
            ReportUtils.logPass("Soft assertion passed: " + message);
        }
    }
    
    /**
     * Hard assertion helper for subclasses
     */
    protected void hardAssert(boolean condition, String message) {
        if (!condition) {
            ReportUtils.logFail("Hard assertion failed: " + message);
            Assert.fail(message);
        } else {
            ReportUtils.logPass("Hard assertion passed: " + message);
        }
    }
    
    /**
     * Retry mechanism for flaky operations
     */
    protected void retryOperation(Runnable operation, int maxRetries, String operationName) {
        Exception lastException = null;
        
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                operation.run();
                if (attempt > 1) {
                    System.out.println("Operation succeeded on attempt " + attempt + ": " + operationName);
                }
                return;
            } catch (Exception e) {
                lastException = e;
                System.out.println("Attempt " + attempt + " failed for operation: " + operationName);
                
                if (attempt < maxRetries) {
                    try {
                        Thread.sleep(1000 * attempt); // Exponential backoff
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }
        
        // All retries failed
        throw new RuntimeException("Operation failed after " + maxRetries + " attempts: " + operationName, lastException);
    }
}