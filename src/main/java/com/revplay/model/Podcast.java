package com.revplay.model;

public class Podcast {
    private long podcastId;
    private String title;
    private String hostName;
    private String description;
    private String genre;
    private String coverImageUrl;
    private java.sql.Timestamp createdAt;
    private java.sql.Timestamp updatedAt;

    public Podcast() {}

    public Podcast(String title, String hostName, String description, String genre) {
        this.title = title;
        this.hostName = hostName;
        this.description = description;
        this.genre = genre;
    }

    public long getPodcastId() { return podcastId; }
    public void setPodcastId(long podcastId) { this.podcastId = podcastId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getHostName() { return hostName; }
    public void setHostName(String hostName) { this.hostName = hostName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getCoverImageUrl() { return coverImageUrl; }
    public void setCoverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; }

    public java.sql.Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(java.sql.Timestamp createdAt) { this.createdAt = createdAt; }

    public java.sql.Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(java.sql.Timestamp updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "Podcast{id=" + podcastId + ", title='" + title + "', host='" + hostName + "'}";
    }
}
