package com.epam.mentoring.taf.ui.page;

import io.qameta.allure.Step;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SettingsPage extends BasePage {

    @FindBy(xpath = "//button[contains(text(),'Or click here to logout.')]")
    public WebElement logoutBtn;

    public SettingsPage(Logger logger, WebDriver driver, WebDriverWait wait) {
        PageFactory.initElements(driver, this);
        this.wait = wait;
        this.driver = driver;
        this.logger = logger;
    }

    @Step("Log out")
    public SettingsPage logout() {
        wait.until(ExpectedConditions.visibilityOf(logoutBtn));
        logoutBtn.click();
        logger.info("Log out");
        return this;
    }

}
