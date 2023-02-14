package com.epam.mentoring.taf.listeners;

import com.epam.mentoring.taf.ui.config.WebDriverCreate;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {
    private static String getTestMethodName(ITestResult iTestResult){
        return iTestResult.getMethod().getConstructorOrMethod().getName();
    }

    @Attachment(value = "Page screenshot", type = "image/png")
    public byte[] saveScreenshotPng (WebDriver driver){
        return((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES);
    }


    @Override
    public void onTestFailure(ITestResult iTestResult){
        WebDriver driver = WebDriverCreate.getWebDriverInstance();
        saveScreenshotPng(driver);
    }
}
