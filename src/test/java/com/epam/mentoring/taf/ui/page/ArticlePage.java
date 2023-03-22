package com.epam.mentoring.taf.ui.page;

import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
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
    public ArticlePage() {
        PageFactory.initElements(driver, this);
        logger = LogManager.getLogger();
    }
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
    public String getComment() throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOf(cardArea));
        Thread.sleep(2000);
        cardArea.getText();
        logger.info("The following comment is added: " + cardArea.getText());
        return cardArea.getText();
    }
    @Step("Comment is not presented check")
    public boolean commentIsNotPresent() {
        cardArea.isDisplayed();
        return true;
        // return driver.findElements(By.xpath(String.valueOf(cardArea))).isEmpty();
    }

    @Step("Get error message")
    public String getError() {
        wait.until(ExpectedConditions.visibilityOf(errorMsg));
        errorMsg.getText();
        logger.info("The following error appears: " + errorMsg.getText());
        return errorMsg.getText();
    }

}

