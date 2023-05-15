package com.epam.mentoring.taf.ui.config;

import com.epam.mentoring.taf.exception.ConfigurationSetupException;
import com.epam.mentoring.taf.exception.ParameterIsNullException;
import com.epam.mentoring.taf.service.YamlReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.naming.ConfigurationException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class WebDriverLoader {

    private WebDriver driver;

    private WebDriverWait wait;

    public WebDriver getWebDriver() {
        initProps();
        return driver;
    }

    public WebDriverWait getWebDriverWait() {
        initProps();
        if (wait == null) {
            wait = new WebDriverWait(getWebDriver(), 15);
        }
        return wait;
    }

    public void initDriver() {
        try {
            YamlReader driver = new YamlReader();
            String driverType = driver.readBrowser();
            chooseBrowser(driverType);
        } catch (ParameterIsNullException e) {
            throw new ConfigurationSetupException("Error when loading driver configuration properties", e);
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    private void chooseBrowser(String browser) throws ParameterIsNullException {
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

    private void setupDriver(WebDriver webDriver) {
        driver = webDriver;
    }

    private URL getGridUrl() {
        try {
            String gridUrl = System.getProperty("grid.url");
            if (gridUrl != null && !gridUrl.isBlank()) {
                return new URL(gridUrl);
            }
            return null;
        } catch (MalformedURLException e) {
            return null;
        }
    }

    private WebDriver getChrome() {
        URL gridUrl = getGridUrl();
        if (Objects.nonNull(gridUrl)) {
            return new RemoteWebDriver(gridUrl, DesiredCapabilities.chrome());
        }
        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        return new ChromeDriver((chromeOptions));
    }

    private WebDriver getFireFox() {
        URL gridUrl = getGridUrl();
        if (Objects.nonNull(gridUrl)) {
            return new RemoteWebDriver(gridUrl, DesiredCapabilities.firefox());
        }

        FirefoxBinary firefoxBinary = new FirefoxBinary();
        System.setProperty("webdriver.gecko.driver", "geckodriver");
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setBinary(firefoxBinary);
        return new FirefoxDriver(firefoxOptions);
    }

    private void initProps() {
        if (Objects.isNull(driver) || isDriverSessionNotPresent()) {
            initDriver();
            wait = new WebDriverWait(getWebDriver(), 15);
        }
    }

    private boolean isDriverSessionNotPresent() {
        return ((RemoteWebDriver) driver).getSessionId() == null;
    }
}

