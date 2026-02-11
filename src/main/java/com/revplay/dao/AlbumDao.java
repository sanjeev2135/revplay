package com.revplay.dao;

import com.revplay.model.Album;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlbumDao {

    public long createAlbum(Album album) throws SQLException {
        String sql = "INSERT INTO albums (artist_id, title, description, release_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, album.getArtistId());
            ps.setString(2, album.getTitle());
            ps.setString(3, album.getDescription());
            ps.setDate(4, new java.sql.Date(System.currentTimeMillis()));

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getLong(1);
                    }
                }
            }
        }
        return -1;
    }

    public boolean hasAlbumForArtist(long artistId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM albums WHERE artist_id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, artistId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public Album getAlbumById(long albumId) throws SQLException {
        String sql = "SELECT * FROM albums WHERE album_id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, albumId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAlbum(rs);
                }
            }
        }
        return null;
    }

    public List<Album> getAlbumsByArtist(long artistId) throws SQLException {
        String sql = "SELECT * FROM albums WHERE artist_id = ? ORDER BY release_date DESC";
        List<Album> albums = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, artistId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    albums.add(mapResultSetToAlbum(rs));
                }
            }
        }
        return albums;
    }

    public boolean updateAlbum(Album album) throws SQLException {
        String sql = "UPDATE albums SET title = ?, description = ? WHERE album_id = ? AND artist_id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, album.getTitle());
            ps.setString(2, album.getDescription());
            ps.setLong(3, album.getAlbumId());
            ps.setLong(4, album.getArtistId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteAlbum(long albumId, long artistId) throws SQLException {
        String sql = "DELETE FROM albums WHERE album_id = ? AND artist_id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, albumId);
            ps.setLong(2, artistId);
            return ps.executeUpdate() > 0;
        }
    }

    private Album mapResultSetToAlbum(ResultSet rs) throws SQLException {
        Album album = new Album();
        album.setAlbumId(rs.getLong("album_id"));
        album.setArtistId(rs.getLong("artist_id"));
        album.setTitle(rs.getString("title"));
        album.setDescription(rs.getString("description"));
        Date releaseDate = rs.getDate("release_date");
        if (releaseDate != null) {
            album.setReleaseDate(releaseDate.toLocalDate());
        }
        return album;
    }
}
