package com.revplay.service;

import com.revplay.dao.FavoriteDao;
import com.revplay.model.Favorite;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class FavoriteService {
    private static final Logger logger = LogManager.getLogger(FavoriteService.class);
    private final FavoriteDao favoriteDao = new FavoriteDao();

    public boolean addFavorite(long userId, long songId) {
        try {
            logger.info("Adding favorite: userId={}, songId={}", userId, songId);
            return favoriteDao.addFavorite(userId, songId);
        } catch (Exception e) {
            logger.error("Failed to add favorite: {}", e.getMessage());
            return false;
        }
    }

    public boolean removeFavorite(long userId, long songId) {
        try {
            logger.info("Removing favorite: userId={}, songId={}", userId, songId);
            return favoriteDao.removeFavorite(userId, songId);
        } catch (Exception e) {
            logger.error("Failed to remove favorite: {}", e.getMessage());
            return false;
        }
    }

    public List<Favorite> getUserFavorites(long userId) {
        try {
            logger.info("Getting favorites for userId={}", userId);
            return favoriteDao.getUserFavorites(userId);
        } catch (Exception e) {
            logger.error("Failed to get user favorites: {}", e.getMessage());
            return List.of();
        }
    }

    public boolean isFavorite(long userId, long songId) {
        try {
            return favoriteDao.isFavorite(userId, songId);
        } catch (Exception e) {
            logger.error("Failed to check favorite status: {}", e.getMessage());
            return false;
        }
    }

    public int getFavoriteCount(long songId) {
        try {
            return favoriteDao.getFavoriteCount(songId);
        } catch (Exception e) {
            logger.error("Failed to get favorite count: {}", e.getMessage());
            return 0;
        }
    }

    public void toggleFavorite(long userId, long songId) {
        if (isFavorite(userId, songId)) {
            removeFavorite(userId, songId);
            logger.info("Removed song {} from user {} favorites", songId, userId);
        } else {
            addFavorite(userId, songId);
            logger.info("Added song {} to user {} favorites", songId, userId);
        }
    }
}
