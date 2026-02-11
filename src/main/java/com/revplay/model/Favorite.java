package com.revplay.model;

import java.sql.Timestamp;

public class Favorite {
    private long favoriteId;
    private long userId;
    private long songId;
    private Timestamp addedAt;
    private String songTitle;
    private String artistName;

    public Favorite() {}

    public Favorite(long userId, long songId) {
        this.userId = userId;
        this.songId = songId;
        this.addedAt = new Timestamp(System.currentTimeMillis());
    }

    public long getFavoriteId() { return favoriteId; }
    public void setFavoriteId(long favoriteId) { this.favoriteId = favoriteId; }

    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }

    public long getSongId() { return songId; }
    public void setSongId(long songId) { this.songId = songId; }

    public Timestamp getAddedAt() { return addedAt; }
    public void setAddedAt(Timestamp addedAt) { this.addedAt = addedAt; }

    public String getSongTitle() { return songTitle; }
    public void setSongTitle(String songTitle) { this.songTitle = songTitle; }

    public String getArtistName() { return artistName; }
    public void setArtistName(String artistName) { this.artistName = artistName; }

    @Override
    public String toString() {
        return "Favorite{song='" + songTitle + "', artist='" + artistName + "', addedAt=" + addedAt + "}";
    }
}
