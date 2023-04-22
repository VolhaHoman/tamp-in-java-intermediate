package com.epam.mentoring.taf;

import com.epam.mentoring.taf.util.StorageHelper;

import static com.epam.mentoring.taf.FollowUserTest.ADMIN_EMAIL;
import static com.epam.mentoring.taf.FollowUserTest.ADMIN_PASSWORD;

public class UiBaseTest extends AbstractTest {

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
