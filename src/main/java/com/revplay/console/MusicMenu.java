package com.revplay.console;

import com.revplay.model.Artist;
import com.revplay.model.Song;
import com.revplay.service.ArtistService;
import com.revplay.service.MusicPlayerService;
import com.revplay.service.SongService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Scanner;

public class MusicMenu {
    private static final Logger logger = LogManager.getLogger(MusicMenu.class);
    private static final MusicPlayerService player = new MusicPlayerService();
    private static final SongService songService = new SongService();
    private static final ArtistService artistService = new ArtistService();
    private static final Scanner scanner = new Scanner(System.in);
    private static long currentUserId = 0;

    public static void show(long userId) {
        currentUserId = userId;
        int choice = 0;
        do {
            System.out.println("\n=== MUSIC PLAYER ===");
            
            if (player.getCurrentSong() == null) {
                // No song playing - show only search options
                System.out.println("1. Search & Play Song");
                System.out.println("2. List Songs by Artist");
                System.out.println("3. Back");
            } else {
                // Song is playing - show only playback controls
                System.out.println("🎵 Now Playing: " + player.getCurrentSong().getTitle());
                System.out.println("1. Pause");
                System.out.println("2. Resume");
                System.out.println("3. Skip");
                System.out.println("4. Previous");
                System.out.println("5. Stop");
                System.out.println("6. Toggle Repeat");
                System.out.println("7. Toggle Favorite");
                System.out.println("8. Current Song Info");
                System.out.println("9. Back");
            }
            System.out.print("Choose: ");

            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("❌ Invalid input! Please enter a number.");
                scanner.nextLine(); // Clear invalid input
                continue;
            }

            if (player.getCurrentSong() == null) {
                // No song playing menu
                switch (choice) {
                    case 1 -> searchAndPlaySong();
                    case 2 -> listSongsByArtist();
                    case 3 -> System.out.println("← Back to user menu");
                    default -> System.out.println("❌ Invalid choice!");
                }
            } else {
                // Song playing menu
                switch (choice) {
                    case 1 -> player.pause();
                    case 2 -> player.resume();
                    case 3 -> player.skip();
                    case 4 -> player.previous();
                    case 5 -> player.requestStop();
                    case 6 -> player.toggleRepeat();
                    case 7 -> player.toggleFavorite();
                    case 8 -> System.out.println(player.getCurrentSongInfo());
                    case 9 -> {
                    System.out.println("← Back to user menu");
                    player.stop(); // Clear current song
                }
                    default -> System.out.println("❌ Invalid choice!");
                }
            }
        } while (choice != 3 && (player.getCurrentSong() != null ? choice != 9 : choice != 3));
    }
    
    private static void searchAndPlaySong() {
        try {
            System.out.print("Enter song name to search: ");
            String keyword = scanner.nextLine();
            
            List<Song> songs = songService.searchSongs(keyword);
            if (songs.isEmpty()) {
                System.out.println("No songs found for '" + keyword + "'");
                return;
            }
            
            System.out.println("\n=== SEARCH RESULTS ===");
            for (int i = 0; i < songs.size(); i++) {
                Song song = songs.get(i);
                System.out.printf("%d. %s by %s [%d:%02d]%n", 
                    i + 1, song.getTitle(), song.getArtistName(),
                    song.getDurationSeconds() / 60, song.getDurationSeconds() % 60);
            }
            
            System.out.print("\nEnter song number to play (0 to cancel): ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            if (choice > 0 && choice <= songs.size()) {
                Song selectedSong = songs.get(choice - 1);
                player.playSong(selectedSong.getSongId(), currentUserId);
                simulateMusicPlayer();
            }
        } catch (Exception e) {
            logger.error("Failed to search/play song: {}", e.getMessage());
        }
    }
    
    private static void listSongsByArtist() {
        try {
            System.out.print("Enter artist name to search: ");
            String keyword = scanner.nextLine();
            
            List<Artist> artists = artistService.searchArtistsByName(keyword);
            if (artists.isEmpty()) {
                System.out.println("No artists found for '" + keyword + "'");
                return;
            }
            
            System.out.println("\n=== ARTISTS ===");
            for (int i = 0; i < artists.size(); i++) {
                Artist artist = artists.get(i);
                System.out.printf("%d. %s (Genre: %s)%n", 
                    i + 1, artist.getStageName(), artist.getGenre());
            }
            
            System.out.print("\nEnter artist number to view songs (0 to cancel): ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            if (choice > 0 && choice <= artists.size()) {
                Artist selectedArtist = artists.get(choice - 1);
                List<Song> songs = songService.getSongsByArtist(selectedArtist.getArtistId());
                
                if (songs.isEmpty()) {
                    System.out.println("No songs found for " + selectedArtist.getStageName());
                    return;
                }
                
                System.out.println("\n=== SONGS BY " + selectedArtist.getStageName().toUpperCase() + " ===");
                for (int i = 0; i < songs.size(); i++) {
                    Song song = songs.get(i);
                    System.out.printf("%d. %s [%d:%02d]%n", 
                        i + 1, song.getTitle(),
                        song.getDurationSeconds() / 60, song.getDurationSeconds() % 60);
                }
                
                System.out.print("\nEnter song number to play (0 to cancel): ");
                int songChoice = scanner.nextInt();
                scanner.nextLine();
                
                if (songChoice > 0 && songChoice <= songs.size()) {
                    Song selectedSong = songs.get(songChoice - 1);
                    player.playSong(selectedSong.getSongId(), currentUserId);
                    simulateMusicPlayer();
                }
            }
        } catch (Exception e) {
            logger.error("Failed to list songs by artist: {}", e.getMessage());
        }
    }
    
    private static void simulateMusicPlayer() {
        if (player.getCurrentSong() == null) {
            System.out.println("❌ No song is currently playing!");
            return;
        }

        Song currentSong = player.getCurrentSong();
        int totalSeconds = currentSong.getDurationSeconds();
        int currentSeconds = 0;
        boolean isPaused = false;
        boolean repeat = player.isRepeat();
        
        System.out.println("\n🎵 NOW PLAYING: " + currentSong.getTitle());
        System.out.println("   Artist: " + currentSong.getArtistName());
        System.out.println("   Duration: " + formatTime(totalSeconds));
        System.out.println("\n📋 AVAILABLE CONTROLS:");
        System.out.println("   [P] Pause  [S] Skip  [R] Repeat  [F] Favorite  [Q] Stop");
        System.out.println("\n" + generateProgressBar(currentSeconds, totalSeconds));
        
        while (player.isPlaying() && player.getCurrentSong() != null) {
            try {
                System.out.print("\r" + generateProgressBar(currentSeconds, totalSeconds) + " " + formatTime(currentSeconds) + " / " + formatTime(totalSeconds));
                
                if (System.in.available() > 0) {
                    String input = scanner.nextLine().trim().toLowerCase();
                    
                    if (input.equals("p") || input.equals("pause")) {
                        player.pause();
                        isPaused = true;
                        System.out.println("\n⏸️ PAUSED: " + currentSong.getTitle());
                        System.out.println("\n📋 CONTROLS:");
                        System.out.println("   [P] Resume  [S] Skip  [R] Repeat  [F] Favorite  [Q] Stop");
                    } else if (input.equals("r") || input.equals("resume")) {
                        if (isPaused) {
                            player.resume();
                            isPaused = false;
                            System.out.println("\n▶️ RESUMED: " + currentSong.getTitle());
                        }
                    } else if (input.equals("s") || input.equals("skip")) {
                        System.out.println("\n⏭️ SKIPPED to next song");
                        player.skip();
                        return;
                    } else if (input.equals("f") || input.equals("favorite")) {
                        player.toggleFavorite();
                        System.out.println("\n❤️ TOGGLED FAVORITE for: " + currentSong.getTitle());
                    } else if (input.equals("repeat")) {
                        player.toggleRepeat();
                        repeat = !repeat;
                        System.out.println("\n🔁 REPEAT " + (repeat ? "ON" : "OFF"));
                    } else if (input.equals("q") || input.equals("stop") || input.equals("quit")) {
                        System.out.println("\n⏹️ STOPPED: " + currentSong.getTitle());
                        player.requestStop();
                        return;
                    }
                    
                    // Redraw header after any command
                    System.out.println("\n🎵 NOW PLAYING: " + currentSong.getTitle());
                    System.out.println("   Artist: " + currentSong.getArtistName());
                    System.out.println("   Duration: " + formatTime(totalSeconds));
                    System.out.println("\n📋 CONTROLS:");
                    System.out.println("   [P] Pause  [S] Skip  [R] Repeat  [F] Favorite  [Q] Stop");
                    System.out.println("\n" + generateProgressBar(currentSeconds, totalSeconds));
                }
                
                if (!isPaused) {
                    currentSeconds++;
                    if (currentSeconds >= totalSeconds) {
                        System.out.println("\n🎵 SONG FINISHED: " + currentSong.getTitle());
                        player.requestStop();
                        return;
                    }
                }
                
                Thread.sleep(1000);
            } catch (Exception e) {
                break;
            }
        }
    }
    
    private static String generateProgressBar(int current, int total) {
        int percentage = (int) ((double) current / total * 100);
        int filled = (int) ((double) current / total * 20);
        StringBuilder bar = new StringBuilder();
        
        for (int i = 0; i < 20; i++) {
            if (i < filled) {
                bar.append("█");
            } else {
                bar.append("░");
            }
        }
        
        return bar + " " + percentage + "%";
    }
    
    private static String formatTime(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return String.format("%d:%02d", minutes, secs);
    }
}
