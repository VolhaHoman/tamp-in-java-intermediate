package com.epam.mentoring.taf.ui.page;

import io.qameta.allure.Step;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ArticlePage extends BasePage {
    @FindBy(xpath = "//textarea")
    private WebElement commentArea;

    @FindBy(xpath = "//app-article-comment[1]/div/div[contains(@class, 'card-block')]")
    private WebElement cardArea;

    @FindBy(xpath = "//button[contains(text(),'Post Comment')]")
    public WebElement sendBtn;

    @FindBy(xpath = "(//span/i[contains(@class, 'ion-trash-a')])[1]")
    public WebElement trashBtn;

    @FindBy(xpath = "//ul[contains(@class, 'error-messages')]")
    public WebElement errorMsg;

    @FindBy(xpath = "//h1")
    private WebElement articleTitle;

    @FindBy(xpath = "//div[contains(@class, 'row article-content')]/div[contains(@class, 'col-md-12')]/div")
    private WebElement articleBody;

    @FindBy(xpath = "//a[contains(text(),'Edit Article')]")
    private WebElement editArticleBtn;

    @FindBy(xpath = "//button[contains(text(),'Delete Article')]")
    private WebElement deleteArticleBtn;

    public ArticlePage(Logger logger) {
        PageFactory.initElements(driver, this);
        this.logger = logger;
    }

    @Step("Enter the comment")
    public ArticlePage enterComment(String text) {
        wait.until(ExpectedConditions.visibilityOf(commentArea));
        commentArea.sendKeys(text);
        logger.info("Enter the following comment: " + text);
        return this;
    }

    @Step("Send comment")
    public ArticlePage sendComment() {
        wait.until(ExpectedConditions.visibilityOf(sendBtn));
        sendBtn.click();
        logger.info("Send comment");
        return this;
    }

    @Step("Delete comment")
    public ArticlePage deleteComment() {
        wait.until(ExpectedConditions.visibilityOf(trashBtn));
        trashBtn.click();
        logger.info("Delete comment");
        return this;
    }

    @Step("Get text of comment")
    public String getComment() {
        wait.until(ExpectedConditions.visibilityOf(cardArea));
        logger.info("The following comment is added: " + cardArea.getText());
        return cardArea.getText();
    }

    @Step("Get error message")
    public String getError() {
        wait.until(ExpectedConditions.visibilityOf(errorMsg));
        logger.info("The following error appears: " + errorMsg.getText());
        return errorMsg.getText();
    }

    @Step("Verify that comment is empty")
    public boolean commentIsNotPresent() {
        cardArea.isDisplayed();
        logger.info("Comment is not displayed");
        return true;
    }

    @Step("Get the article's title")
    public String getArticleTitle() {
        wait.until(ExpectedConditions.visibilityOf(articleTitle));
        logger.info("The following title is displayed: " + articleTitle.getText());
        return articleTitle.getText();
    }

    @Step("Get the article's text")
    public String getArticleBody() {
        wait.until(ExpectedConditions.visibilityOf(articleBody));
        logger.info("The following article text is displayed: " + articleBody.getText());
        return articleBody.getText();
    }

    @Step("Get the article's text")
    public void clickEditArticleBtn() {
        wait.until(ExpectedConditions.visibilityOf(editArticleBtn));
        editArticleBtn.click();
        logger.info("Edit an article");
    }

    @Step("Click on 'Delete Article' button")
    public void clickDeleteArticleBtn() {
        wait.until(ExpectedConditions.visibilityOf(deleteArticleBtn));
        deleteArticleBtn.click();
        logger.info("Delete an article");
    }
}

