package com.epam.mentoring.taf.ui.page;

import io.qameta.allure.Step;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class RegisterPage extends BasePage {

    @FindBy(xpath = "//input[@placeholder='Username']")
    public WebElement usernameField;

    @FindBy(xpath = "//input[@placeholder='Email']")
    public WebElement emailField;

    @FindBy(xpath = "//input[@placeholder='Password']")
    public WebElement passwordField;

    @FindBy(xpath = "//button[contains(text(),'Sign up')]")
    public WebElement signUpButton;

    public RegisterPage(Logger logger) {
        PageFactory.initElements(driver, this);
        this.logger = logger;
    }

    @Step("Fill in username")
    public RegisterPage fillInUsername(String username) {
        usernameField.sendKeys(username);
        logger.info("Fill in username: " + username);
        return this;
    }

    @Step("Fill in email")
    public RegisterPage fillInEmail(String email) {
        emailField.sendKeys(email);
        logger.info("Fill in email: " + email);
        return this;
    }

    @Step("Fill in password {0}")
    public RegisterPage fillInPassword(String password) {
        passwordField.sendKeys(password);
        logger.info("Fill in password: " + password);
        return this;
    }

    @Step("Click on 'Sign Up' button")
    public void clickSignUpBtn() {
        signUpButton.click();
        logger.info("Click on 'Sign Up' button");
    }
}
