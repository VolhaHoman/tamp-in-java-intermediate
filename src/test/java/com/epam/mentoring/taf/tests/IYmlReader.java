package com.epam.mentoring.taf.tests;

import com.epam.mentoring.taf.service.YamlReader;

public interface IYmlReader {

    ThreadLocal<YamlReader> READER = ThreadLocal.withInitial(YamlReader::new);
}
