package com.revplay.service;

import com.revplay.dao.SongDao;
import com.revplay.model.Song;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class MusicPlayerService {
    private static final Logger logger = LogManager.getLogger(MusicPlayerService.class);
    private final SongDao songDao = new SongDao();
    private final ListeningHistoryService historyService = new ListeningHistoryService();
    private final FavoriteService favoriteService = new FavoriteService();
    
    private Song currentSong;
    private boolean isPlaying;
    private List<Song> currentPlaylist;
    private int currentSongIndex;
    private long currentUserId;
    private boolean isRepeat;

    public void playSong(long songId, long userId) {
        try {
            // Stop any currently playing song
            stop();
            
            this.currentUserId = userId;
            currentSong = songDao.getSongById(songId);
            if (currentSong != null) {
                isPlaying = true;
                songDao.incrementPlayCount(songId);
                historyService.simulateSongPlay(userId, currentSong);
            }
        } catch (Exception e) {
            logger.error("Failed to play song {}: {}", songId, e.getMessage());
        }
    }
    
    public void playPlaylist(List<Song> playlist, int startIndex, long userId) {
        try {
            this.currentPlaylist = playlist;
            this.currentSongIndex = startIndex;
            this.currentUserId = userId;
            
            if (startIndex < playlist.size()) {
                Song song = playlist.get(startIndex);
                playSong(song.getSongId(), userId);
                logger.info("Playing playlist: {} (song {}/{})", 
                    "Custom Playlist", startIndex + 1, playlist.size());
            }
        } catch (Exception e) {
            logger.error("Failed to play playlist: {}", e.getMessage());
        }
    }

    public void pause() {
        isPlaying = false;
        logger.info("⏸️ Paused: {}", currentSong != null ? currentSong.getTitle() : "No song");
    }

    public void resume() {
        if (currentSong != null) {
            isPlaying = true;
            logger.info("▶️ Resumed: {}", currentSong.getTitle());
        }
    }

    public void skip() {
        if (currentPlaylist != null && !currentPlaylist.isEmpty()) {
            currentSongIndex++;
            if (currentSongIndex >= currentPlaylist.size()) {
                if (isRepeat) {
                    currentSongIndex = 0;
                } else {
                    stop();
                    return;
                }
            }
            Song nextSong = currentPlaylist.get(currentSongIndex);
            playSong(nextSong.getSongId(), currentUserId);
            logger.info("⏭️ Skipped to: {}", nextSong.getTitle());
        } else if (currentSong != null) {
            // Single song playing - restart from beginning
            logger.info("⏭️ Restarting current song: {}", currentSong.getTitle());
            playSong(currentSong.getSongId(), currentUserId);
        } else {
            logger.info("⏭️ No song playing - cannot skip");
        }
    }

    public void previous() {
        if (currentPlaylist != null && !currentPlaylist.isEmpty()) {
            currentSongIndex--;
            if (currentSongIndex < 0) {
                currentSongIndex = currentPlaylist.size() - 1;
            }
            Song prevSong = currentPlaylist.get(currentSongIndex);
            playSong(prevSong.getSongId(), currentUserId);
            logger.info("⏮️ Previous: {}", prevSong.getTitle());
        } else if (currentSong != null) {
            // Single song playing - restart from beginning
            logger.info("⏮️ Restarting current song: {}", currentSong.getTitle());
            playSong(currentSong.getSongId(), currentUserId);
        } else {
            logger.info("⏮️ No song playing - cannot go to previous");
        }
    }

    public void stop() {
        isPlaying = false;
        currentSong = null;
        currentPlaylist = null;
        currentSongIndex = 0;
        logger.info("⏹️ Stopped");
    }
    
    public void clearCurrentSong() {
        currentSong = null;
        currentPlaylist = null;
        currentSongIndex = 0;
        logger.info("🗑️ Cleared current song state");
    }

    public void toggleRepeat() {
        isRepeat = !isRepeat;
        logger.info("🔁 Repeat {}", isRepeat ? "ON" : "OFF");
    }

    public void toggleFavorite() {
        if (currentSong != null && currentUserId > 0) {
            favoriteService.toggleFavorite(currentUserId, currentSong.getSongId());
            boolean isFavorite = favoriteService.isFavorite(currentUserId, currentSong.getSongId());
            logger.info("❤️ Song '{}' {}", currentSong.getTitle(), isFavorite ? "added to favorites" : "removed from favorites");
        }
    }

    public void shuffle() {
        if (currentPlaylist != null && !currentPlaylist.isEmpty()) {
            // Simple shuffle implementation
            for (int i = currentPlaylist.size() - 1; i > 0; i--) {
                int index = (int) (Math.random() * (i + 1));
                Song temp = currentPlaylist.get(index);
                currentPlaylist.set(index, currentPlaylist.get(i));
                currentPlaylist.set(i, temp);
            }
            currentSongIndex = 0;
            if (!currentPlaylist.isEmpty()) {
                Song firstSong = currentPlaylist.get(0);
                playSong(firstSong.getSongId(), currentUserId);
            }
            logger.info("🔀 Playlist shuffled");
        }
    }

    public String getCurrentSongInfo() {
        if (currentSong == null) {
            return "⏹️ No song playing";
        }
        String status = isPlaying ? "▶️" : "⏸️";
        String favorite = "";
        if (currentUserId > 0 && favoriteService.isFavorite(currentUserId, currentSong.getSongId())) {
            favorite = " ❤️";
        }
        return String.format("%s %s by %s%s [%d:%02d]", 
            status, currentSong.getTitle(), currentSong.getArtistName(), favorite,
            currentSong.getDurationSeconds() / 60, currentSong.getDurationSeconds() % 60);
    }

    public Song getCurrentSong() {
        return currentSong;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public boolean isRepeat() {
        return isRepeat;
    }

    public List<Song> getCurrentPlaylist() {
        return currentPlaylist;
    }

    public int getCurrentSongIndex() {
        return currentSongIndex;
    }

    public String getPlaybackStatus() {
        if (!isPlaying || currentSong == null) {
            return "Stopped";
        }
        
        StringBuilder status = new StringBuilder();
        status.append("Now Playing: ").append(currentSong.getTitle());
        status.append(" by ").append(currentSong.getArtistName());
        
        if (currentPlaylist != null) {
            status.append(" (").append(currentSongIndex + 1).append("/");
            status.append(currentPlaylist.size()).append(")");
        }
        
        if (isRepeat) {
            status.append(" [🔁 Repeat]");
        }
        
        return status.toString();
    }
}
