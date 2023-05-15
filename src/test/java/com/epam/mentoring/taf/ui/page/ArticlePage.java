package com.epam.mentoring.taf.ui.page;

import com.epam.mentoring.taf.ui.config.WebDriverLoader;
import io.qameta.allure.Step;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ArticlePage extends BasePage {

    public String slug;
    @FindBy(xpath = "//textarea")
    private WebElement commentArea;
    @FindBy(xpath = "//app-article-comment[1]/div/div[contains(@class, 'card-block')]")
    private WebElement cardArea;
    @FindBy(xpath = "//button[contains(text(),'Post Comment')]")
    private WebElement sendBtn;
    @FindBy(xpath = "(//span/i[contains(@class, 'ion-trash-a')])[1]")
    private WebElement trashBtn;
    @FindBy(xpath = "//ul[contains(@class, 'error-messages')]")
    private WebElement errorMsg;
    @FindBy(xpath = "//h1")
    private WebElement articleTitle;
    @FindBy(xpath = "//div[contains(@class, 'row article-content')]/div[contains(@class, 'col-md-12')]/div")
    private WebElement articleBody;
    @FindBy(xpath = "//a[contains(text(),'Edit Article')]")
    private WebElement editArticleBtn;
    @FindBy(xpath = "//button[contains(text(),'Delete Article')]")
    private WebElement deleteArticleBtn;
    @FindBy(xpath = "//li[contains(@class,'tag-outline')]")
    private WebElement tagPill;

    public ArticlePage(Logger logger, WebDriverLoader loader) {
        PageFactory.initElements(loader.getWebDriver(), this);
        this.loader = loader;
        this.logger = logger;
    }

    @Step("Enter the comment")
    public ArticlePage enterComment(String text) {
        loader.getWebDriverWait().until(ExpectedConditions.visibilityOf(commentArea));
        commentArea.sendKeys(text);
        logger.info("Enter the following comment: " + text);
        return this;
    }

    @Step("Click on 'Send comment' button")
    public void clickSendCommentBtn() {
        loader.getWebDriverWait().until(ExpectedConditions.elementToBeClickable(sendBtn));
        sendBtn.click();
        logger.info("Send comment");
    }

    @Step("Click on 'Delete comment' button")
    public void clickDeleteCommentBtn() {
        loader.getWebDriverWait().until(ExpectedConditions.visibilityOf(trashBtn));
        trashBtn.click();
        logger.info("Delete comment");
    }

    @Step("Get text of comment")
    public String getComment() {
        loader.getWebDriverWait().until(ExpectedConditions.visibilityOf(cardArea));
        logger.info("The following comment is added: " + cardArea.getText());
        return cardArea.getText();
    }

    @Step("Get error message")
    public String getError() {
        loader.getWebDriverWait().until(ExpectedConditions.visibilityOf(errorMsg));
        logger.info("The following error appears: " + errorMsg.getText());
        return errorMsg.getText();
    }

    @Step("Verify that comment is empty")
    public boolean commentIsNotDisplayed() {
        return !cardArea.isDisplayed();
    }

    @Step("Get the article's title")
    public String getArticleTitle() {
        loader.getWebDriverWait().until(ExpectedConditions.visibilityOf(articleTitle));
        logger.info("The following title is displayed: " + articleTitle.getText());
        return articleTitle.getText();
    }

    @Step("Get the article's text")
    public String getArticleBody() {
        loader.getWebDriverWait().until(ExpectedConditions.visibilityOf(articleBody));
        logger.info("The following article text is displayed: " + articleBody.getText());
        return articleBody.getText();
    }

    @Step("Get the article's tag")
    public String getArticleTag() {
        loader.getWebDriverWait().until(ExpectedConditions.visibilityOf(tagPill));
        logger.info("The following tag is displayed: " + tagPill.getText());
        return tagPill.getText();
    }

    @Step("Click on 'Edit Article' button")
    public void clickEditArticleBtn() {
        loader.getWebDriverWait().until(ExpectedConditions.visibilityOf(editArticleBtn));
        editArticleBtn.click();
        logger.info("Edit an article");
    }

    @Step("Click on 'Delete Article' button")
    public void clickDeleteArticleBtn() {
        loader.getWebDriverWait().until(ExpectedConditions.visibilityOf(deleteArticleBtn));
        deleteArticleBtn.click();
        logger.info("Delete an article");
    }

    @Step("Get the article's slug from URL")
    public String getSlugFromUrl() {
        loader.getWebDriverWait().until(ExpectedConditions.visibilityOf(articleTitle));
        slug = loader.getWebDriver().getCurrentUrl().replace("https://angular.realworld.io/article/", "");
        logger.info("The slug to delete is: " + slug);
        return slug;
    }

}
