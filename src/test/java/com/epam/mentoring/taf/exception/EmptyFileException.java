package com.epam.mentoring.taf.exception;

import java.io.IOException;

public class EmptyFileException extends IOException {
    public EmptyFileException(String message) {
        super(message);
    }

}
