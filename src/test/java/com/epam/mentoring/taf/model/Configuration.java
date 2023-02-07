package com.epam.mentoring.taf.model;

import java.util.Arrays;

public class Configuration {

    public String[] browser;

    public String[] getBrowser() {
        return browser;
    }

    public void setBrowser(String[] browser) {
        this.browser = browser;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "browser=" + Arrays.toString(browser) +
                '}';
    }
}
