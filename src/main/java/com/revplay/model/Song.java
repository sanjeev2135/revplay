package com.revplay.model;

import java.time.LocalDate;

public class Song {
    private long songId;
    private long artistId;
    private long albumId;
    private String title;
    private String genre;
    private int durationSeconds;
    private LocalDate releaseDate;
    private String filePath;  // For upload simulation
    private String fileUrl;
    private long playCount;
    private String artistName; // For display purposes

    public Song() {}

    public Song(long albumId, String title, String genre, int durationSeconds) {
        this.albumId = albumId;
        this.title = title;
        this.genre = genre;
        this.durationSeconds = durationSeconds;
    }

    // Getters and setters
    public long getSongId() { return songId; }
    public void setSongId(long songId) { this.songId = songId; }

    public long getArtistId() { return artistId; }
    public void setArtistId(long artistId) { this.artistId = artistId; }

    public long getAlbumId() { return albumId; }
    public void setAlbumId(long albumId) { this.albumId = albumId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public int getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(int durationSeconds) { this.durationSeconds = durationSeconds; }

    public LocalDate getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }

    public long getPlayCount() { return playCount; }
    public void setPlayCount(long playCount) { this.playCount = playCount; }

    public String getArtistName() { return artistName; }
    public void setArtistName(String artistName) { this.artistName = artistName; }

    @Override
    public String toString() {
        return "Song{id=" + songId + ", title='" + title + "', duration=" + durationSeconds + "s}";
    }
}
