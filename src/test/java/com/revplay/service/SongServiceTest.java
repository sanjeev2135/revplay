package com.revplay.service;

import com.revplay.model.Song;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class SongServiceTest {

    private SongService songService;

    @BeforeEach
    void setUp() {
        songService = new SongService();
    }

    @Test
    void testSongServiceCreation() {
        assertNotNull(songService);
    }

    @Test
    void testSongModelCreation() {
        Song song = new Song();
        
        song.setSongId(1L);
        song.setTitle("Test Song");
        song.setArtistName("Test Artist");
        song.setGenre("Pop");
        song.setDurationSeconds(180);
        song.setPlayCount(0);
        
        assertEquals(1L, song.getSongId());
        assertEquals("Test Song", song.getTitle());
        assertEquals("Test Artist", song.getArtistName());
        assertEquals("Pop", song.getGenre());
        assertEquals(180, song.getDurationSeconds());
        assertEquals(0, song.getPlayCount());
    }

    @Test
    void testSongModelWithAllFields() {
        Song song = new Song();
        
        song.setSongId(123L);
        song.setTitle("Beautiful Song");
        song.setArtistName("Famous Artist");
        song.setGenre("Rock");
        song.setDurationSeconds(240);
        song.setPlayCount(1000);
        song.setFilePath("/music/beautiful_song.mp3");
        
        assertEquals(123L, song.getSongId());
        assertEquals("Beautiful Song", song.getTitle());
        assertEquals("Famous Artist", song.getArtistName());
        assertEquals("Rock", song.getGenre());
        assertEquals(240, song.getDurationSeconds());
        assertEquals(1000, song.getPlayCount());
        assertEquals("/music/beautiful_song.mp3", song.getFilePath());
    }

    @Test
    void testSongModelWithDifferentGenres() {
        Song popSong = createTestSong(1L, "Pop Song", "Pop Artist", "Pop", 200);
        Song rockSong = createTestSong(2L, "Rock Song", "Rock Artist", "Rock", 180);
        Song jazzSong = createTestSong(3L, "Jazz Song", "Jazz Artist", "Jazz", 300);
        
        assertEquals("Pop", popSong.getGenre());
        assertEquals("Rock", rockSong.getGenre());
        assertEquals("Jazz", jazzSong.getGenre());
        
        assertNotEquals(popSong.getGenre(), rockSong.getGenre());
        assertNotEquals(rockSong.getGenre(), jazzSong.getGenre());
    }

    @Test
    void testSongModelWithEmptyStrings() {
        Song song = new Song();
        
        song.setSongId(1L);
        song.setTitle("");
        song.setArtistName("");
        song.setGenre("");
        
        assertEquals(1L, song.getSongId());
        assertEquals("", song.getTitle());
        assertEquals("", song.getArtistName());
        assertEquals("", song.getGenre());
    }

    @Test
    void testSongModelFilePath() {
        Song song = new Song();
        
        song.setFilePath("/path/to/song.mp3");
        assertEquals("/path/to/song.mp3", song.getFilePath());
        
        song.setFilePath("C:\\music\\song.wav");
        assertEquals("C:\\music\\song.wav", song.getFilePath());
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
