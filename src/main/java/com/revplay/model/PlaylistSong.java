package com.revplay.model;

public class PlaylistSong {
    private long playlistId;
    private long songId;

    public PlaylistSong() {}

    public PlaylistSong(long playlistId, long songId) {
        this.playlistId = playlistId;
        this.songId = songId;
    }

    // Getters and setters
    public long getPlaylistId() { return playlistId; }
    public void setPlaylistId(long playlistId) { this.playlistId = playlistId; }

    public long getSongId() { return songId; }
    public void setSongId(long songId) { this.songId = songId; }

    @Override
    public String toString() {
        return "PlaylistSong{playlistId=" + playlistId + ", songId=" + songId + "}";
    }
}
