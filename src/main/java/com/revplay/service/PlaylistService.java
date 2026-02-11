package com.revplay.service;

import com.revplay.dao.PlaylistDao;
import com.revplay.model.Playlist;
import com.revplay.model.PlaylistSong;
import com.revplay.model.Song;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.ArrayList;

public class PlaylistService {
    private static final Logger logger = LogManager.getLogger(PlaylistService.class);
    private final PlaylistDao playlistDao = new PlaylistDao();

    public boolean createPlaylist(Playlist playlist) {
        try {
            logger.info("Creating playlist '{}' for user {}", playlist.getName(), playlist.getUserId());
            return playlistDao.createPlaylist(playlist);
        } catch (Exception e) {
            logger.error("Playlist creation failed: {}", e.getMessage());
            return false;
        }
    }

    public boolean addSongToPlaylist(long playlistId, long songId) {
        try {
            PlaylistSong ps = new PlaylistSong(playlistId, songId);
            logger.info("Adding song {} to playlist {}", songId, playlistId);
            return playlistDao.addSongToPlaylist(ps);
        } catch (Exception e) {
            logger.error("Failed to add song to playlist: {}", e.getMessage());
            return false;
        }
    }

    public boolean removeSongFromPlaylist(long playlistId, long songId) {
        try {
            PlaylistSong ps = new PlaylistSong(playlistId, songId);
            logger.info("Removing song {} from playlist {}", songId, playlistId);
            return playlistDao.removeSongFromPlaylist(ps);
        } catch (Exception e) {
            logger.error("Failed to remove song from playlist: {}", e.getMessage());
            return false;
        }
    }

    public long findPlaylistId(long ownerId, String playlistName) {
        try {
            return playlistDao.findPlaylistIdByName(ownerId, playlistName);
        } catch (Exception e) {
            logger.error("Playlist lookup failed: {}", e.getMessage());
            return -1;
        }
    }

    public List<Playlist> getUserPlaylists(long userId) {
        try {
            logger.info("Getting playlists for user {}", userId);
            return playlistDao.getPlaylistsByUser(userId);
        } catch (Exception e) {
            logger.error("Failed to get user playlists: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Song> getPlaylistSongs(long playlistId) {
        try {
            logger.info("Getting songs for playlist {}", playlistId);
            return playlistDao.getSongsInPlaylist(playlistId);
        } catch (Exception e) {
            logger.error("Failed to get playlist songs: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean updatePlaylist(Playlist playlist) {
        try {
            logger.info("Updating playlist {}", playlist.getPlaylistId());
            return playlistDao.updatePlaylist(playlist);
        } catch (Exception e) {
            logger.error("Failed to update playlist: {}", e.getMessage());
            return false;
        }
    }

    public boolean deletePlaylist(long playlistId, long userId) {
        try {
            logger.info("Deleting playlist {} for user {}", playlistId, userId);
            return playlistDao.deletePlaylist(playlistId, userId);
        } catch (Exception e) {
            logger.error("Failed to delete playlist: {}", e.getMessage());
            return false;
        }
    }

    public List<Playlist> getPublicPlaylists() {
        try {
            logger.info("Getting public playlists");
            return playlistDao.getPublicPlaylists();
        } catch (Exception e) {
            logger.error("Failed to get public playlists: {}", e.getMessage());
            return new ArrayList<>();
        }
    }
}
