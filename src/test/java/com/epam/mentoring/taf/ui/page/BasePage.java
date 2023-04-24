package com.epam.mentoring.taf.ui.page;

import com.epam.mentoring.taf.ui.config.WebDriverCreate;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage {

    protected WebDriver driver = WebDriverCreate.getWebDriverInstance();
    protected WebDriverWait wait = WebDriverCreate.getWebDriverWaitInstance();

    protected Logger logger;
}
