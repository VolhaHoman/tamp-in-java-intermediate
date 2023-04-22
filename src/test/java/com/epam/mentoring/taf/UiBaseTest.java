package com.epam.mentoring.taf;

import com.epam.mentoring.taf.ui.config.WebDriverCreate;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;

public class UiBaseTest extends AbstractTest {

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
