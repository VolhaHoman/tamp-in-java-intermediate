package com.epam.mentoring.taf.ui.page;

import io.qameta.allure.Step;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

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

    @FindBy(xpath = "//li/a[contains(text(),'Sign up')]")
    public WebElement signUpLink;

    @FindBy(xpath = "//input[@placeholder='Username']")
    public WebElement usernameField;

    @FindBy(xpath = "//button[contains(text(),'Sign up')]")
    public WebElement signUpButton;

    private final String baseUrl;

    public LoginPage(String baseUrl, Logger logger) {
        this.baseUrl = baseUrl;
        PageFactory.initElements(driver, this);
        this.logger = logger;
    }

    @Step("Get invalid credentials message")
    public String getInvalidCredentialsMessage() {
        wait.until(ExpectedConditions.visibilityOf(invalidCredentialsMessage));
        logger.info("Appear error message: '" + invalidCredentialsMessage.getText() + "'");
        return invalidCredentialsMessage.getText();
    }

    @Step("Fill in email")
    public LoginPage fillInEmail(String email) {
        emailField.sendKeys(email);
        logger.info("Fill in email: " + email);
        return this;
    }

    @Step("Fill in password {0}")
    public LoginPage fillInPassword(String password) {
        passwordField.sendKeys(password);
        logger.info("Fill in password: " + password);
        return this;
    }

    @Step("Click on 'Sign In' link")
    public LoginPage clickSignInLink() {
        driver.get(baseUrl);
        signInLink.click();
        logger.info("Click on 'Sign In' link");
        return this;
    }

    @Step("Click on 'Sign In' button")
    public LoginPage clickSignInBtn() {
        signInButton.click();
        logger.info("Click on 'Sign In' button");
        return this;
    }

    @Step("Click on 'Sign Up' link")
    public LoginPage clickSignUpLink() {
        driver.get(baseUrl);
        signUpLink.click();
        logger.info("Click on 'Sign Up' link");
        return this;
    }

    @Step("Click on 'Sign Up' button")
    public LoginPage clickSignUpBtn() {
        signUpButton.click();
        logger.info("Click on 'Sign Up' button");
        return this;
    }

    @Step("Fill in username")
    public LoginPage fillInUsername(String username) {
        usernameField.sendKeys(username);
        logger.info("Fill in username: " + username);
        return this;
    }

}
