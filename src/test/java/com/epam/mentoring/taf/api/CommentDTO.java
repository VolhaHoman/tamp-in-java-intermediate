package com.epam.mentoring.taf.api;

import java.util.Date;

public class CommentDTO {
    private CommentResponse comment;

    public CommentResponse getComment() {
        return comment;
    }

    public void setComment(CommentResponse comment) {
        this.comment = comment;
    }

    public class CommentResponse {
        private int id;
        private Date createdAt;
        private Date updatedAt;
        private String body;
        private Author author;

        public String getBody() {
            return body;
        }

        public int getId() {
            return id;
        }

        public Date getCreatedAt() {
            return createdAt;
        }

        public Date getUpdatedAt() {
            return updatedAt;
        }

        public Author getAuthor() {
            return author;
        }

        public class Author {
            private String username;
            private Object bio;
            private String image;
            private boolean following;

            public String getUsername() {
                return username;
            }

            public Object getBio() {
                return bio;
            }

            public String getImage() {
                return image;
            }

            public boolean isFollowing() {
                return following;
            }
        }
    }

}
