package com.epam.mentoring.taf.ui.page;

import com.epam.mentoring.taf.ui.config.WebDriverLoader;
import io.qameta.allure.Step;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CelebPage extends BasePage {

    public static final String PAGE_TITLE = "Conduit";

    @FindBy(xpath = "//h4[text()='celeb_Hamster_Boss']")
    private WebElement authorName;

    @FindBy(xpath = "//li[@class='nav-item']/a[contains(text(),'My Posts')]")
    private WebElement authorPostsNav;

    @FindBy(xpath = "//button[contains(text(),'Unfollow celeb_Hamster_Boss')]")
    private WebElement unfollowBtn;

    public CelebPage(Logger logger, WebDriverLoader loader) {
        PageFactory.initElements(loader.getWebDriver(), this);
        this.loader = loader;
        this.logger = logger;
    }

    @Step("Wait until the page is loaded")
    public String waitPageIsLoaded() throws InterruptedException {
        loader.getWebDriverWait().until(ExpectedConditions.titleIs(PAGE_TITLE));
        loader.getWebDriverWait().until(ExpectedConditions.visibilityOf(authorName));
        logger.info("Page title is: " + PAGE_TITLE);
        return PAGE_TITLE;
    }

    @Step("Get username from navigation bar")
    public String getAuthorName() {
        loader.getWebDriverWait().until(ExpectedConditions.visibilityOf(authorName));
        logger.info("Get username from navigation bar: " + authorName.getText());
        return authorName.getText();
    }

    @Step("Get username from navigation bar")
    public String getAuthorPostsNav() {
        loader.getWebDriverWait().until(ExpectedConditions.visibilityOf(authorPostsNav));
        logger.info("Get username from navigation bar: " + authorPostsNav.getText());
        return authorPostsNav.getText();
    }

    @Step("Verify unfollow button is present")
    public String checkUnfollowBtn() {
        loader.getWebDriverWait().until(ExpectedConditions.visibilityOf(unfollowBtn));
        logger.info("Unfollow button has text: " + unfollowBtn.getText());
        return unfollowBtn.getText();
    }

}
