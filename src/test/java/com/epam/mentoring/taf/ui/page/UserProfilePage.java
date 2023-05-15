package com.epam.mentoring.taf.ui.page;

import com.epam.mentoring.taf.ui.config.WebDriverLoader;
import io.qameta.allure.Step;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

public class UserProfilePage extends BasePage {

    @FindBy(xpath = "//app-article-list/app-article-preview[last()]/div/a")
    private WebElement article;

    @FindBy(xpath = "//*[contains(text(),'No articles are here')]")
    private WebElement noArticlesHereMessage;

    public UserProfilePage(Logger logger, WebDriverLoader loader) {
        PageFactory.initElements(loader.getWebDriver(), this);
        this.loader = loader;
        this.logger = logger;
    }

    public boolean checkNoArticleMessage() {
        try {
            return loader.getWebDriverWait()
                    .withTimeout(Duration.ofSeconds(3))
                    .until(driver -> noArticlesHereMessage.isDisplayed());
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isPresentByXpath(String xPath) {
        try {
            return loader.getWebDriverWait()
                    .withTimeout(Duration.ofSeconds(3))
                    .until(driver -> driver.findElement(By.xpath(xPath)).isDisplayed());
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Select article")
    public void selectArt() {
        loader.getWebDriverWait().until(ExpectedConditions.visibilityOf(article));
        article.click();
        logger.info("Select article");
    }

}
