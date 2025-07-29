package execution;


import config.ConfigManager;
import utils.ReportUtils;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.util.ArrayList;
import java.util.List;

public class TestExecutor {
    
    public static void main(String[] args) {
        // Initialize reporting
        ReportUtils.initializeReport();
        
        // Create TestNG suite programmatically
        XmlSuite suite = createTestSuite();
        
        // Run tests
        TestNG testNG = new TestNG();
        List<XmlSuite> suites = new ArrayList<>();
        suites.add(suite);
        testNG.setXmlSuites(suites);
        
        // Add listeners
        testNG.addListener(new listeners.EnhancedTestListener());
        
        // Execute
        testNG.run();
        
        // Generate final report
        ReportUtils.flushReport();
        
        System.out.println("Test execution completed. Check reports in: " + 
            System.getProperty("user.dir") + "/test-output/reports/");
    }
    
    private static XmlSuite createTestSuite() {
        XmlSuite suite = new XmlSuite();
        suite.setName("Flutter App Automated Test Suite");
        suite.setVerbose(1);
        
        // Add parameters
        suite.setParameter("environment", ConfigManager.getProperty("test.environment", "qa"));
        
        // Create smoke test
        XmlTest smokeTest = new XmlTest(suite);
        smokeTest.setName("Smoke Tests");
        List<XmlClass> smokeClasses = new ArrayList<>();
        smokeClasses.add(new XmlClass("tests.SmokeTests"));
        smokeTest.setXmlClasses(smokeClasses);
        
        // Create login test
        XmlTest loginTest = new XmlTest(suite);
        loginTest.setName("Login Tests");
        List<XmlClass> loginClasses = new ArrayList<>();
        loginClasses.add(new XmlClass("tests.LoginTests"));
        loginTest.setXmlClasses(loginClasses);
        
        return suite;
    }
}