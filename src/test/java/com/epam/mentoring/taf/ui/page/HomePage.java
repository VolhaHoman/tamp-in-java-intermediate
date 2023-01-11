package com.epam.mentoring.taf.ui.page;

import com.epam.mentoring.taf.ui.config.WebDriverCreate;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {
    private final WebDriver driver = WebDriverCreate.getWebDriverInstance();
    private final WebDriverWait wait = WebDriverCreate.getWebDriverWaitInstance();

    public HomePage() {
    }

    public static final By USERNAME_ACCOUNT_NAV = By.xpath("//ul[contains(@class,'navbar-nav')]/li[4]/a");

    public String getTextWithWait(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return driver.findElement(locator).getText();
    }
}
