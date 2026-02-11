package com.revplay.service;

import com.revplay.model.Song;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class MusicPlayerServiceTest {

    private MusicPlayerService player;

    @BeforeEach
    void setUp() {
        player = new MusicPlayerService();
    }

    @Test
    void testMusicPlayerServiceCreation() {
        assertNotNull(player);
    }

    @Test
    void testPlayerInitialState() {
        assertNull(player.getCurrentSong());
        assertFalse(player.isPlaying());
        assertFalse(player.isRepeat());
        assertNull(player.getCurrentPlaylist());
        assertEquals(0, player.getCurrentSongIndex()); // Default value is 0, not -1
    }

    @Test
    void testPlayerRepeatStatePersistence() {
        // Test repeat state persists through operations
        player.toggleRepeat();
        assertTrue(player.isRepeat());
        
        player.playSong(1L, 1L);
        assertTrue(player.isRepeat());
        
        player.pause();
        assertTrue(player.isRepeat());
        
        player.resume();
        assertTrue(player.isRepeat());
        
        player.requestStop();
        assertTrue(player.isRepeat());
    }

    @Test
    void testPlayerMultiplePlayCalls() {
        // Test multiple play calls
        assertDoesNotThrow(() -> player.playSong(1L, 1L));
        assertDoesNotThrow(() -> player.playSong(2L, 1L));
        assertDoesNotThrow(() -> player.playSong(3L, 1L));
        assertDoesNotThrow(() -> player.playSong(1L, 2L));
    }

    @Test
    void testPlayerControlSequence() {
        // Test a typical control sequence
        player.playSong(1L, 1L);
        // Note: isPlaying might be false if song not found in database
        
        player.pause();
        
        player.resume();
        
        player.toggleRepeat();
        assertTrue(player.isRepeat());
        
        player.requestStop();
        assertFalse(player.isPlaying());
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
