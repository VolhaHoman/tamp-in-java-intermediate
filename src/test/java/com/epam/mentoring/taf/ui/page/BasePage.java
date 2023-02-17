package com.epam.mentoring.taf.ui.page;

import com.epam.mentoring.taf.ui.config.WebDriverCreate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage {

    public final WebDriver driver = WebDriverCreate.getWebDriverInstance();
    public final WebDriverWait wait = WebDriverCreate.getWebDriverWaitInstance();
    public final Logger logger = LogManager.getRootLogger();

}
