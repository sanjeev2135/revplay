package com.revplay.dao;

import com.revplay.model.Podcast;
import com.revplay.model.PodcastEpisode;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PodcastDao {

    public boolean createPodcast(Podcast podcast) throws SQLException {
        String sql = "INSERT INTO podcasts (podcast_id, title, host_name, description, genre) VALUES (podcasts_seq.NEXTVAL, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, new String[]{"podcast_id"})) {

            ps.setString(1, podcast.getTitle());
            ps.setString(2, podcast.getHostName());
            ps.setString(3, podcast.getDescription());
            ps.setString(4, podcast.getGenre());

            int result = ps.executeUpdate();
            if (result > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        podcast.setPodcastId(generatedKeys.getLong(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean createPodcastEpisode(PodcastEpisode episode) throws SQLException {
        String sql = "INSERT INTO podcast_episodes (episode_id, podcast_id, title, description, duration_seconds, publish_date) VALUES (podcast_episodes_seq.NEXTVAL, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, new String[]{"episode_id"})) {

            ps.setLong(1, episode.getPodcastId());
            ps.setString(2, episode.getTitle());
            ps.setString(3, episode.getDescription());
            ps.setInt(4, episode.getDurationSeconds());
            ps.setDate(5, episode.getPublishDate());

            int result = ps.executeUpdate();
            if (result > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        episode.setEpisodeId(generatedKeys.getLong(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    public List<Podcast> getAllPodcasts() throws SQLException {
        String sql = "SELECT * FROM podcasts ORDER BY title";
        List<Podcast> podcasts = new ArrayList<>();
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Podcast podcast = new Podcast();
                podcast.setPodcastId(rs.getLong("podcast_id"));
                podcast.setTitle(rs.getString("title"));
                podcast.setHostName(rs.getString("host_name"));
                podcast.setDescription(rs.getString("description"));
                podcast.setGenre(rs.getString("genre"));
                podcast.setCoverImageUrl(rs.getString("cover_image_url"));
                podcast.setCreatedAt(rs.getTimestamp("created_at"));
                podcast.setUpdatedAt(rs.getTimestamp("updated_at"));
                podcasts.add(podcast);
            }
        }
        return podcasts;
    }

    public List<Podcast> searchPodcasts(String keyword) throws SQLException {
        String sql = "SELECT * FROM podcasts WHERE LOWER(title) LIKE LOWER(?) OR LOWER(host_name) LIKE LOWER(?) OR LOWER(description) LIKE LOWER(?) ORDER BY title";
        List<Podcast> podcasts = new ArrayList<>();
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Podcast podcast = new Podcast();
                    podcast.setPodcastId(rs.getLong("podcast_id"));
                    podcast.setTitle(rs.getString("title"));
                    podcast.setHostName(rs.getString("host_name"));
                    podcast.setDescription(rs.getString("description"));
                    podcast.setGenre(rs.getString("genre"));
                    podcast.setCoverImageUrl(rs.getString("cover_image_url"));
                    podcasts.add(podcast);
                }
            }
        }
        return podcasts;
    }

    public List<PodcastEpisode> getEpisodesByPodcast(long podcastId) throws SQLException {
        String sql = "SELECT * FROM podcast_episodes WHERE podcast_id = ? ORDER BY publish_date DESC";
        List<PodcastEpisode> episodes = new ArrayList<>();
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, podcastId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PodcastEpisode episode = new PodcastEpisode();
                    episode.setEpisodeId(rs.getLong("episode_id"));
                    episode.setPodcastId(rs.getLong("podcast_id"));
                    episode.setTitle(rs.getString("title"));
                    episode.setDescription(rs.getString("description"));
                    episode.setDurationSeconds(rs.getInt("duration_seconds"));
                    episode.setFileUrl(rs.getString("file_url"));
                    episode.setPublishDate(rs.getDate("publish_date"));
                    episode.setPlayCount(rs.getLong("play_count"));
                    episode.setCreatedAt(rs.getTimestamp("created_at"));
                    episodes.add(episode);
                }
            }
        }
        return episodes;
    }

    public Podcast getPodcastById(long podcastId) throws SQLException {
        String sql = "SELECT * FROM podcasts WHERE podcast_id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, podcastId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Podcast podcast = new Podcast();
                    podcast.setPodcastId(rs.getLong("podcast_id"));
                    podcast.setTitle(rs.getString("title"));
                    podcast.setHostName(rs.getString("host_name"));
                    podcast.setDescription(rs.getString("description"));
                    podcast.setGenre(rs.getString("genre"));
                    podcast.setCoverImageUrl(rs.getString("cover_image_url"));
                    podcast.setCreatedAt(rs.getTimestamp("created_at"));
                    podcast.setUpdatedAt(rs.getTimestamp("updated_at"));
                    return podcast;
                }
            }
        }
        return null;
    }

    public List<Podcast> getPodcastsByGenre(String genre) throws SQLException {
        String sql = "SELECT * FROM podcasts WHERE genre = ? ORDER BY title";
        List<Podcast> podcasts = new ArrayList<>();
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, genre);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Podcast podcast = new Podcast();
                    podcast.setPodcastId(rs.getLong("podcast_id"));
                    podcast.setTitle(rs.getString("title"));
                    podcast.setHostName(rs.getString("host_name"));
                    podcast.setDescription(rs.getString("description"));
                    podcast.setGenre(rs.getString("genre"));
                    podcasts.add(podcast);
                }
            }
        }
        return podcasts;
    }

    public boolean incrementPlayCount(long episodeId) throws SQLException {
        String sql = "UPDATE podcast_episodes SET play_count = play_count + 1 WHERE episode_id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, episodeId);
            return ps.executeUpdate() > 0;
        }
    }
}
