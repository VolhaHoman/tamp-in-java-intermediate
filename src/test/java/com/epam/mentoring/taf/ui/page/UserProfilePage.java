package com.epam.mentoring.taf.ui.page;

import io.qameta.allure.Step;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class UserProfilePage extends BasePage {

    @FindBy(xpath = "//app-article-list/app-article-preview[1]/div/a")
    private WebElement article;

    public UserProfilePage(Logger logger) {
        PageFactory.initElements(driver, this);
        this.logger = logger;
    }

    @Step("Select article")
    public void selectArt() {
        wait.until(ExpectedConditions.visibilityOf(article));
        article.click();
        logger.info("Select article");
    }
}
