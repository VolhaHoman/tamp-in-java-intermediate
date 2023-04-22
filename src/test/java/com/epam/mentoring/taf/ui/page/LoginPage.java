package com.epam.mentoring.taf.ui.page;

import io.qameta.allure.Step;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {

    @FindBy(xpath = "//input[@placeholder='Email']")
    public WebElement emailField;

    @FindBy(xpath = "//input[@placeholder='Password']")
    public WebElement passwordField;

    @FindBy(xpath = "//button[contains(text(),'Sign in')]")
    public WebElement signInButton;

    @FindBy(xpath = "//ul[@class='error-messages']/li")
    public WebElement invalidCredentialsMessage;

    public static final String CREDENTIALS_ERROR_TEXT = "email or password is invalid";

    public LoginPage(Logger logger) {
        PageFactory.initElements(driver, this);
        this.logger = logger;
    }

    @Step("Get invalid credentials message")
    public String getInvalidCredentialsMessage() {
        wait.until(ExpectedConditions.visibilityOf(invalidCredentialsMessage));
        logger.info("Appear error message: '" + invalidCredentialsMessage.getText() + "'");
        return invalidCredentialsMessage.getText();
    }

    @Step("Fill in email {0}")
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

    @Step("Click on 'Sign In' button")
    public LoginPage clickSignInBtn() {
        signInButton.click();
        logger.info("Click on 'Sign In' button");
        return this;
    }

}
