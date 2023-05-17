package com.epam.mentoring.taf.tests;

import com.epam.mentoring.taf.api.RestClient;
import com.epam.mentoring.taf.util.StorageHelper;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

import static com.epam.mentoring.taf.tests.AuthorizationUserBase.AUTH_TOKEN;
import static com.epam.mentoring.taf.tests.api.CommentAPITest.ALL_COMMENT;
import static com.epam.mentoring.taf.util.StorageHelper.rememberThat;
import static com.epam.mentoring.taf.util.StorageHelper.whatIsThe;

public class AllCommentsBase implements IRestClient, IAuthorizationTest {

    public static final String API_ARTICLES = "https://api.realworld.io/api/articles/";
    public static final String ARTICLES_COUNT_JSON_PATH = "articlesCount";
    public static final String COMMENT_PATH = "/comments";
    public static final String SLUG = "SLUG";

    private static final Logger log = LogManager.getLogger();

    static {
        CLIENT.set(new RestClient(log));
    }

    public static void getSlug() {
        Response getResponse = CLIENT.get().sendGetRequestWithHeaders(API_ARTICLES, Map.ofEntries(
                Map.entry(org.apache.http.HttpHeaders.AUTHORIZATION, "Token " + StorageHelper.whatIsThe(AUTH_TOKEN)),
                Map.entry("X-Requested-With", "XMLHttpRequest")));
        String slug =
                getResponse.getBody().jsonPath().get("articles[0].slug");
        log.info("slug: " + slug);
        rememberThat(SLUG, slug);
        String allCommentPath = API_ARTICLES + whatIsThe(SLUG) + COMMENT_PATH;
        rememberThat(ALL_COMMENT, allCommentPath);
    }
}
