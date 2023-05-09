package com.epam.mentoring.taf.tests;

import com.epam.mentoring.taf.ui.config.WebDriverCreate;
import com.epam.mentoring.taf.ui.page.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;

public class UiBaseTest extends AbstractTest {

    protected WebDriver driver;
    protected WebDriverWait wait;

    protected HomePage homePage;
    protected LoginPage loginPage;
    protected CelebPage celebPage;
    protected ArticlePage articlePage;
    protected AppEditorPage appEditorPage;
    protected SettingsPage settingPage;
    protected UserProfilePage userProfilePage;
    protected RegisterPage registerPage;

    @BeforeMethod
    public void initialisation() {
        // TODO: Remove after migration to Page Object Pattern.
        driver = WebDriverCreate.getWebDriverInstance();
        wait = WebDriverCreate.getWebDriverWaitInstance();

        homePage = new HomePage(log, driver, wait);
        loginPage = new LoginPage(log, driver, wait);
        celebPage = new CelebPage(log, driver, wait);
        articlePage = new ArticlePage(log, driver, wait);
        appEditorPage = new AppEditorPage(log, driver, wait);
        settingPage = new SettingsPage(log, driver, wait);
        userProfilePage = new UserProfilePage(log, driver, wait);
        registerPage = new RegisterPage(log, driver, wait);

        driver.get(baseUrl);
        driver.manage().window().maximize();
    }

    public void logIn(String email, String password) {
        homePage.clickSignInLink();
        loginPage.fillInEmail(email)
                 .fillInPassword(password)
                 .clickSignInBtn();
    }

    public void selectArticle() {
        homePage.navToUser();
        userProfilePage.selectArt();
    }

    public void logOut() {
        homePage.navToSetting();
        settingPage.clickLogOutBtn();
    }

    @AfterClass
    public void terminate() {
        driver.quit();
    }

}
