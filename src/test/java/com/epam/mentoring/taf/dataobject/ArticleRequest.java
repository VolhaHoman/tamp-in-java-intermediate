package com.epam.mentoring.taf.dataobject;

import com.epam.mentoring.taf.model.ArticleModel;
import com.epam.mentoring.taf.service.YamlReader;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;
import java.util.List;

public class ArticleRequest {

    public static final YamlReader YAML_READER = new YamlReader();

    private static String generateUniqueId() {
        return RandomStringUtils.randomNumeric(10);
    }

    public static ArticleDTO generateArticle() throws IOException {
        ArticleModel articleModel = YAML_READER.readArticle("article");
        String uniqueId = generateUniqueId();
        String title = articleModel.getTitle() + uniqueId;
        String body = articleModel.getBody() + uniqueId;
        return new ArticleDTO(title, articleModel.getDescription(), body, List.of(articleModel.getTagList()));
    }
}
