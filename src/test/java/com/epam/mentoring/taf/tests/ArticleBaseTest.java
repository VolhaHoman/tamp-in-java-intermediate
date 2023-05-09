package com.epam.mentoring.taf.tests;

import com.epam.mentoring.taf.dataobject.ArticleDTO;
import com.epam.mentoring.taf.dataobject.ArticleRequest;
import com.epam.mentoring.taf.dataobject.ArticleResponseDTO;
import com.epam.mentoring.taf.mapper.ArticleResponseMapper;
import com.epam.mentoring.taf.util.StorageHelper;
import io.restassured.response.Response;
import org.apache.hc.core5.http.HttpHeaders;
import org.testng.annotations.AfterMethod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.epam.mentoring.taf.util.StorageHelper.whatIsThe;

public class ArticleBaseTest extends UiBaseTest {

    protected List<String> createdArticles = new ArrayList<>();

    public String createArticle() throws IOException {
        ArticleDTO articleDTO = ArticleRequest.generateArticle();
        Response response = client.sendPostRequestWithHeaders(API_ARTICLES, articleDTO.articleToString(), Map.ofEntries(
                Map.entry(HttpHeaders.AUTHORIZATION, "Token " + whatIsThe(AUTH_TOKEN)),
                Map.entry("X-Requested-With", "XMLHttpRequest")));
        ArticleResponseMapper articleResponseMapper = new ArticleResponseMapper();
        ArticleResponseDTO articleResponseDTO = articleResponseMapper.articleToDto(response, log);
        return articleResponseDTO.getArticle().getSlug();
    }

    public Response deleteArticle(String slug) {
        return client.sendDeleteRequestWithHeaders(API_ARTICLES + slug, "", Map.ofEntries(
                Map.entry(org.apache.http.HttpHeaders.AUTHORIZATION, "Token " + StorageHelper.whatIsThe(AUTH_TOKEN)),
                Map.entry("X-Requested-With", "XMLHttpRequest")));
    }

    @AfterMethod(description = "Post-condition: delete the test article")
    public void cleanArticle() {
        if (!createdArticles.isEmpty()) {
            for (String s : createdArticles) {
                deleteArticle(s);
            }
        }
        createdArticles.clear();
    }

}
