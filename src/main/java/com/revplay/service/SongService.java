package com.revplay.service;

import com.revplay.dao.SongDao;
import com.revplay.model.Song;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.ArrayList;

public class SongService {
    private static final Logger logger = LogManager.getLogger(SongService.class);
    private final SongDao songDao = new SongDao();

    public boolean uploadSong(long artistId, String title, String genre, int duration) {
        try {
            logger.info("Uploading '{}' by artist {}", title, artistId);
            return songDao.uploadSong(artistId, title, genre, duration);
        } catch (Exception e) {
            logger.error("Upload failed: {}", e.getMessage());
            return false;
        }
    }

    public boolean uploadSongWithAlbum(long artistId, long albumId, String title, String genre, int duration) {
        try {
            logger.info("Uploading '{}' to album {} by artist {}", title, albumId, artistId);
            return songDao.uploadSongWithAlbum(artistId, albumId, title, genre, duration);
        } catch (Exception e) {
            logger.error("Upload failed: {}", e.getMessage());
            return false;
        }
    }

    public List<Song> searchSongs(String keyword) {
        try {
            logger.info("Searching songs with keyword: {}", keyword);
            return songDao.searchSongs(keyword);
        } catch (Exception e) {
            logger.error("Search failed: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Song> getSongsByGenre(String genre) {
        try {
            logger.info("Getting songs by genre: {}", genre);
            return songDao.getSongsByGenre(genre);
        } catch (Exception e) {
            logger.error("Failed to get songs by genre: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    public Song getSongById(long songId) {
        try {
            logger.info("Getting song by ID: {}", songId);
            return songDao.getSongById(songId);
        } catch (Exception e) {
            logger.error("Failed to get song by ID: {}", e.getMessage());
            return null;
        }
    }

    public List<Song> getAllSongs() {
        try {
            logger.info("Getting all songs");
            return songDao.getAllSongs();
        } catch (Exception e) {
            logger.error("Failed to get all songs: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean updateSong(long songId, String title, String genre, int duration) {
        try {
            logger.info("Updating song {}: title={}, genre={}, duration={}", songId, title, genre, duration);
            return songDao.updateSong(songId, title, genre, duration);
        } catch (Exception e) {
            logger.error("Failed to update song: {}", e.getMessage());
            return false;
        }
    }

    public boolean deleteSong(long songId) {
        try {
            logger.info("Deleting song {}", songId);
            return songDao.deleteSong(songId);
        } catch (Exception e) {
            logger.error("Failed to delete song: {}", e.getMessage());
            return false;
        }
    }

    public List<Song> getSongsByArtist(long artistId) {
        try {
            logger.info("Getting songs by artist: {}", artistId);
            return songDao.searchSongsByArtist(artistId);
        } catch (Exception e) {
            logger.error("Failed to get songs by artist: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean incrementPlayCount(long songId) {
        try {
            return songDao.incrementPlayCount(songId);
        } catch (Exception e) {
            logger.error("Failed to increment play count: {}", e.getMessage());
            return false;
        }
    }
}
