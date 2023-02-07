package com.epam.mentoring.taf.ui.page;

import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static com.epam.mentoring.taf.data.UserData.DEFAULT_EMAIL;

public class LoginPage extends BasePage {

    @FindBy(xpath = "//li/a[contains(text(),'Sign in')]")
    public WebElement signInLink;

    @FindBy(xpath = "//input[@placeholder='Email']")
    public WebElement emailField;

    @FindBy(xpath = "//input[@placeholder='Password']")
    public WebElement passwordField;

    @FindBy(xpath = "//button[contains(text(),'Sign in')]")
    public WebElement signInButton;

    @FindBy(xpath = "//ul[@class='error-messages']/li")
    public WebElement invalidCredentialsMessage;

    public static final String CREDENTIALS_ERROR_TEXT = "email or password is invalid";

    private final String baseUrl;

    public LoginPage(String baseUrl) {
        this.baseUrl = baseUrl;
        PageFactory.initElements(driver, this);
    }

    @Step("Get invalid credentials message")
    public String getInvalidCredentialsMessage() {
        wait.until(ExpectedConditions.visibilityOf(invalidCredentialsMessage));
        return invalidCredentialsMessage.getText();
    }

    @Step("Fill in email")
    public void fillInEmail() {
        emailField.sendKeys(DEFAULT_EMAIL);
    }

    @Step("Fill in password {0}")
    public void fillInPassword(String password) {
        passwordField.sendKeys(password);
    }

    @Step("Click on Sign In link")
    public void clickSignInLink() {
        driver.get(baseUrl);
        signInLink.click();
    }

    @Step("Click on Sign In button")
    public void clickSignInBtn() {
        signInButton.click();
    }
}
