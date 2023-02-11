package com.epam.mentoring.taf.model;

public class BrowserConfiguration {

    public String browser;

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String toString() {
        return "BrowserConfiguration{" +
                "browser='" + browser + '\'' +
                '}';
    }
}
