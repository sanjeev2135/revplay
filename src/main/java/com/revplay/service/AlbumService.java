package com.revplay.service;

import com.revplay.dao.AlbumDao;
import com.revplay.model.Album;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class AlbumService {
    private static final Logger logger = LogManager.getLogger(AlbumService.class);
    private final AlbumDao albumDao = new AlbumDao();

    public List<Album> getAllAlbums() {
        try {
            logger.info("Getting all albums");
            // This would need a new method in AlbumDao, using getAlbumsByArtist with a join for now
            return new ArrayList<>();
        } catch (Exception e) {
            logger.error("Failed to get albums: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Album> getAlbumsByArtist(long artistId) {
        try {
            logger.info("Getting albums for artist: {}", artistId);
            return albumDao.getAlbumsByArtist(artistId);
        } catch (Exception e) {
            logger.error("Failed to get albums by artist: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    public Album getAlbumById(long albumId) {
        try {
            logger.info("Getting album by ID: {}", albumId);
            return albumDao.getAlbumById(albumId);
        } catch (Exception e) {
            logger.error("Failed to get album: {}", e.getMessage());
            return null;
        }
    }
}
