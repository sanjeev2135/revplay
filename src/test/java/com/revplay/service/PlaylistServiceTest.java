package com.revplay.service;

import com.revplay.model.Playlist;
import com.revplay.model.Song;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class PlaylistServiceTest {

    private PlaylistService playlistService;

    @BeforeEach
    void setUp() {
        playlistService = new PlaylistService();
    }

    @Test
    void testPlaylistServiceCreation() {
        assertNotNull(playlistService);
    }

    @Test
    void testPlaylistModelCreation() {
        Playlist playlist = new Playlist();
        
        playlist.setPlaylistId(1L);
        playlist.setUserId(123L);
        playlist.setName("My Favorite Songs");
        playlist.setDescription("A collection of my favorite tracks");
        playlist.setPublic(true);
        
        assertEquals(1L, playlist.getPlaylistId());
        assertEquals(123L, playlist.getUserId());
        assertEquals("My Favorite Songs", playlist.getName());
        assertEquals("A collection of my favorite tracks", playlist.getDescription());
        assertTrue(playlist.isPublic());
    }

    @Test
    void testPlaylistModelWithAllFields() {
        Playlist playlist = createTestPlaylist(1L, 100L, "Workout Mix", "High energy songs for workout", false);
        
        assertEquals(1L, playlist.getPlaylistId());
        assertEquals(100L, playlist.getUserId());
        assertEquals("Workout Mix", playlist.getName());
        assertEquals("High energy songs for workout", playlist.getDescription());
        assertFalse(playlist.isPublic());
    }

    @Test
    void testPlaylistModelPublicVsPrivate() {
        Playlist publicPlaylist = createTestPlaylist(1L, 100L, "Public Playlist", "Everyone can see", true);
        Playlist privatePlaylist = createTestPlaylist(2L, 100L, "Private Playlist", "Only I can see", false);
        
        assertTrue(publicPlaylist.isPublic());
        assertFalse(privatePlaylist.isPublic());
        
        assertNotEquals(publicPlaylist.isPublic(), privatePlaylist.isPublic());
    }

    @Test
    void testPlaylistModelWithSpecialCharacters() {
        Playlist playlist = createTestPlaylist(1L, 100L, "🎵 Music Mix! @#$%", "Special chars: ñáéíóú 🎶", true);
        
        assertEquals("🎵 Music Mix! @#$%", playlist.getName());
        assertEquals("Special chars: ñáéíóú 🎶", playlist.getDescription());
        assertTrue(playlist.isPublic());
    }

    @Test
    void testPlaylistModelIdUpdates() {
        Playlist playlist = createTestPlaylist(0L, 100L, "New Playlist", "Not yet saved", false);
        
        assertEquals(0L, playlist.getPlaylistId()); // Not yet saved to database
        
        playlist.setPlaylistId(123L);
        assertEquals(123L, playlist.getPlaylistId()); // After saving
    }

    @Test
    void testPlaylistModelVisibilityToggle() {
        Playlist playlist = createTestPlaylist(1L, 100L, "Toggle Test", "Testing visibility", false);
        
        assertFalse(playlist.isPublic());
        
        playlist.setPublic(true);
        assertTrue(playlist.isPublic());
        
        playlist.setPublic(false);
        assertFalse(playlist.isPublic());
    }

    private Playlist createTestPlaylist(Long id, Long userId, String name, String description, boolean isPublic) {
        Playlist playlist = new Playlist();
        playlist.setPlaylistId(id);
        playlist.setUserId(userId);
        playlist.setName(name);
        playlist.setDescription(description);
        playlist.setPublic(isPublic);
        return playlist;
    }

    private Song createTestSong(Long id, String title, String artistName) {
        Song song = new Song();
        song.setSongId(id);
        song.setTitle(title);
        song.setArtistName(artistName);
        song.setGenre("Pop");
        song.setDurationSeconds(180);
        song.setPlayCount(0);
        return song;
    }
}
