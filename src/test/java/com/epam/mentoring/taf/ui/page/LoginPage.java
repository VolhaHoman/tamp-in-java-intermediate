package com.epam.mentoring.taf.ui.page;

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

    public String getInvalidCredentialsMessage() {
        wait.until(ExpectedConditions.visibilityOf(invalidCredentialsMessage));
        return invalidCredentialsMessage.getText();
    }

    public void fillInEmail() {
        emailField.sendKeys(DEFAULT_EMAIL);
    }

    public void fillInPassword(String password) {
        passwordField.sendKeys(password);
    }

    public void clickSignInLink() {
        driver.get(baseUrl);
        signInLink.click();
    }

    public void clickSignInBtn() {
        signInButton.click();
    }
}
