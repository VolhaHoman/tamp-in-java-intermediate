package com.epam.mentoring.taf.ui.page;

import io.qameta.allure.Step;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class AppEditorPage extends BasePage {

    @FindBy(xpath = "//input[@formcontrolname='title']")
    private WebElement title;

    @FindBy(xpath = "//input[@formcontrolname='description']")
    private WebElement description;

    @FindBy(xpath = "//textarea[@formcontrolname='body']")
    private WebElement body;

    @FindBy(xpath = "//fieldset/fieldset[4]/input")
    private WebElement tagList;

    @FindBy(xpath = "//button[contains(text(),'Publish Article')]")
    private WebElement publishBtn;

    @FindBy(xpath = "//app-list-errors/ul/li")
    public WebElement errorMsg;

    public AppEditorPage(Logger logger) {
        PageFactory.initElements(driver, this);
        this.logger = logger;
    }

    @Step("Enter the title")
    public AppEditorPage enterTitle(String text) {
        wait.until(ExpectedConditions.visibilityOf(title));
        title.sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END), Keys.DELETE);
        title.sendKeys(text);
        logger.info("Enter the following title: " + text);
        return this;
    }

    @Step("Enter the description")
    public AppEditorPage enterDescription(String text) {
        description.sendKeys(text);
        logger.info("Enter the following description: " + text);
        return this;
    }

    @Step("Enter the body")
    public AppEditorPage enterBody(String text) {
        body.sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END), Keys.DELETE);
        body.sendKeys(text);
        logger.info("Enter the following body: " + text);
        return this;
    }

    @Step("Enter a tag")
    public AppEditorPage enterTag(String text) {
        tagList.sendKeys(text);
        tagList.sendKeys(Keys.ENTER);
        logger.info("Enter the following tag: " + text);
        return this;
    }

    @Step("Enter the body")
    public void publishArticle() {
        publishBtn.click();
        logger.info("Publish the article");
    }

    @Step("Get error message")
    public String getError() {
        wait.until(ExpectedConditions.visibilityOf(errorMsg));
        logger.info("The following error appears: " + errorMsg.getText());
        return errorMsg.getText();
    }

}
