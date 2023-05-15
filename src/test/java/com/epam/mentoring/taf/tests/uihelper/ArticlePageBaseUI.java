package com.epam.mentoring.taf.tests.uihelper;

import com.epam.mentoring.taf.ui.page.HomePage;
import com.epam.mentoring.taf.ui.page.UserProfilePage;

public interface ArticlePageBaseUI {

    default void selectArticle(HomePage homePage, UserProfilePage profilePage) {
        homePage.navToUser();
        profilePage.selectArt();
    }
}
