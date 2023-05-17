package com.epam.mentoring.taf.tests;

import com.epam.mentoring.taf.api.RestClient;
import org.apache.logging.log4j.LogManager;

public interface IRestClient {

    ThreadLocal<RestClient> CLIENT = ThreadLocal.withInitial(
            () -> new RestClient(LogManager.getLogger())
    );
}
