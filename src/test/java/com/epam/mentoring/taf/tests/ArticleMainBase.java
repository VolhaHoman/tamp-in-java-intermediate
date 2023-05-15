package com.epam.mentoring.taf.tests;

import com.epam.mentoring.taf.dataobject.ArticleDTO;
import com.epam.mentoring.taf.dataobject.ArticleRequest;
import io.restassured.response.Response;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.epam.mentoring.taf.mapper.ArticleResponseMapper.articleToDto;
import static com.epam.mentoring.taf.tests.AllCommentsBase.API_ARTICLES;
import static com.epam.mentoring.taf.tests.AuthorizationUserBase.AUTH_TOKEN;
import static com.epam.mentoring.taf.util.StorageHelper.whatIsThe;

public class ArticleMainBase implements IRestClient {

    private final Logger log;
    private final List<String> createdArticles = new ArrayList<>();

    public ArticleMainBase(Logger log) {
        this.log = log;
    }

    public List<String> getCreatedArticles() {
        return createdArticles;
    }

    public String createArticle() throws IOException {
        Response response = getArticleResponse(ArticleRequest.generateArticle());
        String slug = articleToDto(response, log).getArticle().getSlug();
        createdArticles.add(slug);
        return slug;
    }

    public Response getArticleResponse(ArticleDTO articleDTO) {
        return CLIENT.get().sendPostRequestWithHeaders(API_ARTICLES, articleDTO.articleToString(), Map.ofEntries(
                Map.entry(HttpHeaders.AUTHORIZATION, "Token " + whatIsThe(AUTH_TOKEN)),
                Map.entry("X-Requested-With", "XMLHttpRequest")));
    }

    public Response deleteArticle(String slug) {
        return CLIENT.get().sendDeleteRequestWithHeaders(API_ARTICLES + slug, "", Map.ofEntries(
                Map.entry(org.apache.http.HttpHeaders.AUTHORIZATION, "Token " + whatIsThe(AUTH_TOKEN)),
                Map.entry("X-Requested-With", "XMLHttpRequest")));
    }

    public void cleanArticle() {
        if (!createdArticles.isEmpty()) {
            for (String s : createdArticles) {
                deleteArticle(s);
            }
        }
        createdArticles.clear();
    }

}
