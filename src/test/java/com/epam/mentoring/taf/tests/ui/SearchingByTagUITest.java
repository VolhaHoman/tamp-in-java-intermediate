package com.epam.mentoring.taf.tests.ui;

import com.epam.mentoring.taf.listeners.ReportPortalTestListener;
import com.epam.mentoring.taf.listeners.TestListener;
import com.epam.mentoring.taf.tests.UiBaseTest;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners({TestListener.class, ReportPortalTestListener.class})
@Feature("Searching By Tag UI Tests")
public class SearchingByTagUITest extends UiBaseTest {

    @Test(description = "UI Search by a valid tag")
    @Severity(SeverityLevel.CRITICAL)
    @Description("UI Search by a valid tag")
    @Story("Add UI and API layers support to SearchByTagTest")
    public void uiSearchByRandomValidTag() {
        String tagName = homePage.getTagFromSidebar().getTagText();
        String selectedTag = homePage.clickTag().getNavLink();
        Assert.assertEquals(selectedTag, tagName);
    }

}
