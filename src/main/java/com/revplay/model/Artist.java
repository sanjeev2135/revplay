package com.revplay.model;

public class Artist {
    private long artistId;
    private long userId;        // References User
    private String stageName;
    private String genre;
    private String bio;
    private String socialLinks; // JSON or delimited string

    public Artist() {}

    public Artist(long userId, String stageName, String genre) {
        this.userId = userId;
        this.stageName = stageName;
        this.genre = genre;
    }

    // Getters and setters
    public long getArtistId() { return artistId; }
    public void setArtistId(long artistId) { this.artistId = artistId; }

    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }

    public String getStageName() { return stageName; }
    public void setStageName(String stageName) { this.stageName = stageName; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getSocialLinks() { return socialLinks; }
    public void setSocialLinks(String socialLinks) { this.socialLinks = socialLinks; }

    @Override
    public String toString() {
        return "Artist{id=" + artistId + ", stageName='" + stageName + "', genre='" + genre + "'}";
    }
}
