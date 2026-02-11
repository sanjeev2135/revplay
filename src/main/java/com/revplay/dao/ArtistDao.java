package com.revplay.dao;

import com.revplay.model.Artist;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArtistDao {

    public boolean createArtist(Artist artist) throws SQLException {
        String sql = "INSERT INTO artists (user_id, bio, genre, social_media_links) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, artist.getUserId());
            ps.setString(2, artist.getBio());
            ps.setString(3, artist.getGenre());
            ps.setString(4, artist.getSocialLinks());

            return ps.executeUpdate() > 0;
        }
    }

    public long findArtistIdByUserId(long userId) throws SQLException {
        String sql = "SELECT artist_id FROM artists WHERE user_id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("artist_id");
                }
            }
        }
        return -1;
    }

    public Artist getArtistByUserId(long userId) throws SQLException {
        String sql = "SELECT * FROM artists WHERE user_id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Artist artist = new Artist();
                    artist.setArtistId(rs.getLong("artist_id"));
                    artist.setUserId(rs.getLong("user_id"));
                    artist.setGenre(rs.getString("genre"));
                    artist.setBio(rs.getString("bio"));
                    artist.setSocialLinks(rs.getString("social_media_links"));
                    return artist;
                }
            }
        }
        return null;
    }

    public List<Artist> searchArtistsByName(String keyword) throws SQLException {
        String sql = "SELECT a.*, u.username FROM artists a JOIN users u ON a.user_id = u.user_id WHERE LOWER(u.username) LIKE LOWER(?)";
        List<Artist> artists = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Artist artist = new Artist();
                    artist.setArtistId(rs.getLong("artist_id"));
                    artist.setUserId(rs.getLong("user_id"));
                    artist.setStageName(rs.getString("username"));
                    artist.setGenre(rs.getString("genre"));
                    artist.setBio(rs.getString("bio"));
                    artist.setSocialLinks(rs.getString("social_media_links"));
                    artists.add(artist);
                }
            }
        }
        return artists;
    }

    public boolean updateArtist(Artist artist) throws SQLException {
        String sql = "UPDATE artists SET bio = ?, genre = ?, social_media_links = ? WHERE user_id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, artist.getBio());
            ps.setString(2, artist.getGenre());
            ps.setString(3, artist.getSocialLinks());
            ps.setLong(4, artist.getUserId());

            return ps.executeUpdate() > 0;
        }
    }
}
