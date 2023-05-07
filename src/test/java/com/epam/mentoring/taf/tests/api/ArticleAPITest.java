package com.epam.mentoring.taf.tests.api;

import com.epam.mentoring.taf.dataobject.ArticleDTO;
import com.epam.mentoring.taf.dataobject.ArticleRequest;
import com.epam.mentoring.taf.dataobject.ArticleResponseDTO;
import com.epam.mentoring.taf.listeners.ReportPortalTestListener;
import com.epam.mentoring.taf.listeners.TestListener;
import com.epam.mentoring.taf.mapper.ArticleResponseMapper;
import com.epam.mentoring.taf.tests.ArticleBaseTest;
import com.epam.mentoring.taf.util.StorageHelper;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;

import static com.epam.mentoring.taf.util.StorageHelper.whatIsThe;

@Listeners({TestListener.class, ReportPortalTestListener.class})
@Feature("Article handling API Tests")
public class ArticleAPITest extends ArticleBaseTest {

    public static final String updatedBody = "With two hands";
    public static final String JSON_BODY_UPDATE = "{\"article\":{\"body\":\"%s\"}}";

    @Test(description = "API: Add a valid article", priority = 0)
    @Severity(SeverityLevel.BLOCKER)
    @Description("API: Add a valid article from YAML file")
    @Story("Create new tests for articles handling functionality using Annotations and Data Providers")
    public void apiAddValidArticle() throws IOException {

        ArticleDTO articleDTO = ArticleRequest.generateArticle();
        Response response = client.sendPostRequestWithHeaders(API_ARTICLES, articleDTO.articleToString(), Map.ofEntries(
                Map.entry(HttpHeaders.AUTHORIZATION, "Token " + whatIsThe(AUTH_TOKEN)),
                Map.entry("X-Requested-With", "XMLHttpRequest")));
        ArticleResponseMapper articleResponseMapper = new ArticleResponseMapper();
        ArticleResponseDTO articleResponseDTO = articleResponseMapper.articleToDto(response, log);
        createdArticles.add(articleResponseDTO.getArticle().getSlug());

        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK);
        Assert.assertEquals(articleResponseDTO.getArticle().getTitle(), articleDTO.getTitle());
        Assert.assertEquals(articleResponseDTO.getArticle().getDescription(), articleDTO.getDescription());
        Assert.assertEquals(articleResponseDTO.getArticle().getBody(), articleDTO.getBody());
        Assert.assertTrue(articleResponseDTO.getArticle().getTagList().containsAll(articleDTO.getTagList()));

    }

    @Test(description = "API: Read all articles with authorization", priority = 1)
    @Severity(SeverityLevel.NORMAL)
    @Description("API: Read all articles with authorization")
    @Story("Create new tests for articles handling functionality using Annotations and Data Providers")
    public void apiReadAllArticles() throws IOException {

        String slug = createArticle();
        createdArticles.add(slug);
        Response response = client.sendGetRequestWithHeaders(API_ARTICLES, Map.ofEntries(
                Map.entry(org.apache.http.HttpHeaders.AUTHORIZATION, "Token " + StorageHelper.whatIsThe(AUTH_TOKEN)),
                Map.entry("X-Requested-With", "XMLHttpRequest")));

        int articlesCount = response.getBody().jsonPath().get(ARTICLES_COUNT_JSON_PATH);

        Assert.assertEquals(response.getStatusCode(), org.apache.http.HttpStatus.SC_OK);
        Assert.assertTrue(articlesCount > 0);
    }

    @Test(description = "API: Update an existing article", priority = 2)
    @Severity(SeverityLevel.BLOCKER)
    @Description("API: Update an existing article")
    @Story("Create new tests for articles handling functionality using Annotations and Data Providers")
    public void apiUpdateArticle() throws IOException {

        String slug = createArticle();
        createdArticles.add(slug);

        Response response = client.sendPutRequestWithHeaders(API_ARTICLES +  slug, String.format(JSON_BODY_UPDATE, updatedBody), Map.ofEntries(
                Map.entry(HttpHeaders.AUTHORIZATION, "Token " + whatIsThe(AUTH_TOKEN)),
                Map.entry("X-Requested-With", "XMLHttpRequest")));
        ArticleResponseMapper articleResponseMapper = new ArticleResponseMapper();
        ArticleResponseDTO articleResponseDTO = articleResponseMapper.articleToDto(response, log);

        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK);
        Assert.assertEquals(articleResponseDTO.getArticle().getBody(), updatedBody);

    }

    @Test(description = "API: Delete an existing article", priority = 3)
    @Severity(SeverityLevel.CRITICAL)
    @Description("API: Delete an existing article")
    @Story("Create new tests for articles handling functionality using Annotations and Data Providers")
    public void apiDeleteArticle() throws IOException {

        String slug = createArticle();
        createdArticles.add(slug);

        Response response = deleteArticle(slug);

        Assert.assertEquals(response.getStatusCode(), org.apache.http.HttpStatus.SC_NO_CONTENT);
    }

}
