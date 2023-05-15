package com.epam.mentoring.taf.tests.uihelper;

import com.epam.mentoring.taf.ui.page.HomePage;
import com.epam.mentoring.taf.ui.page.LoginPage;
import com.epam.mentoring.taf.ui.page.SettingsPage;

public interface LoginBaseUI {

    default void logIn(String email, String password, HomePage homePage, LoginPage loginPage) {
        homePage.clickSignInLink();
        loginPage.fillInEmail(email)
                .fillInPassword(password)
                .clickSignInBtn();
    }

    default void logOut(HomePage homePage, SettingsPage settingsPage) {
        homePage.navToSetting();
        settingsPage.clickLogOutBtn();
    }
}
