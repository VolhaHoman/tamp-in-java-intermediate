package com.epam.mentoring.taf.tests.api;

import com.epam.mentoring.taf.dataobject.ArticleDTO;
import com.epam.mentoring.taf.dataobject.ArticleRequest;
import com.epam.mentoring.taf.dataobject.ArticleResponseDTO;
import com.epam.mentoring.taf.listeners.ReportPortalTestListener;
import com.epam.mentoring.taf.listeners.TestListener;
import com.epam.mentoring.taf.mapper.ArticleResponseMapper;
import com.epam.mentoring.taf.tests.ArticleMainBase;
import com.epam.mentoring.taf.tests.IAuthorizationTest;
import com.epam.mentoring.taf.tests.IRestClient;
import com.epam.mentoring.taf.util.StorageHelper;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;

import static com.epam.mentoring.taf.tests.AllCommentsBase.API_ARTICLES;
import static com.epam.mentoring.taf.tests.AllCommentsBase.ARTICLES_COUNT_JSON_PATH;
import static com.epam.mentoring.taf.tests.AuthorizationUserBase.AUTH_TOKEN;
import static com.epam.mentoring.taf.util.StorageHelper.whatIsThe;

@Listeners({TestListener.class, ReportPortalTestListener.class})
@Feature("Article handling API Tests")
public class ArticleAPITest implements IAuthorizationTest, IRestClient {

    public static final String updatedBody = "With two hands";
    public static final String JSON_BODY_UPDATE = "{\"article\":{\"body\":\"%s\"}}";
    protected static Logger log = LogManager.getLogger();

    @Test(description = "API: Add a valid article", priority = 0)
    @Severity(SeverityLevel.BLOCKER)
    @Description("API: Add a valid article from YAML file")
    @Story("Create new tests for articles handling functionality using Annotations and Data Providers")
    public void apiAddValidArticle() throws IOException {
        ArticleMainBase articleMainBase = new ArticleMainBase(log);
        try {
            ArticleDTO articleDTO = ArticleRequest.generateArticle();

            Response response = articleMainBase.getArticleResponse(articleDTO);
            ArticleResponseDTO articleResponseDTO = ArticleResponseMapper.articleToDto(response, log);
            articleMainBase.getCreatedArticles().add(articleResponseDTO.getArticle().getSlug());

            Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK);
            Assert.assertEquals(articleResponseDTO.getArticle().getTitle(), articleDTO.getTitle());
            Assert.assertEquals(articleResponseDTO.getArticle().getDescription(), articleDTO.getDescription());
            Assert.assertEquals(articleResponseDTO.getArticle().getBody(), articleDTO.getBody());
            Assert.assertTrue(articleResponseDTO.getArticle().getTagList().containsAll(articleDTO.getTagList()));
        } finally {
            articleMainBase.cleanArticle();
        }
    }

    @Test(description = "API: Read all articles with authorization", priority = 1)
    @Severity(SeverityLevel.NORMAL)
    @Description("API: Read all articles with authorization")
    @Story("Create new tests for articles handling functionality using Annotations and Data Providers")
    public void apiReadAllArticles() throws IOException {
        ArticleMainBase articleMainBase = new ArticleMainBase(log);
        try {
            articleMainBase.createArticle();
            Response response = CLIENT.get().sendGetRequestWithHeaders(API_ARTICLES, Map.ofEntries(
                    Map.entry(org.apache.http.HttpHeaders.AUTHORIZATION, "Token " + StorageHelper.whatIsThe(AUTH_TOKEN)),
                    Map.entry("X-Requested-With", "XMLHttpRequest")));

            int articlesCount = response.getBody().jsonPath().get(ARTICLES_COUNT_JSON_PATH);

            Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK);
            Assert.assertTrue(articlesCount > 0);
            Assert.assertFalse(articleMainBase.getCreatedArticles().isEmpty());
        } finally {
            articleMainBase.cleanArticle();
        }
    }

    @Test(description = "API: Update an existing article", priority = 2)
    @Severity(SeverityLevel.BLOCKER)
    @Description("API: Update an existing article")
    @Story("Create new tests for articles handling functionality using Annotations and Data Providers")
    public void apiUpdateArticle() throws IOException {
        ArticleMainBase articleMainBase = new ArticleMainBase(log);
        try {
            String slug = articleMainBase.createArticle();

            Response response = CLIENT.get()
                    .sendPutRequestWithHeaders(API_ARTICLES + slug,
                            String.format(JSON_BODY_UPDATE, updatedBody),
                            Map.ofEntries(
                                    Map.entry(HttpHeaders.AUTHORIZATION, "Token " + whatIsThe(AUTH_TOKEN)),
                                    Map.entry("X-Requested-With", "XMLHttpRequest"))
                    );
            ArticleResponseDTO articleResponseDTO = ArticleResponseMapper.articleToDto(response, log);

            Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK);
            Assert.assertEquals(articleResponseDTO.getArticle().getBody(), updatedBody);
        } finally {
            articleMainBase.cleanArticle();
        }
    }

    @Test(description = "API: Delete an existing article", priority = 3)
    @Severity(SeverityLevel.CRITICAL)
    @Description("API: Delete an existing article")
    @Story("Create new tests for articles handling functionality using Annotations and Data Providers")
    public void apiDeleteArticle() throws IOException {
        ArticleMainBase articleMainBase = new ArticleMainBase(log);
        try {
            String article = articleMainBase.createArticle();
            Response response = articleMainBase.deleteArticle(article);
            Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_NO_CONTENT);
        } finally {
            articleMainBase.cleanArticle();
        }

    }

}
