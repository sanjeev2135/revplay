package com.revplay.dao;

import com.revplay.model.Song;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SongDao {

    public static boolean uploadSong(long artistId, String title, String genre, int duration) throws SQLException {
        // Find the artist's default album
        long albumId = findDefaultAlbumForArtist(artistId);
        if (albumId == -1) {
            throw new SQLException("No album found for artist. Please create an album first.");
        }

        String sql = "{call upload_song(?,?,?,?,?,?)}";
        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setLong(1, albumId);
            cs.setString(2, title);
            cs.setString(3, genre);
            cs.setInt(4, duration);
            cs.setDate(5, new java.sql.Date(System.currentTimeMillis()));
            cs.setString(6, "/uploads/" + title.replace(" ", "_") + ".mp3");

            cs.execute();
            return true;
        }
    }

    private static long findDefaultAlbumForArtist(long artistId) throws SQLException {
        String sql = "SELECT album_id FROM albums WHERE artist_id = ? ORDER BY album_id FETCH FIRST 1 ROWS ONLY";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, artistId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("album_id");
                }
            }
        }
        return -1;
    }

    public boolean uploadSongWithAlbum(long artistId, long albumId, String title, String genre, int duration) throws SQLException {
        String sql = "INSERT INTO songs (title, artist_id, album_id, genre, duration_seconds, release_date, file_url) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, title);
            ps.setLong(2, artistId);
            ps.setLong(3, albumId);
            ps.setString(4, genre);
            ps.setInt(5, duration);
            ps.setDate(6, new java.sql.Date(System.currentTimeMillis()));
            ps.setString(7, "/uploads/" + title.replace(" ", "_") + ".mp3");

            return ps.executeUpdate() > 0;
        }
    }

    public List<Song> searchSongs(String keyword) throws SQLException {
        String sql = "SELECT s.*, u.username as artist_name " +
                    "FROM songs s " +
                    "JOIN artists a ON s.artist_id = a.artist_id " +
                    "JOIN users u ON a.user_id = u.user_id " +
                    "WHERE LOWER(s.title) LIKE LOWER(?) OR LOWER(s.genre) LIKE LOWER(?) OR LOWER(u.username) LIKE LOWER(?) " +
                    "ORDER BY s.title";
        
        List<Song> songs = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Song song = mapResultSetToSong(rs);
                    song.setArtistName(rs.getString("artist_name"));
                    songs.add(song);
                }
            }
        }
        return songs;
    }

    public List<Song> getSongsByGenre(String genre) throws SQLException {
        String sql = "SELECT s.*, u.username as artist_name " +
                    "FROM songs s " +
                    "JOIN artists a ON s.artist_id = a.artist_id " +
                    "JOIN users u ON a.user_id = u.user_id " +
                    "WHERE s.genre = ? " +
                    "ORDER BY s.title";
        
        List<Song> songs = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, genre);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Song song = mapResultSetToSong(rs);
                    song.setArtistName(rs.getString("artist_name"));
                    songs.add(song);
                }
            }
        }
        return songs;
    }

    public boolean incrementPlayCount(long songId) throws SQLException {
        String sql = "{call increment_song_play_count(?)}";
        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setLong(1, songId);
            cs.execute();
            return true;
        }
    }

    public boolean updateSong(long songId, String title, String genre, int duration) throws SQLException {
        String sql = "UPDATE songs SET title = ?, genre = ?, duration_seconds = ?, updated_at = CURRENT_TIMESTAMP WHERE song_id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, title);
            ps.setString(2, genre);
            ps.setInt(3, duration);
            ps.setLong(4, songId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteSong(long songId) throws SQLException {
        String sql = "DELETE FROM songs WHERE song_id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, songId);
            return ps.executeUpdate() > 0;
        }
    }

    public List<Song> getAllSongs() throws SQLException {
        String sql = "SELECT s.*, u.username as artist_name " +
                    "FROM songs s " +
                    "JOIN artists a ON s.artist_id = a.artist_id " +
                    "JOIN users u ON a.user_id = u.user_id " +
                    "ORDER BY s.title";
        
        List<Song> songs = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Song song = mapResultSetToSong(rs);
                song.setArtistName(rs.getString("artist_name"));
                songs.add(song);
            }
        }
        return songs;
    }

    private Song mapResultSetToSong(ResultSet rs) throws SQLException {
        Song song = new Song();
        song.setSongId(rs.getLong("song_id"));
        song.setArtistId(rs.getLong("artist_id"));
        song.setAlbumId(rs.getLong("album_id"));
        song.setTitle(rs.getString("title"));
        song.setGenre(rs.getString("genre"));
        song.setDurationSeconds(rs.getInt("duration_seconds"));
        song.setFileUrl(rs.getString("file_url"));
        song.setPlayCount(rs.getLong("play_count"));
        
        Date releaseDate = rs.getDate("release_date");
        if (releaseDate != null) {
            song.setReleaseDate(releaseDate.toLocalDate());
        }
        
        return song;
    }



    public List<Song> searchSongsByArtist(long artistId) throws SQLException {
        List<Song> songs = new ArrayList<>();
        String sql = "SELECT s.* FROM songs s JOIN albums a ON s.album_id = a.album_id " +
                "WHERE a.artist_id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, artistId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Song song = new Song();
                    song.setSongId(rs.getLong("song_id"));
                    song.setTitle(rs.getString("title"));
                    song.setGenre(rs.getString("genre"));
                    song.setDurationSeconds(rs.getInt("duration_seconds"));
                    songs.add(song);
                }
            }
        }
        return songs;
    }

    public Song getSongById(long songId) throws SQLException {
        String sql = "SELECT s.*, u.username as artist_name " +
                "FROM songs s " +
                "JOIN artists a ON s.artist_id = a.artist_id " +
                "JOIN users u ON a.user_id = u.user_id " +
                "WHERE s.song_id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, songId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Song song = new Song();
                    song.setSongId(rs.getLong("song_id"));
                    song.setArtistId(rs.getLong("artist_id"));
                    song.setAlbumId(rs.getLong("album_id"));
                    song.setTitle(rs.getString("title"));
                    song.setGenre(rs.getString("genre"));
                    song.setDurationSeconds(rs.getInt("duration_seconds"));
                    song.setPlayCount(rs.getLong("play_count"));
                    song.setArtistName(rs.getString("artist_name"));
                    
                    Date releaseDate = rs.getDate("release_date");
                    if (releaseDate != null) {
                        song.setReleaseDate(releaseDate.toLocalDate());
                    }
                    
                    song.setFileUrl(rs.getString("file_url"));
                    return song;
                }
            }
        }
        return null;
    }

    public int getSongCountByArtist(long artistId) throws SQLException {
        String sql = "SELECT COUNT(*) as song_count " +
                "FROM songs s " +
                "JOIN albums a ON s.album_id = a.album_id " +
                "WHERE a.artist_id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, artistId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("song_count");
                }
            }
        }
        return 0;
    }


}
