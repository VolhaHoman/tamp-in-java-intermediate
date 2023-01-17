package com.epam.mentoring.taf.ui.page;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static com.epam.mentoring.taf.data.UserData.DEFAULT_EMAIL;

public class LoginPage extends BasePage {

    @FindBy(xpath = "//li/a[contains(text(),'Sign in')]")
    private WebElement signInLink;

    @FindBy(xpath = "//input[@placeholder='Email']")
    private WebElement emailField;

    @FindBy(xpath = "//input[@placeholder='Password']")
    private WebElement passwordField;

    @FindBy(xpath = "//button[contains(text(),'Sign in')]")
    private WebElement signInButton;

    @FindBy(xpath = "//ul[@class='error-messages']/li")
    private WebElement invalidCredentialsMessage;

    public static final String CREDENTIALS_ERROR_TEXT = "email or password is invalid";

    private final String baseUrl;

    public LoginPage(String baseUrl) {
        this.baseUrl = baseUrl;
        PageFactory.initElements(driver, this);
    }

    public String getInvalidCredentialsMessage() {
        wait.until(ExpectedConditions.visibilityOf(invalidCredentialsMessage));
        return invalidCredentialsMessage.getText();
    }

    public void signIn(String password) {
        driver.get(baseUrl);
        performClick(signInLink);
        fillInInput(emailField, DEFAULT_EMAIL);
        fillInInput(passwordField, password);
        performClick(signInButton);
    }

    private void fillInInput(WebElement webElement, String value) {
        webElement.sendKeys(value);
    }

    private void performClick(WebElement webElement) {
        webElement.click();
    }
}
