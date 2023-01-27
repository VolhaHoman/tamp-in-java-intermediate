package com.epam.mentoring.taf.ui.config;

import com.epam.mentoring.taf.exception.ConfigurationSetupException;
import com.epam.mentoring.taf.exception.ParameterIsNullException;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class WebDriverCreate {

    private static WebDriver driver;

    private static WebDriverWait wait;

    public static WebDriver getWebDriverInstance() {
        initProps();
        return driver;
    }

    public static WebDriverWait getWebDriverWaitInstance() {
        initProps();
        if (wait == null) {
            wait = new WebDriverWait(getWebDriverInstance(), 2);
        }
        return wait;
    }

    public static void initDriver() {
        try {
            chooseBrowser(getDriverType());
        } catch (ParameterIsNullException | ConfigurationException e) {
            throw new ConfigurationSetupException("Error when loading driver configuration properties", e);
        }
    }

    private static String getDriverType() throws ConfigurationException {
        try (InputStream inputStream = WebDriverCreate.class
                .getClassLoader()
                .getResourceAsStream("application.properties")){
            PropertiesConfiguration config = new PropertiesConfiguration();
            config.load(inputStream);

            String driverForUse = config.getString("driver.for.use");

            return driverForUse != null ? driverForUse : "";
        } catch (IOException e) {
            throw new ConfigurationException("Failed to load configuration", e);
        }
    }

    private static void chooseBrowser(String browser) throws ParameterIsNullException {
        switch (browser) {
            case "CHROME": {
                setupDriver(getChrome());
                break;
            }
            case "FIREFOX": {
                setupDriver(getFireFox());
                break;
            }
            default:
                throw new ParameterIsNullException("Driver configuration not found");
        }
    }

    private static void setupDriver(WebDriver webDriver) {
        driver = webDriver;
    }

    private static WebDriver getChrome() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        return new ChromeDriver((chromeOptions));
    }

    private static WebDriver getFireFox() {
        FirefoxBinary firefoxBinary = new FirefoxBinary();
        System.setProperty("webdriver.gecko.driver", "geckodriver");
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setBinary(firefoxBinary);
        return new FirefoxDriver(firefoxOptions);
    }

    private static void initProps() {
        if (Objects.isNull(driver) || isDriverSessionNotPresent()) {
            initDriver();
            wait = new WebDriverWait(getWebDriverInstance(), 2);
        }
    }

    private static boolean isDriverSessionNotPresent() {
        return ((RemoteWebDriver)driver).getSessionId() == null;
    }
}

