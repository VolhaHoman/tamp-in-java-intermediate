package com.epam.mentoring.taf;

import com.epam.mentoring.taf.listeners.TestListener;
import io.qameta.allure.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Listeners({ TestListener.class })
@Feature("Searching By Tag Tests")
public class SearchingByTagTest extends AbstractTest {

    private static final String ARTICLES_BY_TAG_URL = "/api/articles?tag={tag}&limit=10&offset=0";
    public static final String INVALID_TAG = "invalid_tag_name";
    public static final String TAG_LIST_JSON_PATH = "articles.tagList";
    public static final String ARTICLES_COUNT_JSON_PATH = "articlesCount";
    public static final String APP_ARTICLE_PREVIEW_XPATH = "//div[@class='app-article-preview' and not(@hidden)]";
    public static final String TAG_PILL_XPATH = "//a[contains(@class,'tag-pill')]";
    public static final String NAV_LINK_XPATH = "//a[@class='nav-link active']";

    @DataProvider(name = "apiDataProvider")
    public Object[][] apiDataProviderMethod() {
        return new Object[][] {{"implementations"},{"welcome"},{"ipsum"}};
    }

    @Test(description = "UI Search by a valid tag")
    @Severity(SeverityLevel.CRITICAL)
    @Description("UI Search by a valid tag")
    @Story("Investigate the issues and fix SearchingByTagTest")
    public void uiSearchByRandomValidTag() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(TAG_PILL_XPATH)));
        int tagCount = driver.findElements(By.xpath(TAG_PILL_XPATH)).size();
        int randomTag = (int) (Math.random() * tagCount + 1);
        WebElement tag = driver.findElement(By.xpath(TAG_PILL_XPATH + "[" + randomTag + "]"));
        String tagName = tag.getText();
        tag.click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(APP_ARTICLE_PREVIEW_XPATH)));
        String selectedTag = driver.findElement(By.xpath(NAV_LINK_XPATH)).getText();
        Assert.assertEquals(selectedTag, tagName);
    }

    @Test(dataProvider = "apiDataProvider", description = "API Search by a valid tag")
    @Severity(SeverityLevel.CRITICAL)
    @Description("API Search by a valid tag")
    @Story("Create layers for Data storing")
    public void apiSearchByValidTag(String tag) {
        given()
                .baseUri(API_URL)
                .when()
                .get(ARTICLES_BY_TAG_URL, tag)
                .then()
                .statusCode(200)
                .body(TAG_LIST_JSON_PATH, everyItem(hasItem(tag)));
    }

    @Test(description = "API Search by an invalid tag")
    @Severity(SeverityLevel.MINOR)
    @Description("API Search by an invalid tag")
    @Story("Create layers for Data storing")
    public void apiSearchByInvalidTag() {
        given()
                .baseUri(API_URL)
                .when()
                .get(ARTICLES_BY_TAG_URL, INVALID_TAG)
                .then()
                .statusCode(200)
                .body(ARTICLES_COUNT_JSON_PATH, equalTo(0));
    }

}
