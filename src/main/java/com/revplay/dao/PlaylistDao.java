package com.revplay.dao;

import com.revplay.model.Playlist;
import com.revplay.model.PlaylistSong;
import com.revplay.model.Song;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistDao {

    public boolean createPlaylist(Playlist playlist) throws SQLException {
        String sql = "{call create_playlist(?,?,?,?)}";
        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setLong(1, playlist.getUserId());
            cs.setString(2, playlist.getName());
            cs.setString(3, playlist.getDescription());
            cs.setString(4, playlist.isPublic() ? "Y" : "N");

            cs.executeUpdate();
            return true;
        }
    }

    public long findPlaylistIdByName(long ownerId, String name) throws SQLException {
        String sql = "SELECT playlist_id FROM playlists " +
                "WHERE user_id = ? AND LOWER(name) = LOWER(?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, ownerId);
            ps.setString(2, name);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("playlist_id");
                }
            }
        }
        return -1;
    }

    public boolean addSongToPlaylist(PlaylistSong playlistSong) throws SQLException {
        String sql = "{call add_song_to_playlist(?,?)}";
        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setLong(1, playlistSong.getPlaylistId());
            cs.setLong(2, playlistSong.getSongId());
            return cs.executeUpdate() > 0;
        }
    }

    public boolean removeSongFromPlaylist(PlaylistSong playlistSong) throws SQLException {
        String sql = "{call remove_song_from_playlist(?,?)}";
        try (Connection conn = ConnectionFactory.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setLong(1, playlistSong.getPlaylistId());
            cs.setLong(2, playlistSong.getSongId());
            return cs.executeUpdate() > 0;
        }
    }

    public List<Playlist> getPlaylistsByUser(long userId) throws SQLException {
        String sql = "SELECT * FROM playlists WHERE user_id = ? ORDER BY created_at DESC";
        List<Playlist> playlists = new ArrayList<>();
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Playlist playlist = new Playlist();
                    playlist.setPlaylistId(rs.getLong("playlist_id"));
                    playlist.setUserId(rs.getLong("user_id"));
                    playlist.setName(rs.getString("name"));
                    playlist.setDescription(rs.getString("description"));
                    playlist.setPublic("Y".equals(rs.getString("is_public")));
                    playlist.setCreatedAt(rs.getTimestamp("created_at"));
                    playlist.setUpdatedAt(rs.getTimestamp("updated_at"));
                    playlists.add(playlist);
                }
            }
        }
        return playlists;
    }

    public List<Song> getSongsInPlaylist(long playlistId) throws SQLException {
        String sql = "SELECT s.*, u.username as artist_name " +
                    "FROM playlist_songs ps " +
                    "JOIN songs s ON ps.song_id = s.song_id " +
                    "JOIN artists a ON s.artist_id = a.artist_id " +
                    "JOIN users u ON a.user_id = u.user_id " +
                    "WHERE ps.playlist_id = ? " +
                    "ORDER BY ps.position_order";
        
        List<Song> songs = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, playlistId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Song song = new Song();
                    song.setSongId(rs.getLong("song_id"));
                    song.setArtistId(rs.getLong("artist_id"));
                    song.setAlbumId(rs.getLong("album_id"));
                    song.setTitle(rs.getString("title"));
                    song.setGenre(rs.getString("genre"));
                    song.setDurationSeconds(rs.getInt("duration_seconds"));
                    song.setFileUrl(rs.getString("file_url"));
                    song.setPlayCount(rs.getLong("play_count"));
                    song.setArtistName(rs.getString("artist_name"));
                    
                    Date releaseDate = rs.getDate("release_date");
                    if (releaseDate != null) {
                        song.setReleaseDate(releaseDate.toLocalDate());
                    }
                    
                    songs.add(song);
                }
            }
        }
        return songs;
    }

    public boolean updatePlaylist(Playlist playlist) throws SQLException {
        String sql = "UPDATE playlists SET name = ?, description = ?, is_public = ?, updated_at = CURRENT_TIMESTAMP WHERE playlist_id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, playlist.getName());
            ps.setString(2, playlist.getDescription());
            ps.setString(3, playlist.isPublic() ? "Y" : "N");
            ps.setLong(4, playlist.getPlaylistId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deletePlaylist(long playlistId, long userId) throws SQLException {
        String sql = "DELETE FROM playlists WHERE playlist_id = ? AND user_id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, playlistId);
            ps.setLong(2, userId);
            return ps.executeUpdate() > 0;
        }
    }

    public List<Playlist> getPublicPlaylists() throws SQLException {
        String sql = "SELECT * FROM playlists WHERE is_public = 'Y' ORDER BY created_at DESC";
        List<Playlist> playlists = new ArrayList<>();
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Playlist playlist = new Playlist();
                playlist.setPlaylistId(rs.getLong("playlist_id"));
                playlist.setUserId(rs.getLong("user_id"));
                playlist.setName(rs.getString("name"));
                playlist.setDescription(rs.getString("description"));
                playlist.setPublic("Y".equals(rs.getString("is_public")));
                playlist.setCreatedAt(rs.getTimestamp("created_at"));
                playlist.setUpdatedAt(rs.getTimestamp("updated_at"));
                playlists.add(playlist);
            }
        }
        return playlists;
    }
}

