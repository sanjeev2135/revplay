package com.revplay.dao;

import com.revplay.model.Song;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class SongDaoTest {

    private SongDao songDao;

    @BeforeEach
    void setUp() {
        songDao = new SongDao();
    }

    @Test
    void testSongDaoCreation() {
        assertNotNull(songDao);
    }

    @Test
    void testSongModelValidation() {
        Song song = new Song();
        
        song.setSongId(1L);
        song.setTitle("Test Song");
        song.setArtistName("Test Artist");
        song.setGenre("Pop");
        song.setDurationSeconds(180);
        song.setPlayCount(0);
        song.setFilePath("/music/test.mp3");
        
        assertEquals(1L, song.getSongId());
        assertEquals("Test Song", song.getTitle());
        assertEquals("Test Artist", song.getArtistName());
        assertEquals("Pop", song.getGenre());
        assertEquals(180, song.getDurationSeconds());
        assertEquals(0, song.getPlayCount());
        assertEquals("/music/test.mp3", song.getFilePath());
    }

    @Test
    void testSongModelWithMinimalFields() {
        Song song = new Song();
        
        song.setSongId(1L);
        song.setTitle("Minimal Song");
        // Other fields remain null/default
        
        assertEquals(1L, song.getSongId());
        assertEquals("Minimal Song", song.getTitle());
        assertNull(song.getArtistName());
        assertNull(song.getGenre());
        assertEquals(0, song.getDurationSeconds()); // Default value
        assertEquals(0, song.getPlayCount()); // Default value
        assertNull(song.getFilePath());
    }

    @Test
    void testSongModelWithAllGenres() {
        String[] genres = {"Pop", "Rock", "Jazz", "Classical", "Hip Hop", "Electronic", "Indie"};
        
        for (int i = 0; i < genres.length; i++) {
            Song song = createTestSong((long)(i + 1), "Song " + (i + 1), "Artist " + (i + 1), genres[i], 180);
            assertEquals(genres[i], song.getGenre());
        }
    }

    @Test
    void testSongModelDurationValidation() {
        Song shortSong = createTestSong(1L, "Short", "Artist", "Pop", 30);
        Song mediumSong = createTestSong(2L, "Medium", "Artist", "Rock", 180);
        Song longSong = createTestSong(3L, "Long", "Artist", "Jazz", 600);
        
        assertEquals(30, shortSong.getDurationSeconds());
        assertEquals(180, mediumSong.getDurationSeconds());
        assertEquals(600, longSong.getDurationSeconds());
        
        assertTrue(shortSong.getDurationSeconds() < mediumSong.getDurationSeconds());
        assertTrue(mediumSong.getDurationSeconds() < longSong.getDurationSeconds());
    }

    @Test
    void testSongModelPlayCountTracking() {
        Song song = createTestSong(1L, "Popular Song", "Famous Artist", "Pop", 200);
        
        assertEquals(0, song.getPlayCount());
        
        song.setPlayCount(100);
        assertEquals(100, song.getPlayCount());
        
        song.setPlayCount(1000);
        assertEquals(1000, song.getPlayCount());
        
        song.setPlayCount(0); // Reset
        assertEquals(0, song.getPlayCount());
    }

    @Test
    void testSongModelFilePathHandling() {
        Song song1 = new Song();
        Song song2 = new Song();
        Song song3 = new Song();
        
        song1.setFilePath("/absolute/path/song.mp3");
        song2.setFilePath("relative/path/song.wav");
        song3.setFilePath("C:\\Windows\\Music\\song.flac");
        
        assertEquals("/absolute/path/song.mp3", song1.getFilePath());
        assertEquals("relative/path/song.wav", song2.getFilePath());
        assertEquals("C:\\Windows\\Music\\song.flac", song3.getFilePath());
    }

    @Test
    void testSongModelWithSpecialCharacters() {
        Song song = new Song();
        
        song.setSongId(1L);
        song.setTitle("🎵 Special Song! @#$%");
        song.setArtistName("Artist ñáéíóú");
        song.setGenre("Electronic/Dance");
        
        assertEquals("🎵 Special Song! @#$%", song.getTitle());
        assertEquals("Artist ñáéíóú", song.getArtistName());
        assertEquals("Electronic/Dance", song.getGenre());
    }

    @Test
    void testSongModelWithEmptyAndNullValues() {
        Song song1 = new Song();
        Song song2 = new Song();
        
        // Empty strings
        song1.setSongId(1L);
        song1.setTitle("");
        song1.setArtistName("");
        song1.setGenre("");
        song1.setFilePath("");
        
        // Null values
        song2.setSongId(2L);
        song2.setTitle(null);
        song2.setArtistName(null);
        song2.setGenre(null);
        song2.setFilePath(null);
        
        assertEquals("", song1.getTitle());
        assertEquals("", song1.getArtistName());
        assertEquals("", song1.getGenre());
        assertEquals("", song1.getFilePath());
        
        assertNull(song2.getTitle());
        assertNull(song2.getArtistName());
        assertNull(song2.getGenre());
        assertNull(song2.getFilePath());
    }

    @Test
    void testSongModelIdUpdates() {
        Song song = createTestSong(0L, "New Song", "New Artist", "Pop", 180);
        
        assertEquals(0L, song.getSongId()); // Not yet saved
        
        song.setSongId(123L);
        assertEquals(123L, song.getSongId()); // After database save
        
        song.setSongId(456L);
        assertEquals(456L, song.getSongId()); // Updated
    }

    @Test
    void testSongModelComparison() {
        Song song1 = createTestSong(1L, "First Song", "Artist A", "Pop", 180);
        Song song2 = createTestSong(2L, "Second Song", "Artist B", "Rock", 200);
        Song song3 = createTestSong(1L, "First Song", "Artist A", "Pop", 180);
        
        // Different songs
        assertNotEquals(song1.getSongId(), song2.getSongId());
        assertNotEquals(song1.getTitle(), song2.getTitle());
        assertNotEquals(song1.getArtistName(), song2.getArtistName());
        
        // Same content, different objects (but same ID)
        assertEquals(song1.getSongId(), song3.getSongId());
        assertEquals(song1.getTitle(), song3.getTitle());
        assertEquals(song1.getArtistName(), song3.getArtistName());
    }

    @Test
    void testSongModelToString() {
        Song song = createTestSong(1L, "Test Song", "Test Artist", "Pop", 180);
        String songString = song.toString();
        
        assertNotNull(songString);
        assertTrue(songString.contains("Test Song") || songString.contains("1"));
    }

    @Test
    void testSongModelWithLongFields() {
        String longTitle = "This is a very long song title that might exceed normal database field limits to test how the application handles such cases";
        String longArtist = "This is a very long artist name that includes multiple words and might be longer than typical artist names in the database";
        String longPath = "/very/long/path/to/music/files/that/goes/many/directories/deep/into/the/filesystem/structure/and/might/cause/issues/with/some/systems/song.mp3";
        
        Song song = new Song();
        song.setSongId(1L);
        song.setTitle(longTitle);
        song.setArtistName(longArtist);
        song.setFilePath(longPath);
        
        assertEquals(longTitle, song.getTitle());
        assertEquals(longArtist, song.getArtistName());
        assertEquals(longPath, song.getFilePath());
    }

    @Test
    void testSongModelWithNumericEdgeCases() {
        Song song1 = new Song();
        Song song2 = new Song();
        Song song3 = new Song();
        
        // Edge case durations
        song1.setDurationSeconds(0); // No duration
        song2.setDurationSeconds(1); // Minimum duration
        song3.setDurationSeconds(7200); // 2 hours - very long song
        
        assertEquals(0, song1.getDurationSeconds());
        assertEquals(1, song2.getDurationSeconds());
        assertEquals(7200, song3.getDurationSeconds());
        
        // Edge case play counts
        song1.setPlayCount(-1); // Invalid but test handling
        song2.setPlayCount(0); // No plays
        song3.setPlayCount(Integer.MAX_VALUE); // Maximum plays
        
        assertEquals(-1, song1.getPlayCount());
        assertEquals(0, song2.getPlayCount());
        assertEquals(Integer.MAX_VALUE, song3.getPlayCount());
    }

    private Song createTestSong(Long id, String title, String artistName, String genre, int duration) {
        Song song = new Song();
        song.setSongId(id);
        song.setTitle(title);
        song.setArtistName(artistName);
        song.setGenre(genre);
        song.setDurationSeconds(duration);
        song.setPlayCount(0);
        return song;
    }
}
