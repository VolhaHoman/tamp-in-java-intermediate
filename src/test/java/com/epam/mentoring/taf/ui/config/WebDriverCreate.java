package com.epam.mentoring.taf.ui.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.InputStream;

public class WebDriverCreate {

    private static WebDriver driver;

    private static WebDriverWait wait;

    public static WebDriver getWebDriverInstance() {
        return driver;
    }

    public static WebDriverWait getWebDriverWaitInstance() {
        if (wait == null) {
            wait = new WebDriverWait(driver, 2);
        }
        return wait;
    }

    static {
        try {
            BrowserEnum driverType = getDriverType();
            chooseBrowser(driverType);
        } catch (Exception e) {
            setupDriver(getChrome());
        }
    }

    private static BrowserEnum getDriverType() throws ConfigurationException {
        PropertiesConfiguration config = new PropertiesConfiguration();
        InputStream inputStream = WebDriverCreate.class
                .getClassLoader()
                .getResourceAsStream("application.properties");
        config.load(inputStream);
        return BrowserEnum.valueOf(config.getString("driver.for.use"));
    }

    private static void chooseBrowser(BrowserEnum browser) throws Exception {
        switch (browser) {
            case CHROME: {
                setupDriver(getChrome());
                break;
            }
            case FIREFOX: {
                setupDriver(getFireFox());
                break;
            }
            default:
                throw new Exception("Driver configuration not found");
        }
    }

    private static void setupDriver(WebDriver webDriver) {
        if (driver == null) {
            driver = webDriver;
        }
    }

    private static WebDriver getChrome() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        return new ChromeDriver((chromeOptions));
    }

    private static WebDriver getFireFox() {
        FirefoxBinary firefoxBinary = new FirefoxBinary();
        firefoxBinary.addCommandLineOptions("--headless");
        System.setProperty("webdriver.gecko.driver", "geckodriver");
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setBinary(firefoxBinary);
        return new FirefoxDriver(firefoxOptions);
    }
}

