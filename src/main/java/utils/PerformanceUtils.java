package utils;


import framework.base.DriverManager;
import io.appium.java_client.AppiumDriver;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PerformanceUtils {
    private static Map<String, Long> performanceMetrics = new ConcurrentHashMap<>();
    
    public static void startTimer(String metricName) {
        performanceMetrics.put(metricName + "_start", System.currentTimeMillis());
    }
    
    public static long stopTimer(String metricName) {
        Long startTime = performanceMetrics.get(metricName + "_start");
        if (startTime != null) {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            performanceMetrics.put(metricName + "_duration", duration);
            return duration;
        }
        return -1;
    }
    
    public static long getMetric(String metricName) {
        return performanceMetrics.getOrDefault(metricName + "_duration", -1L);
    }
    
    public static void logAppStartupTime() {
        try {
            AppiumDriver driver = DriverManager.getDriver();
            // This is a simplified example - actual implementation would use performance APIs
            Map<String, Object> performance = (Map<String, Object>) driver.executeScript(
                "mobile: getPerformanceData", 
                Map.of("packageName", "com.example.flutter.qa", "dataType", "cpuinfo")
            );
            
            System.out.println("App Performance Data: " + performance);
        } catch (Exception e) {
            System.err.println("Failed to collect performance data: " + e.getMessage());
        }
    }
    
    public static void logMemoryUsage() {
        try {
            AppiumDriver driver = DriverManager.getDriver();
            Map<String, Object> memoryInfo = (Map<String, Object>) driver.executeScript(
                "mobile: getPerformanceData",
                Map.of("packageName", "com.example.flutter.qa", "dataType", "memoryinfo")
            );
            
            System.out.println("Memory Usage: " + memoryInfo);
        } catch (Exception e) {
            System.err.println("Failed to collect memory data: " + e.getMessage());
        }
    }
    
    public static void printAllMetrics() {
        System.out.println("\n=== Performance Metrics ===");
        performanceMetrics.entrySet().stream()
            .filter(entry -> entry.getKey().endsWith("_duration"))
            .forEach(entry -> {
                String metricName = entry.getKey().replace("_duration", "");
                System.out.printf("%s: %d ms%n", metricName, entry.getValue());
            });
        System.out.println("============================\n");
    }
}