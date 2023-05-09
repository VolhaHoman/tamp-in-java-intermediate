package com.epam.mentoring.taf.mapper;

import com.epam.mentoring.taf.dataobject.CommentDTO;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ResponseDataTransferMapper {

    public final Logger logger = LogManager.getRootLogger();

    public CommentDTO transformToDtoCom(Response response, Logger logger) {
        CommentDTO commentDTO = response.body().as(CommentDTO.class);
        return commentDTO;
    }

}
