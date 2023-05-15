package com.epam.mentoring.taf.tests.uihelper;

import com.epam.mentoring.taf.tests.ILoggerTest;
import com.epam.mentoring.taf.ui.page.*;

public interface PageLoader extends UIDriverTest, ILoggerTest {

    default HomePage homePage() {
        return new HomePage(LOGGER.get(), DRIVER.get());
    }

    default ArticlePage articlePage() {
        return new ArticlePage(LOGGER.get(), DRIVER.get());
    }

    default SettingsPage settingsPage() {
        return new SettingsPage(LOGGER.get(), DRIVER.get());
    }

    default RegisterPage registerPage() {
        return new RegisterPage(LOGGER.get(), DRIVER.get());
    }

    default LoginPage loginPage() {
        return new LoginPage(LOGGER.get(), DRIVER.get());
    }

    default UserProfilePage userProfilePage() {
        return new UserProfilePage(LOGGER.get(), DRIVER.get());
    }

    default AppEditorPage appEditorPage() {
        return new AppEditorPage(LOGGER.get(), DRIVER.get());
    }

    default CelebPage celebPage() {
        return new CelebPage(LOGGER.get(), DRIVER.get());
    }
}
