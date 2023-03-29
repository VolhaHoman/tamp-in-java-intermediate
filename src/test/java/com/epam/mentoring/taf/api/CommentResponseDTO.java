package com.epam.mentoring.taf.api;

public class CommentResponseDTO {

    private String body;

    private static final String JSON_BODY = "{\"comment\":{\"body\":\"%s\"}}";

    CommentResponseDTO(CommentResponseDTOBuilder builder) {
        this.body = builder.body;
    }

    public String getBody() {
        return body;
    }

    public String toString() {
        return String.format(JSON_BODY, body);
    }

    public static class CommentResponseDTOBuilder {
        private String body;

        public CommentResponseDTOBuilder(String body) {
            this.body = String.valueOf(body);
        }

        public CommentResponseDTOBuilder setBody(String text) {
            this.body = body;
            return this;
        }

        public CommentResponseDTO build() {
            return new CommentResponseDTO(this);
        }
    }

}
