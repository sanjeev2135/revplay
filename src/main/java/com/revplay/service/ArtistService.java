package com.revplay.service;

import com.revplay.dao.AlbumDao;
import com.revplay.dao.ArtistDao;
import com.revplay.dao.SongDao;
import com.revplay.model.Album;
import com.revplay.model.Artist;
import com.revplay.model.Song;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ArtistService {
    private static final Logger logger = LogManager.getLogger(ArtistService.class);
    private final ArtistDao artistDao = new ArtistDao();
    private final AlbumDao albumDao = new AlbumDao();

    public boolean createArtistProfile(Artist artist) {
        try {
            logger.info("Creating artist profile for userId: {}", artist.getUserId());
            return artistDao.createArtist(artist);
        } catch (Exception e) {
            logger.error("Artist profile creation failed: {}", e.getMessage());
            return false;
        }
    }

    public Artist getArtistProfile(long userId) {
        try {
            logger.debug("Fetching artist profile for userId: {}", userId);
            return artistDao.getArtistByUserId(userId);
        } catch (Exception e) {
            logger.error("Failed to fetch artist profile: {}", e.getMessage());
            return null;
        }
    }

    public boolean updateArtistProfile(Artist artist) {
        try {
            logger.info("Updating artist profile for userId: {}", artist.getUserId());
            return artistDao.updateArtist(artist);
        } catch (Exception e) {
            logger.error("Artist profile update failed: {}", e.getMessage());
            return false;
        }
    }

    public long getArtistIdByUserId(long userId) {
        try {
            return artistDao.findArtistIdByUserId(userId);
        } catch (Exception e) {
            logger.error("Failed to get artist ID: {}", e.getMessage());
            return -1;
        }
    }

    public boolean uploadSong(long artistId, String title, String genre, int duration) {
        try {
            return SongDao.uploadSong(artistId, title, genre, duration);
        } catch (Exception e) {
            logger.error("Upload failed: {}", e.getMessage());
            return false;
        }
    }

    public List<Artist> searchArtistsByName(String keyword) {
        try {
            logger.info("Searching artists with keyword: {}", keyword);
            return artistDao.searchArtistsByName(keyword);
        } catch (Exception e) {
            logger.error("Artist search failed: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    // Album Management
    public List<Album> getMyAlbums(long artistId) {
        try {
            logger.info("Getting albums for artist: {}", artistId);
            return albumDao.getAlbumsByArtist(artistId);
        } catch (Exception e) {
            logger.error("Failed to get albums: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean createAlbum(long artistId, String title, String description) {
        try {
            logger.info("Creating album '{}' for artist {}", title, artistId);
            Album album = new Album();
            album.setArtistId(artistId);
            album.setTitle(title);
            album.setDescription(description);
            return albumDao.createAlbum(album) > 0;
        } catch (Exception e) {
            logger.error("Album creation failed: {}", e.getMessage());
            return false;
        }
    }

    public boolean updateAlbum(Album album) {
        try {
            logger.info("Updating album {}", album.getAlbumId());
            return albumDao.updateAlbum(album);
        } catch (Exception e) {
            logger.error("Album update failed: {}", e.getMessage());
            return false;
        }
    }

    public boolean deleteAlbum(long albumId, long artistId) {
        try {
            logger.info("Deleting album {} by artist {}", albumId, artistId);
            return albumDao.deleteAlbum(albumId, artistId);
        } catch (Exception e) {
            logger.error("Album deletion failed: {}", e.getMessage());
            return false;
        }
    }
}
