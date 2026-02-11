package com.revplay.model;

import java.sql.Timestamp;

public class ListeningHistory {
    private long historyId;
    private long userId;
    private long songId;
    private Timestamp playedAt;
    private int durationPlayedSeconds;
    private String songTitle;
    private String artistName;

    public ListeningHistory() {}

    public ListeningHistory(long userId, long songId, int durationPlayedSeconds) {
        this.userId = userId;
        this.songId = songId;
        this.durationPlayedSeconds = durationPlayedSeconds;
        this.playedAt = new Timestamp(System.currentTimeMillis());
    }

    public long getHistoryId() { return historyId; }
    public void setHistoryId(long historyId) { this.historyId = historyId; }

    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }

    public long getSongId() { return songId; }
    public void setSongId(long songId) { this.songId = songId; }

    public Timestamp getPlayedAt() { return playedAt; }
    public void setPlayedAt(Timestamp playedAt) { this.playedAt = playedAt; }

    public int getDurationPlayedSeconds() { return durationPlayedSeconds; }
    public void setDurationPlayedSeconds(int durationPlayedSeconds) { this.durationPlayedSeconds = durationPlayedSeconds; }

    public String getSongTitle() { return songTitle; }
    public void setSongTitle(String songTitle) { this.songTitle = songTitle; }

    public String getArtistName() { return artistName; }
    public void setArtistName(String artistName) { this.artistName = artistName; }

    @Override
    public String toString() {
        return "ListeningHistory{song='" + songTitle + "', artist='" + artistName + "', playedAt=" + playedAt + "}";
    }
}
