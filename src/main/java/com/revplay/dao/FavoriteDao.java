package com.revplay.dao;

import com.revplay.model.Favorite;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FavoriteDao {

    public boolean addFavorite(long userId, long songId) throws SQLException {
        // Oracle MERGE statement for upsert
        String sql = "MERGE INTO favorites f " +
                    "USING (SELECT ? as user_id, ? as song_id FROM dual) src " +
                    "ON (f.user_id = src.user_id AND f.song_id = src.song_id) " +
                    "WHEN MATCHED THEN UPDATE SET added_at = CURRENT_TIMESTAMP " +
                    "WHEN NOT MATCHED THEN INSERT (user_id, song_id) VALUES (src.user_id, src.song_id)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ps.setLong(2, songId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean removeFavorite(long userId, long songId) throws SQLException {
        String sql = "DELETE FROM favorites WHERE user_id = ? AND song_id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ps.setLong(2, songId);
            return ps.executeUpdate() > 0;
        }
    }

    public List<Favorite> getUserFavorites(long userId) throws SQLException {
        String sql = "SELECT f.*, s.title as song_title, u.username as artist_name " +
                    "FROM favorites f " +
                    "JOIN songs s ON f.song_id = s.song_id " +
                    "JOIN artists a ON s.artist_id = a.artist_id " +
                    "JOIN users u ON a.user_id = u.user_id " +
                    "WHERE f.user_id = ? " +
                    "ORDER BY f.added_at DESC";
        
        List<Favorite> favorites = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Favorite favorite = new Favorite();
                    favorite.setFavoriteId(rs.getLong("favorite_id"));
                    favorite.setUserId(rs.getLong("user_id"));
                    favorite.setSongId(rs.getLong("song_id"));
                    favorite.setAddedAt(rs.getTimestamp("added_at"));
                    favorite.setSongTitle(rs.getString("song_title"));
                    favorite.setArtistName(rs.getString("artist_name"));
                    favorites.add(favorite);
                }
            }
        }
        return favorites;
    }

    public boolean isFavorite(long userId, long songId) throws SQLException {
        String sql = "SELECT 1 FROM favorites WHERE user_id = ? AND song_id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ps.setLong(2, songId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public int getFavoriteCount(long songId) throws SQLException {
        String sql = "SELECT COUNT(*) as count FROM favorites WHERE song_id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, songId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count");
                }
            }
        }
        return 0;
    }
}
