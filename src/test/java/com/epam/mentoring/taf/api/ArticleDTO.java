package com.epam.mentoring.taf.api;

import java.util.ArrayList;
import java.util.Date;

public class ArticleDTO {
    private ArticleResponse articles;

    public ArticleResponse getArticles() {
        return articles;
    }

    public void setArticles(ArticleResponse articles) {
        this.articles = articles;
    }

    public class ArticleResponse {
        private String slug;
        private String title;
        private String description;
        private String body;
        private ArrayList<Object> tagList;
        private Date createdAt;
        private Date updatedAt;
        private boolean favorited;
        private int favoritesCount;
        private Author author;

        public String getSlug() {
            return slug;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getBody() {
            return body;
        }

        public ArrayList<Object> getTagList() {
            return tagList;
        }

        public Date getCreatedAt() {
            return createdAt;
        }

        public Date getUpdatedAt() {
            return updatedAt;
        }

        public boolean isFavorited() {
            return favorited;
        }

        public int getFavoritesCount() {
            return favoritesCount;
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
