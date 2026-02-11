package com.revplay.model;

import java.sql.Date;

public class PodcastEpisode {
    private long episodeId;
    private long podcastId;
    private String title;
    private String description;
    private int durationSeconds;
    private String fileUrl;
    private Date publishDate;
    private long playCount;
    private java.sql.Timestamp createdAt;

    public PodcastEpisode() {}

    public PodcastEpisode(long podcastId, String title, String description, int durationSeconds, Date publishDate) {
        this.podcastId = podcastId;
        this.title = title;
        this.description = description;
        this.durationSeconds = durationSeconds;
        this.publishDate = publishDate;
        this.playCount = 0;
    }

    public long getEpisodeId() { return episodeId; }
    public void setEpisodeId(long episodeId) { this.episodeId = episodeId; }

    public long getPodcastId() { return podcastId; }
    public void setPodcastId(long podcastId) { this.podcastId = podcastId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(int durationSeconds) { this.durationSeconds = durationSeconds; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }

    public Date getPublishDate() { return publishDate; }
    public void setPublishDate(Date publishDate) { this.publishDate = publishDate; }

    public long getPlayCount() { return playCount; }
    public void setPlayCount(long playCount) { this.playCount = playCount; }

    public java.sql.Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(java.sql.Timestamp createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "PodcastEpisode{id=" + episodeId + ", title='" + title + "', podcastId=" + podcastId + "}";
    }
}
