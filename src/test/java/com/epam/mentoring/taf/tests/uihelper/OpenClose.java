package com.epam.mentoring.taf.tests.uihelper;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import static com.epam.mentoring.taf.tests.AuthorizationUserBase.BASE_URL;

public interface OpenClose extends UIDriverTest {

    @BeforeMethod
    default void open() {
        try {
            DRIVER.get().getWebDriver().get(BASE_URL);
        } catch (Exception e) {
            DRIVER.get().getWebDriver().quit();
        }
    }

    @AfterMethod
    default void close() {
        DRIVER.get().getWebDriver().quit();
    }
}
