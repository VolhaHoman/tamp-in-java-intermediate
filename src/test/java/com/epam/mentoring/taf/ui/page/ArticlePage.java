package com.epam.mentoring.taf.ui.page;

import io.qameta.allure.Step;
import org.apache.logging.log4j.Logger;

import org.openqa.selenium.WebDriver;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import org.openqa.selenium.support.ui.WebDriverWait;


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

    public ArticlePage(Logger logger, WebDriver driver, WebDriverWait wait) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
        this.wait = wait;
        this.logger = logger;
    }

    @Step("Enter the comment")
    public ArticlePage enterComment(String text) {
        wait.until(ExpectedConditions.visibilityOf(commentArea));
        commentArea.sendKeys(text);
        logger.info("Enter the following comment: " + text);
        return this;
    }

    @Step("Click on 'Send comment' button")
    public void clickSendCommentBtn() {
        wait.until(ExpectedConditions.visibilityOf(sendBtn));
        sendBtn.click();
        logger.info("Send comment");
    }

    @Step("Click on 'Delete comment' button")
    public void clickDeleteCommentBtn() {
        wait.until(ExpectedConditions.visibilityOf(trashBtn));
        trashBtn.click();
        logger.info("Delete comment");
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
    public boolean commentIsNotDisplayed() {
        return !cardArea.isDisplayed();
    }

}



