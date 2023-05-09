package com.epam.mentoring.taf.mapper;

import com.epam.mentoring.taf.dataobject.ArticleResponseDTO;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ArticleResponseMapper {

    public final Logger logger = LogManager.getRootLogger();

    public ArticleResponseDTO articleToDto(Response response, Logger logger) {
        ArticleResponseDTO articleResponseDTO = response.body().as(ArticleResponseDTO.class);
        logger.info("Response message: " + articleResponseDTO.getArticle());
        return articleResponseDTO;
    }
}
