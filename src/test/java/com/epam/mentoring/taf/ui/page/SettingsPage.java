package com.epam.mentoring.taf.ui.page;

import com.epam.mentoring.taf.ui.config.WebDriverLoader;
import io.qameta.allure.Step;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class SettingsPage extends BasePage {

    @FindBy(xpath = "//button[contains(text(),'Or click here to logout.')]")
    private WebElement logoutBtn;

    public SettingsPage(Logger logger, WebDriverLoader loader) {
        PageFactory.initElements(loader.getWebDriver(), this);
        this.loader = loader;
        this.logger = logger;
    }

    @Step("Click 'Log out' button")
    public void clickLogOutBtn() {
        loader.getWebDriverWait().until(ExpectedConditions.visibilityOf(logoutBtn));
        logoutBtn.click();
        logger.info("Log out");
    }

}
