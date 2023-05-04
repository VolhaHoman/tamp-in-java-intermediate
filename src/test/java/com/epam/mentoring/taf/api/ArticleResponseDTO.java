package com.epam.mentoring.taf.api;

import java.util.List;

public class ArticleResponseDTO {

    private Article article;

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public class Article {

        private String slug;

        private String title;

        private String description;

        private String body;

        private List<String> tagList;

        private String createdAt;

        private String updatedAt;

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

        public List<String> getTagList() {
            return tagList;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public String getUpdatedAt() {
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
            private String bio;
            private String image;
            private boolean following;

            public String getUsername() {
                return username;
            }

            public String getBio() {
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
