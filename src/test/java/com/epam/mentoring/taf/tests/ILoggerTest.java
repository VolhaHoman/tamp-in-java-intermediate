package com.epam.mentoring.taf.tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface ILoggerTest {

    ThreadLocal<Logger> LOGGER = ThreadLocal.withInitial(LogManager::getLogger);
}
