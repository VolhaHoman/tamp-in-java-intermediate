package com.epam.mentoring.taf.ui.page;

import com.epam.mentoring.taf.ui.config.WebDriverLoader;
import io.qameta.allure.Step;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {

    public static final String CREDENTIALS_ERROR_TEXT = "email or password is invalid";
    @FindBy(xpath = "//input[@placeholder='Email']")
    private WebElement emailField;
    @FindBy(xpath = "//input[@placeholder='Password']")
    private WebElement passwordField;
    @FindBy(xpath = "//button[contains(text(),'Sign in')]")
    private WebElement signInButton;
    @FindBy(xpath = "//ul[@class='error-messages']/li")
    private WebElement invalidCredentialsMessage;

    public LoginPage(Logger logger, WebDriverLoader loader) {
        PageFactory.initElements(loader.getWebDriver(), this);
        this.loader = loader;
        this.logger = logger;
    }

    @Step("Get invalid credentials message")
    public String getInvalidCredentialsMessage() {
        loader.getWebDriverWait().until(ExpectedConditions.visibilityOf(invalidCredentialsMessage));
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

    @Step("Click on 'Sign In' button")
    public void clickSignInBtn() {
        signInButton.click();
        logger.info("Click on 'Sign In' button");
    }

}
