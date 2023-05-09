package com.epam.mentoring.taf.dataobject;

public class ApiUserDTO {
    private String username;
    private String email;
    private String password;

    public static final String JSON_BODY = "{\"user\":{\"email\":\"%s\",\"password\":\"%s\",\"username\":\"%s\"}}";

    ApiUserDTO(ApiUserDTOBuilder builder) {
        this.username = builder.username;
        this.email = builder.email;
        this.password = builder.password;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String toString() {
        return String.format(JSON_BODY, email, password, username);
    }

    public static class ApiUserDTOBuilder {
        private String username;
        private String email;
        private String password;

        public ApiUserDTOBuilder(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public ApiUserDTOBuilder setUsername(String username) {
            this.username = username;
            return this;
        }

        public ApiUserDTO build() {
            return new ApiUserDTO(this);
        }
    }

}
