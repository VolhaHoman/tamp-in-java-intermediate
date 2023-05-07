package com.epam.mentoring.taf.ui.page;

import io.qameta.allure.Step;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
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

    @FindBy(xpath = "//a[contains(@class,'nav-link')]/img")
    private WebElement userIcon;

    @FindBy(xpath = "//a[contains(text(),'New Article')]")
    private WebElement newArticle;

    @FindBy(xpath = "//li/a[contains(text(),'Settings')]")
    private WebElement settingNav;

    @FindBy(xpath = "//li/a[contains(text(),'Sign up')]")
    private WebElement signUpLink;

    @FindBy(xpath = "//li/a[contains(text(),'Sign in')]")
    private WebElement signInLink;

    @FindBy(xpath = "//h1")
    private WebElement articleTitle;

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

    @Step("Navigate to User page")
    public void navToUser() {
        wait.until(ExpectedConditions.visibilityOfAllElements(tagPills));
        userIcon.click();
        logger.info("Navigate to User page");
    }

    @Step("Navigate to Global feed")
    public HomePage navGlobalFeed() {
        wait.until(ExpectedConditions.visibilityOf(globalLink));
        globalLink.click();
        logger.info("Navigate to Global feed");
        return this;
    }

    @Step("Navigate to AppEditor page")
    public void navToEditorPage() {
        wait.until(ExpectedConditions.visibilityOf(newArticle));
        newArticle.click();
        logger.info("Navigate to AppEditor page");
    }

    @Step("Navigate to Setting page")
    public void navToSetting() {
        wait.until(ExpectedConditions.visibilityOf(settingNav));
        settingNav.click();
        logger.info("Navigate to Setting page");
    }

    @Step("Click on 'Sign In' link")
    public void clickSignInLink() {
        signInLink.click();
        logger.info("Click on 'Sign In' link");
    }

    @Step("Click on 'Sign Up' link")
    public void clickSignUpLink() {
        signUpLink.click();
        logger.info("Click on 'Sign Up' link");
    }

    @Step("Verify that user is logged in")
    public boolean userIconIsDisplayed() {
        try {
            return userIcon.isDisplayed();
        }
        catch(NoSuchElementException e) {
            return false;
        }
    }

    @Step("Get an article title")
    public String getArticleTitle() {
        wait.until(ExpectedConditions.visibilityOf(articleTitle));
        logger.info("The following title of the first article is displayed: " + articleTitle.getText());
        return articleTitle.getText();
    }

    @Step("Click on the article")
    public void clickArticleLink() {
        articleTitle.click();
        logger.info("Select article");
    }

}
