package com.revplay.service;

import com.revplay.dao.ListeningHistoryDao;
import com.revplay.model.ListeningHistory;
import com.revplay.model.Song;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ListeningHistoryService {
    private static final Logger logger = LogManager.getLogger(ListeningHistoryService.class);
    private final ListeningHistoryDao listeningHistoryDao = new ListeningHistoryDao();

    public boolean recordPlay(long userId, long songId, int durationPlayedSeconds) {
        try {
            logger.info("Recording play: userId={}, songId={}, duration={}", userId, songId, durationPlayedSeconds);
            return listeningHistoryDao.addToListeningHistory(userId, songId, durationPlayedSeconds);
        } catch (Exception e) {
            logger.error("Failed to record play: {}", e.getMessage());
            return false;
        }
    }

    public List<ListeningHistory> getUserHistory(long userId, int limit) {
        try {
            logger.info("Getting listening history for userId={}, limit={}", userId, limit);
            return listeningHistoryDao.getUserListeningHistory(userId, limit);
        } catch (Exception e) {
            logger.error("Failed to get user listening history: {}", e.getMessage());
            return List.of();
        }
    }

    public List<ListeningHistory> getRecentlyPlayed(long userId, int limit) {
        try {
            logger.info("Getting recently played for userId={}, limit={}", userId, limit);
            return listeningHistoryDao.getRecentlyPlayedDistinct(userId, limit);
        } catch (Exception e) {
            logger.error("Failed to get recently played: {}", e.getMessage());
            return List.of();
        }
    }

    public boolean clearHistory(long userId) {
        try {
            logger.info("Clearing listening history for userId={}", userId);
            return listeningHistoryDao.clearListeningHistory(userId);
        } catch (Exception e) {
            logger.error("Failed to clear listening history: {}", e.getMessage());
            return false;
        }
    }

    public int getTotalListeningTime(long userId) {
        try {
            return listeningHistoryDao.getTotalListeningTime(userId);
        } catch (Exception e) {
            logger.error("Failed to get total listening time: {}", e.getMessage());
            return 0;
        }
    }

    public void simulateSongPlay(long userId, Song song) {
        // Simulate playing the entire song
        int duration = song.getDurationSeconds();
        recordPlay(userId, song.getSongId(), duration);
        logger.info("Simulated play of '{}' for user {}", song.getTitle(), userId);
    }
}
