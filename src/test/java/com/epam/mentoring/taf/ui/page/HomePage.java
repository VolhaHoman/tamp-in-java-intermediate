package com.epam.mentoring.taf.ui.page;

import io.qameta.allure.Step;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Random;

public class HomePage extends BasePage {

    private WebElement tag;

    @FindBy(xpath = "//a[contains(@class,'tag-pill')]")
    private List<WebElement> tagPills;
    @FindBy(xpath = "//div[@class='app-article-preview' and not(@hidden)]")
    private WebElement articlePreview;
    @FindBy(xpath = "//a[@class='nav-link active']")
    private WebElement navLink;
    @FindBy(xpath = "//li/a[contains(text(),'Your Feed')]")
    private WebElement userFeedNav;
    @FindBy(xpath = "(//div[@class='row'])[last()]/descendant::div[@class='article-preview']//a[@class='author']")
    private WebElement userFeedAuthor;
    @FindBy(xpath = "//ul[contains(@class,'navbar-nav')]/li[4]/a")
    private WebElement usernameAccountNav;
    @FindBy(xpath = "//a[contains(text(),'Global Feed')]")
    private WebElement globalLink;
    @FindBy(xpath = "//app-article-list/app-article-preview[1]/div/a")
    private WebElement article;
    @FindBy(xpath = "//a[contains(@class,'nav-link')]/img")
    private WebElement userIcon;

    public HomePage(Logger logger) {
        PageFactory.initElements(driver, this);
        this.logger = logger;
    }

    public HomePage(Logger logger, WebDriver driver, WebDriverWait wait) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
        this.wait = wait;
        this.logger = logger;
    }

    @Step("Get username from navigation bar")
    public String getUsernameAccountNav() {
        wait.until(ExpectedConditions.visibilityOf(usernameAccountNav));
        logger.info("Get username from navigation bar: " + usernameAccountNav.getText());
        return usernameAccountNav.getText();
    }

    @Step("Get a tag from side bar")
    public HomePage getTagFromSidebar() {
        wait.until(ExpectedConditions.visibilityOfAllElements(tagPills));
        Random rand = new Random();
        tag = tagPills.get(rand.nextInt(tagPills.size()));
        logger.info("Get all tags from side bar: " + tagPills.size());
        return this;
    }

    @Step("Get a tag name")
    public String getTagText() {
        logger.info("Get a tag from side bar: " + tag.getText());
        return tag.getText();
    }

    @Step("Click on the tag")
    public HomePage clickTag() {
        tag.click();
        logger.info("Click on the tag pill");
        return this;
    }

    @Step("Get the new tab title")
    public String getNavLink() {
        wait.until(ExpectedConditions.visibilityOf(articlePreview));
        logger.info("Get the new tag tab title: " + navLink.getText());
        return navLink.getText();
    }

    @Step("Verify 'Your Feed' exists")
    public String getYourFeedNav() {
        wait.until(ExpectedConditions.visibilityOf(userFeedNav));
        logger.info("Get user feed: " + userFeedNav.getText());
        return userFeedNav.getText();
    }

    @Step("Verify 'Your Feed Author' exists")
    public String getYourFeedAuthor() {
        wait.until(ExpectedConditions.visibilityOf(userFeedAuthor));
        logger.info("Get user feed: " + userFeedAuthor.getText());
        return userFeedAuthor.getText();
    }

    @Step("Click on 'Celeb User' link")
    public HomePage clickCelebUserLink() {
        wait.until(ExpectedConditions.elementToBeClickable(userFeedAuthor));
        userFeedAuthor.click();
        logger.info("Click on 'Celeb User' link");
        return this;
    }

    @Step("Select article")
    public void selectArt() {
        wait.until(ExpectedConditions.visibilityOf(article));
        article.click();
        logger.info("Select article");
    }

    @Step("Navigate to User page")
    public HomePage navToUser() {
        wait.until(ExpectedConditions.visibilityOf(userIcon));
        userIcon.click();
        logger.info("Navigate to User page");
        return this;
    }

    @Step("Navigate to Global feed")
    public HomePage navGlobalFeed() {
        wait.until(ExpectedConditions.visibilityOf(globalLink));
        globalLink.click();
        logger.info("Navigate to Global feed");
        return this;
    }
}
