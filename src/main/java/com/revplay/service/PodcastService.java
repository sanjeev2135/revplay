package com.revplay.service;

import com.revplay.dao.PodcastDao;
import com.revplay.model.Podcast;
import com.revplay.model.PodcastEpisode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class PodcastService {
    private static final Logger logger = LogManager.getLogger(PodcastService.class);
    private final PodcastDao podcastDao = new PodcastDao();

    public boolean createPodcast(Podcast podcast) {
        try {
            logger.info("Creating podcast: {}", podcast.getTitle());
            return podcastDao.createPodcast(podcast);
        } catch (Exception e) {
            logger.error("Failed to create podcast: {}", e.getMessage());
            return false;
        }
    }

    public boolean createEpisode(PodcastEpisode episode) {
        try {
            logger.info("Creating episode: {}", episode.getTitle());
            return podcastDao.createPodcastEpisode(episode);
        } catch (Exception e) {
            logger.error("Failed to create episode: {}", e.getMessage());
            return false;
        }
    }

    public List<Podcast> getAllPodcasts() {
        try {
            logger.info("Getting all podcasts");
            return podcastDao.getAllPodcasts();
        } catch (Exception e) {
            logger.error("Failed to get all podcasts: {}", e.getMessage());
            return List.of();
        }
    }

    public List<Podcast> searchPodcasts(String keyword) {
        try {
            logger.info("Searching podcasts with keyword: {}", keyword);
            return podcastDao.searchPodcasts(keyword);
        } catch (Exception e) {
            logger.error("Failed to search podcasts: {}", e.getMessage());
            return List.of();
        }
    }

    public List<PodcastEpisode> getEpisodesByPodcast(long podcastId) {
        try {
            logger.info("Getting episodes for podcastId={}", podcastId);
            return podcastDao.getEpisodesByPodcast(podcastId);
        } catch (Exception e) {
            logger.error("Failed to get episodes for podcast: {}", e.getMessage());
            return List.of();
        }
    }

    public Podcast getPodcastById(long podcastId) {
        try {
            logger.info("Getting podcast by ID: {}", podcastId);
            return podcastDao.getPodcastById(podcastId);
        } catch (Exception e) {
            logger.error("Failed to get podcast by ID: {}", e.getMessage());
            return null;
        }
    }

    public List<Podcast> getPodcastsByGenre(String genre) {
        try {
            logger.info("Getting podcasts by genre: {}", genre);
            return podcastDao.getPodcastsByGenre(genre);
        } catch (Exception e) {
            logger.error("Failed to get podcasts by genre: {}", e.getMessage());
            return List.of();
        }
    }

    public boolean incrementEpisodePlayCount(long episodeId) {
        try {
            logger.info("Incrementing play count for episodeId={}", episodeId);
            return podcastDao.incrementPlayCount(episodeId);
        } catch (Exception e) {
            logger.error("Failed to increment episode play count: {}", e.getMessage());
            return false;
        }
    }

    public void simulateEpisodePlay(long userId, PodcastEpisode episode) {
        // Simulate playing the entire episode
        incrementEpisodePlayCount(episode.getEpisodeId());
        logger.info("Simulated play of episode '{}' for user {}", episode.getTitle(), userId);
    }
}
