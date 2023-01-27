package com.epam.mentoring.taf.ui.page;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HomePage extends BasePage {

    @FindBy(xpath = "//ul[contains(@class,'navbar-nav')]/li[4]/a")
    private WebElement usernameAccountNav;

    public HomePage() {
        PageFactory.initElements(driver, this);
    }

    public String getUsernameAccountNav() {
        wait.until(ExpectedConditions.visibilityOf(usernameAccountNav));
        return usernameAccountNav.getText();
    }
}
