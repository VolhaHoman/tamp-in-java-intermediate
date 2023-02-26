package com.epam.mentoring.taf.ui.page;

import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.Random;

public class HomePage extends BasePage {

    private WebElement tag;

    @FindBy(xpath = "//ul[contains(@class,'navbar-nav')]/li[4]/a")
    private WebElement usernameAccountNav;

    @FindBy(xpath = "//a[contains(@class,'tag-pill')]")
    private List<WebElement> tagPills;

    @FindBy(xpath = "//div[@class='app-article-preview' and not(@hidden)]")
    private WebElement articlePreview;

    @FindBy(xpath = "//a[@class='nav-link active']")
    private WebElement navLink;

    public HomePage() {
        PageFactory.initElements(driver, this);
    }

    @Step("Get username from navigation bar")
    public String getUsernameAccountNav() {
        wait.until(ExpectedConditions.visibilityOf(usernameAccountNav));
        return usernameAccountNav.getText();
    }

    @Step("Get a tag from side bar")
    public HomePage getTagFromSidebar() {
        wait.until(ExpectedConditions.visibilityOfAllElements(tagPills));
        Random rand = new Random();
        tag = tagPills.get(rand.nextInt(tagPills.size()));
//        logger.info("Get all tags from side bar: " + tagPills.size());
        return this;
    }

    @Step("Get a tag name")
    public String getTagText() {
//        logger.info("Get a tag from side bar: " + tag.getText();
        return tag.getText();
    }

    @Step("Click on the tag")
    public HomePage clickTag() {
        tag.click();
//        logger.info("Click on the tag pill");
        return this;
    }

    @Step("Get the new tab title")
    public String getNavLink() {
        wait.until(ExpectedConditions.visibilityOf(articlePreview));
//        logger.info("Get the new tag tab title: " + navLink.getText());
        return navLink.getText();
    }
}
