package com.epam.mentoring.taf.listeners;

import com.epam.mentoring.taf.ui.config.WebDriverCreate;
import io.qameta.allure.Attachment;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TestListener implements ITestListener {

    public final Logger logger = LogManager.getRootLogger();
    public final WebDriver driver = WebDriverCreate.getWebDriverInstance();

    private static String getTestMethodName(ITestResult iTestResult) {
        return iTestResult.getMethod().getConstructorOrMethod().getName();
    }

    @Attachment(value = "Page screenshot", type = "image/png")
    public byte[] saveScreenshotPng(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        WebDriver driver = WebDriverCreate.getWebDriverInstance();
        saveScreenshotPng(driver);
        saveScreenshotToLogFile();
    }

    private void saveScreenshotToLogFile() {
        File screenCapture = ((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.FILE);
        String path = ".//target/screenshots/"
                + getCurrentAsString() + ".png";
        try {
            FileUtils.copyFile(screenCapture, new File(path));
        } catch (IOException e) {
            logger.error("Failed to save screenshot: " + e.getLocalizedMessage());
        }
        StringBuilder builder = new StringBuilder();
        builder.append("Path to the failed test screenshot: <img src=\"").append(path).append("\">");
        logger.info(builder.toString());
    }

    private String getCurrentAsString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd-HH-mm-ss");
        return ZonedDateTime.now().format(formatter);
    }

}
