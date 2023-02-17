package com.epam.mentoring.taf;

import com.epam.mentoring.taf.listeners.TestListener;
import com.epam.mentoring.taf.ui.config.WebDriverCreate;
import com.epam.mentoring.taf.util.Redirection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

@Listeners({TestListener.class})
abstract public class AbstractTest {

    protected final static String baseUrl = "https://angular.realworld.io";
    public final static String API_URL = "https://conduit.productionready.io";
    public static final String API_USERS = "https://api.realworld.io/api/users";
    public static final String LOGIN_URL = "/api/users/login";
    public static final String API_LOGIN = API_URL + LOGIN_URL;
    public final Logger logger = LogManager.getRootLogger();
    protected WebDriver driver;
    protected WebDriverWait wait;

    Redirection redirection = new Redirection();

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
