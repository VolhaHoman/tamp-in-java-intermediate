package com.epam.mentoring.taf.tests.uihelper;

import com.epam.mentoring.taf.ui.config.WebDriverLoader;

public interface UIDriverTest {

    ThreadLocal<WebDriverLoader> DRIVER = ThreadLocal.withInitial(WebDriverLoader::new);
}
