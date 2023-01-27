package com.epam.mentoring.taf;

import com.epam.mentoring.taf.ui.config.WebDriverCreate;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;

abstract public class AbstractTest {

    protected final static String baseUrl = "https://angular.realworld.io";
    protected final static String UI_URL = "https://angular.realworld.io";
    protected final static String API_URL = "https://conduit.productionready.io";

    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeMethod
    public void initialisation() {
        // TODO: Remove after migration to Page Object Pattern.
        driver = WebDriverCreate.getWebDriverInstance();
        wait = WebDriverCreate.getWebDriverWaitInstance();

        driver.get(baseUrl);
    }

    @AfterClass
    public void terminate() {
        driver.quit();
    }
}
