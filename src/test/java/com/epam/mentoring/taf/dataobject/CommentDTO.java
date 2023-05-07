package com.epam.mentoring.taf.dataobject;

import java.util.Date;

public class CommentDTO {

    private CommentBody comment;

    public CommentBody getComment() {
        return comment;
    }

    public void setComment(CommentBody comment) {
        this.comment = comment;
    }

    public class CommentBody {
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

