package com.epam.mentoring.taf.listeners;

import com.epam.mentoring.taf.ui.config.WebDriverCreate;
import com.epam.mentoring.taf.util.LoggingUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ReportPortalTestListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        WebDriver driver = WebDriverCreate.getWebDriverInstance();
        if (driver instanceof TakesScreenshot) {
            String screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
            LoggingUtils.logBase64(screenshot, iTestResult.getThrowable().getLocalizedMessage());
        }
    }
}
