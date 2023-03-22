package com.epam.mentoring.taf.api;

public class CommentResponseDTO {
    private String body;
    public static final String JSON_BODY = "{\"comment\":{\"body\":\"%s\"}}";

    CommentResponseDTO(CommentResponseDTO.ApiDTOBuilder builder) {
        this.body = builder.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public String toString() {
        return String.format(JSON_BODY, body);
    }

    public static class ApiDTOBuilder {
        private String body;

        public ApiDTOBuilder(String body) {
            this.body = body;
        }

        public CommentResponseDTO.ApiDTOBuilder setBody(String text) {
            this.body = body;
            return this;
        }

        public CommentResponseDTO build() {
            return new CommentResponseDTO(this);
        }
    }

}
