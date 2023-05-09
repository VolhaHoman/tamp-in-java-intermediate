package com.epam.mentoring.taf.tests.api;

import com.epam.mentoring.taf.dataobject.CommentDTO;
import com.epam.mentoring.taf.dataobject.CommentResponseDTO;
import com.epam.mentoring.taf.listeners.ReportPortalTestListener;
import com.epam.mentoring.taf.listeners.TestListener;
import com.epam.mentoring.taf.mapper.ResponseDataTransferMapper;
import com.epam.mentoring.taf.tests.AbstractTest;
import com.epam.mentoring.taf.util.DataUtil;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.epam.mentoring.taf.util.StorageHelper.rememberThat;
import static com.epam.mentoring.taf.util.StorageHelper.whatIsThe;

@Listeners({TestListener.class, ReportPortalTestListener.class})
@Feature("API: Comments Tests")
public class CommentAPITest extends AbstractTest {

    public static final String AUTH_TOKEN = "AUTH_TOKEN";
    public static final String COM_ID = "ID";
    public static final String ALL_COMMENT = "ALL_COMMENT";

    private static Logger log = LogManager.getLogger();

    @Test(description = "API: add multiple valid comments to article", dataProviderClass = DataUtil.class, dataProvider = "dataProviderForValidComments", priority = 0)
    @Severity(SeverityLevel.BLOCKER)
    @Description("API add comments to article from json file")
    @Story("Create new tests for comments functionality")
    public void apiAddCommentWithValidText(HashMap<String, String> data) {
        CommentResponseDTO commentDTO = new CommentResponseDTO.CommentResponseDTOBuilder(data.get("body")).build();
        Response response = client.sendPostRequestWithHeaders(whatIsThe(ALL_COMMENT), commentDTO.toString(), Map.ofEntries(
                Map.entry(HttpHeaders.AUTHORIZATION, "Token " + whatIsThe(AUTH_TOKEN)),
                Map.entry("X-Requested-With", "XMLHttpRequest")
        ));

        ResponseDataTransferMapper restAPIClient = new ResponseDataTransferMapper();
        CommentDTO responseDTO = restAPIClient.transformToDtoCom(response, log);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK);
        Assert.assertEquals(responseDTO.getComment().getBody(), data.get("body"));
    }

    @Test(description = "API: add invalid comments to article", dataProviderClass = DataUtil.class, dataProvider = "dataProviderForInvalidComments", priority = 1)
    @Severity(SeverityLevel.NORMAL)
    @Description("API add invalid comments to article from json file")
    @Story("Create new tests for comments functionality")
    public void apiAddCommentWithInvalidText(HashMap<String, String> data) {
        CommentResponseDTO commentDTO = new CommentResponseDTO.CommentResponseDTOBuilder(data.get("invalid_text")).build();
        Response response = client.sendPostRequestWithHeaders(whatIsThe(ALL_COMMENT), String.valueOf(commentDTO), Map.ofEntries(
                Map.entry(HttpHeaders.AUTHORIZATION, "Token " + whatIsThe(AUTH_TOKEN)),
                Map.entry("X-Requested-With", "XMLHttpRequest")
        ));
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @AfterTest(description = "Post-condition: delete all comments")
    public void cleanComments() {
        Response getResponse = client.sendGetRequestWithHeaders(whatIsThe(ALL_COMMENT), Map.ofEntries(
                Map.entry(HttpHeaders.AUTHORIZATION, "Token " + whatIsThe(AUTH_TOKEN)),
                Map.entry("X-Requested-With", "XMLHttpRequest")
        ));
        List<String> id =
                getResponse.getBody().jsonPath().getList("comments.id", String.class);
        rememberThat(COM_ID, String.valueOf(id));
        for (int i = 0; i < id.size(); i++) {
            String uniqueCommentPath = whatIsThe(ALL_COMMENT) + "/" + id.get(i);
            Response response = client.sendDeleteRequestWithHeaders(uniqueCommentPath, "", Map.ofEntries(
                    Map.entry(HttpHeaders.AUTHORIZATION, "Token " + whatIsThe(AUTH_TOKEN)),
                    Map.entry("X-Requested-With", "XMLHttpRequest")
            ));

            ResponseDataTransferMapper restAPIClient = new ResponseDataTransferMapper();
            CommentDTO responseDTO = restAPIClient.transformToDtoCom(response, log);
            Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK);
            Assert.assertNull(responseDTO.getComment());
        }
    }

}
