package config;


import org.openqa.selenium.remote.DesiredCapabilities;
import java.io.File;

public class FlutterCapabilities {
    
    public static DesiredCapabilities getAndroidCapabilities() {
        DesiredCapabilities caps = new DesiredCapabilities();
        
        // Basic Android capabilities
        caps.setCapability("platformName", "Android");
        caps.setCapability("platformVersion", ConfigManager.getProperty("android.version"));
        caps.setCapability("deviceName", ConfigManager.getProperty("device.name"));
        caps.setCapability("automationName", "Flutter");
        
        // App configuration
        String apkPath = System.getProperty("user.dir") + "/src/test/resources/apk/app-qa-release-universal.apk";
        caps.setCapability("app", new File(apkPath).getAbsolutePath());
        
        // Flutter specific capabilities
        caps.setCapability("autoGrantPermissions", true);
        caps.setCapability("noReset", false);
        caps.setCapability("fullReset", false);
        caps.setCapability("newCommandTimeout", 300);
        
        // Performance capabilities
        caps.setCapability("skipServerInstallation", true);
        caps.setCapability("skipDeviceInitialization", true);
        
        return caps;
    }
}