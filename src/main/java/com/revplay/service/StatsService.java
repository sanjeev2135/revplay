package com.revplay.service;

import com.revplay.dao.SongDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StatsService {
    private static final Logger logger = LogManager.getLogger(StatsService.class);
    private final SongDao songDao = new SongDao();

    public int getUserSongCount(long userId) {
        try {
            // Simulate count of user's songs (artists only)
            int count = songDao.getSongCountByArtist(userId);  // Implement in SongDao
            logger.info("User {} has {} songs", userId, count);
            return count;
        } catch (Exception e) {
            logger.error("Failed to get song count: {}", e.getMessage());
            return 0;
        }
    }
}
