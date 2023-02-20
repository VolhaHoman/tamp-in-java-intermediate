package com.epam.mentoring.taf.ui.page;

import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HomePage extends BasePage {

    @FindBy(xpath = "//ul[contains(@class,'navbar-nav')]/li[4]/a")
    private WebElement usernameAccountNav;

    @FindBy(xpath = "//ul[contains(@class, 'nav-pills')]/li[2]/a")
    private WebElement feedNav;

    @FindBy(xpath = "//app-article-list/app-article-preview[1]/div/a")
    private WebElement article;


    public HomePage() {
        PageFactory.initElements(driver, this);
    }

    @Step("Get username from navigation bar")
    public String getUsernameAccountNav() {
        wait.until(ExpectedConditions.visibilityOf(usernameAccountNav));
        logger.info("Get username from navigation bar: " + usernameAccountNav.getText());
        return usernameAccountNav.getText();
    }

    public HomePage navFeed() {
        feedNav.click();
        return this;
    }

    public HomePage selectArt() {
        article.click();
        return this;
    }

}
