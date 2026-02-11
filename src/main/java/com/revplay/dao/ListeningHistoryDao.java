package com.revplay.dao;

import com.revplay.model.ListeningHistory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ListeningHistoryDao {

    public boolean addToListeningHistory(long userId, long songId, int durationPlayedSeconds) throws SQLException {
        String sql = "INSERT INTO listening_history (user_id, song_id, duration_played_seconds) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ps.setLong(2, songId);
            ps.setInt(3, durationPlayedSeconds);
            return ps.executeUpdate() > 0;
        }
    }

    public List<ListeningHistory> getUserListeningHistory(long userId, int limit) throws SQLException {
        String sql = "SELECT lh.*, s.title as song_title, u.username as artist_name " +
                    "FROM listening_history lh " +
                    "JOIN songs s ON lh.song_id = s.song_id " +
                    "JOIN artists a ON s.artist_id = a.artist_id " +
                    "JOIN users u ON a.user_id = u.user_id " +
                    "WHERE lh.user_id = ? " +
                    "ORDER BY lh.played_at DESC " +
                    "FETCH FIRST ? ROWS ONLY";
        
        List<ListeningHistory> history = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ListeningHistory entry = new ListeningHistory();
                    entry.setHistoryId(rs.getLong("history_id"));
                    entry.setUserId(rs.getLong("user_id"));
                    entry.setSongId(rs.getLong("song_id"));
                    entry.setPlayedAt(rs.getTimestamp("played_at"));
                    entry.setDurationPlayedSeconds(rs.getInt("duration_played_seconds"));
                    entry.setSongTitle(rs.getString("song_title"));
                    entry.setArtistName(rs.getString("artist_name"));
                    history.add(entry);
                }
            }
        }
        return history;
    }

    public List<ListeningHistory> getRecentlyPlayed(long userId, int limit) throws SQLException {
        return getUserListeningHistory(userId, limit);
    }

    public List<ListeningHistory> getRecentlyPlayedDistinct(long userId, int limit) throws SQLException {
        String sql = "SELECT DISTINCT lh.song_id, s.title as song_title, u.username as artist_name, " +
                    "MAX(lh.played_at) as last_played_at " +
                    "FROM listening_history lh " +
                    "JOIN songs s ON lh.song_id = s.song_id " +
                    "JOIN artists a ON s.artist_id = a.artist_id " +
                    "JOIN users u ON a.user_id = u.user_id " +
                    "WHERE lh.user_id = ? " +
                    "GROUP BY lh.song_id, s.title, u.username " +
                    "ORDER BY last_played_at DESC " +
                    "FETCH FIRST ? ROWS ONLY";
        
        List<ListeningHistory> history = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ListeningHistory entry = new ListeningHistory();
                    entry.setUserId(userId);
                    entry.setSongId(rs.getLong("song_id"));
                    entry.setPlayedAt(rs.getTimestamp("last_played_at"));
                    entry.setSongTitle(rs.getString("song_title"));
                    entry.setArtistName(rs.getString("artist_name"));
                    history.add(entry);
                }
            }
        }
        return history;
    }

    public boolean clearListeningHistory(long userId) throws SQLException {
        String sql = "DELETE FROM listening_history WHERE user_id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            return ps.executeUpdate() > 0;
        }
    }

    public int getTotalListeningTime(long userId) throws SQLException {
        String sql = "SELECT SUM(duration_played_seconds) as total_time FROM listening_history WHERE user_id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total_time");
                }
            }
        }
        return 0;
    }
}
