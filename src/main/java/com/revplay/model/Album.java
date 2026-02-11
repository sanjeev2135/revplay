package com.revplay.model;

import java.time.LocalDate;

public class Album {
    private long albumId;
    private long artistId;
    private String title;
    private String description;
    private LocalDate releaseDate;
    private String coverPath;  // File path for upload simulation

    public Album() {}

    public Album(long artistId, String title, LocalDate releaseDate) {
        this.artistId = artistId;
        this.title = title;
        this.releaseDate = releaseDate;
    }

    // Getters and setters
    public long getAlbumId() { return albumId; }
    public void setAlbumId(long albumId) { this.albumId = albumId; }

    public long getArtistId() { return artistId; }
    public void setArtistId(long artistId) { this.artistId = artistId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }

    public String getCoverPath() { return coverPath; }
    public void setCoverPath(String coverPath) { this.coverPath = coverPath; }

    @Override
    public String toString() {
        return "Album{id=" + albumId + ", title='" + title + "', artistId=" + artistId + "}";
    }
}
