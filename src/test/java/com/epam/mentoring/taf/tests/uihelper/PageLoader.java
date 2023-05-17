package com.epam.mentoring.taf.tests.uihelper;

import com.epam.mentoring.taf.ui.page.*;
import org.apache.logging.log4j.Logger;

public interface PageLoader extends UIDriverTest {

    default HomePage homePage(Logger logger) {
        return new HomePage(logger, DRIVER.get());
    }

    default ArticlePage articlePage(Logger logger) {
        return new ArticlePage(logger, DRIVER.get());
    }

    default SettingsPage settingsPage(Logger logger) {
        return new SettingsPage(logger, DRIVER.get());
    }

    default RegisterPage registerPage(Logger logger) {
        return new RegisterPage(logger, DRIVER.get());
    }

    default LoginPage loginPage(Logger logger) {
        return new LoginPage(logger, DRIVER.get());
    }

    default UserProfilePage userProfilePage(Logger logger) {
        return new UserProfilePage(logger, DRIVER.get());
    }

    default AppEditorPage appEditorPage(Logger logger) {
        return new AppEditorPage(logger, DRIVER.get());
    }

    default CelebPage celebPage(Logger logger) {
        return new CelebPage(logger, DRIVER.get());
    }
}
