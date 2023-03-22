package com.epam.mentoring.taf.service;

import com.epam.mentoring.taf.model.UserDataModel;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CsvReader {

    public static List<UserDataModel> getUserDataModels() throws IOException {
        return getModels(
                "src/test/resources/userList.csv",
                UserDataModel.class,
                ','
        );
    }

    private static <T>  List<T> getModels(String path, Class<T> clazz, char separator) throws IOException {
        File csvFile = new File(path);
        CsvMapper csvMapper = new CsvMapper();

        CsvSchema csvSchema = csvMapper
                .typedSchemaFor(clazz)
                .withHeader()
                .withColumnSeparator(separator)
                .withComments();

        try (MappingIterator<T> userModelIterator = csvMapper
                .readerWithTypedSchemaFor(clazz)
                .with(csvSchema)
                .readValues(csvFile)) {
            return userModelIterator.readAll();
        }
    }
}
