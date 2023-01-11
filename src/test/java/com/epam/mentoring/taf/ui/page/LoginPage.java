package com.epam.mentoring.taf.ui.page;

import com.epam.mentoring.taf.ui.config.WebDriverCreate;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static com.epam.mentoring.taf.data.UserData.DEFAULT_EMAIL;

public class LoginPage {

    public static final By SIGN_IN_LINK = By.xpath("//li/a[contains(text(),'Sign in')]");
    public static final By EMAIL_FIELD = By.xpath("//input[@placeholder='Email']");
    public static final By PASSWORD_FIELD = By.xpath("//input[@placeholder='Password']");
    public static final By SIGN_IN_BTN = By.xpath("//button[contains(text(),'Sign in')]");
    public static final By INVALID_CREDENTIALS_MESSAGE = By.xpath("//ul[@class='error-messages']/li");
    public static final String CREDENTIALS_ERROR_TEXT = "email or password is invalid";

    private final WebDriver driver = WebDriverCreate.getWebDriverInstance();

    private final String baseUrl;

    public LoginPage(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void signIn(String password) {
        driver.get(baseUrl);
        performClick(SIGN_IN_LINK);
        fillInInput(EMAIL_FIELD, DEFAULT_EMAIL);
        fillInInput(PASSWORD_FIELD, password);
        performClick(SIGN_IN_BTN);
    }

    private void fillInInput(By locator, String value) {
        driver.findElement(locator).sendKeys(value);
    }

    private void performClick(By locator) {
        driver.findElement(locator).click();
    }
}
