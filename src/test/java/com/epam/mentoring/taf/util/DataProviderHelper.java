package com.epam.mentoring.taf.util;

import com.epam.mentoring.taf.exception.EmptyValuesException;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;

public class DataProviderHelper {

    public static <T> Object[][] mapToProviderArray(List<T> values) throws EmptyValuesException {
        return mapToProviderArray(values.toArray());
    }

    public static <T> Object[][] mapToProviderArray(T[] values) throws EmptyValuesException {
        if (ArrayUtils.isEmpty(values)) {
            throw new EmptyValuesException("List of values is empty.");
        }
        Object[][] data = new Object[values.length][1];
        for (int i = 0; i < values.length; i++) {
            data[i][0] = values[i];
        }
        return data;
    }
}
