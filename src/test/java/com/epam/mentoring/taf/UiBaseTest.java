package com.epam.mentoring.taf;

import com.epam.mentoring.taf.util.StorageHelper;

public class UiBaseTest extends AbstractTest {

    public static final String ADMIN_EMAIL = "ADMIN_EMAIL";
    public static final String ADMIN_PASSWORD = "ADMIN_PASSWORD";

    public void logIn() {
        loginPage.clickSignInLink()
                .fillInEmail(StorageHelper.whatIsThe(ADMIN_EMAIL))
                .fillInPassword(StorageHelper.whatIsThe(ADMIN_PASSWORD))
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

}
