package com.epam.mentoring.taf;

import com.epam.mentoring.taf.ui.config.WebDriverCreate;
import com.epam.mentoring.taf.ui.page.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;

public class UiBaseTest extends AbstractTest {

    protected WebDriver driver;
    protected WebDriverWait wait;

    protected static LoginPage loginPage = new LoginPage(log);
    protected static HomePage homePage = new HomePage(baseUrl, log);
    protected static CelebPage celebPage = new CelebPage(log);
    protected static ArticlePage articlePage = new ArticlePage(log);
    protected static AppEditorPage appEditorPage = new AppEditorPage(log);
    protected static SettingsPage settingPage = new SettingsPage(log);
    protected static UserProfilePage userProfilePage = new UserProfilePage(log);
    protected static RegisterPage registerPage = new RegisterPage(log);

    @BeforeMethod
    public void initialisation() {
        // TODO: Remove after migration to Page Object Pattern.
        driver = WebDriverCreate.getWebDriverInstance();
        wait = WebDriverCreate.getWebDriverWaitInstance();

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
        settingPage.logout();
    }

    @AfterClass
    public void terminate() {
        driver.quit();
    }

}
