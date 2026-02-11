package com.revplay.console;

import com.revplay.model.Playlist;
import com.revplay.model.Song;
import com.revplay.service.PlaylistService;
import com.revplay.service.SongService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Scanner;

public class PlaylistMenu {
    private static final Logger logger = LogManager.getLogger(PlaylistMenu.class);
    private static final PlaylistService playlistService = new PlaylistService();
    private static final SongService songService = new SongService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void show(long userId) {
        int choice;

        do {
            System.out.println("\n=== PLAYLIST MENU ===");
            System.out.println("1. Create Playlist");
            System.out.println("2. View My Playlists");
            System.out.println("3. Add Song to Playlist");
            System.out.println("4. Remove Song from Playlist");
            System.out.println("5. Update Playlist");
            System.out.println("6. Delete Playlist");
            System.out.println("7. View Public Playlists");
            System.out.println("8. Back");
            System.out.print("Choose: ");

            while (!scanner.hasNextInt()) {
                System.out.println("❌ Enter a number!");
                scanner.nextLine();
            }
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> createPlaylist(userId);
                case 2 -> viewMyPlaylists(userId);
                case 3 -> addSongToPlaylist(userId);
                case 4 -> removeSongFromPlaylist(userId);
                case 5 -> updatePlaylist(userId);
                case 6 -> deletePlaylist(userId);
                case 7 -> viewPublicPlaylists();
                case 8 -> logger.info("← Back to user menu");
                default -> logger.warn("❌ Invalid choice 1-8!");
            }
        } while (choice != 8);
    }

    private static void createPlaylist(long userId) {
        System.out.print("Playlist name: ");
        String name = scanner.nextLine();

        System.out.print("Description: ");
        String desc = scanner.nextLine();

        System.out.print("Privacy (PUBLIC/PRIVATE): ");
        String privacy = scanner.nextLine().toUpperCase();

        Playlist playlist = new Playlist(userId, name, desc, privacy);
        if (playlistService.createPlaylist(playlist)) {
            logger.info("✅ Playlist '{}' created!", name);
        } else {
            logger.error("❌ Failed to create playlist");
        }
    }

    private static void viewMyPlaylists(long userId) {
        try {
            var playlists = playlistService.getUserPlaylists(userId);
            if (playlists.isEmpty()) {
                logger.info("You have no playlists yet.");
                return;
            }

            System.out.println("\n=== YOUR PLAYLISTS ===");
            for (Playlist playlist : playlists) {
                System.out.printf("%d. %s (%s) - %s%n", 
                    playlist.getPlaylistId(), 
                    playlist.getName(), 
                    playlist.isPublic() ? "Public" : "Private",
                    playlist.getDescription());
                
                // Show songs in playlist
                var songs = playlistService.getPlaylistSongs(playlist.getPlaylistId());
                if (!songs.isEmpty()) {
                    System.out.println("   Songs:");
                    for (var song : songs) {
                        System.out.printf("   - %s by %s%n", song.getTitle(), song.getArtistName());
                    }
                }
                System.out.println();
            }
        } catch (Exception e) {
            logger.error("Failed to view playlists: {}", e.getMessage());
        }
    }

    private static void addSongToPlaylist(long currentUserId) {
        try {
            viewMyPlaylists(currentUserId);
            
            System.out.print("Enter Playlist ID (the number at the start): ");
            long playlistId = scanner.nextLong();
            scanner.nextLine();

            // Show available songs
            System.out.println("\n=== AVAILABLE SONGS ===");
            List<Song> songs = songService.getAllSongs();
            if (songs.isEmpty()) {
                System.out.println("No songs available. Upload songs first!");
                return;
            }
            
            for (Song song : songs) {
                System.out.printf("%d. %s by %s (%s, %d sec)%n", 
                    song.getSongId(), 
                    song.getTitle(), 
                    song.getArtistName(),
                    song.getGenre(),
                    song.getDurationSeconds());
            }

            System.out.print("\nEnter Song ID to add (the number at the start): ");
            long songId = scanner.nextLong();
            scanner.nextLine();

            if (playlistService.addSongToPlaylist(playlistId, songId)) {
                logger.info("✅ Song {} added to playlist {}", songId, playlistId);
            } else {
                logger.error("❌ Failed to add song (playlist or song may not exist)");
            }
        } catch (Exception e) {
            logger.error("Failed to add song to playlist: {}", e.getMessage());
        }
    }

    private static void removeSongFromPlaylist(long currentUserId) {
        try {
            viewMyPlaylists(currentUserId);
            
            System.out.print("Playlist ID: ");
            long playlistId = scanner.nextLong();
            scanner.nextLine();

            System.out.print("Song ID: ");
            long songId = scanner.nextLong();
            scanner.nextLine();

            if (playlistService.removeSongFromPlaylist(playlistId, songId)) {
                logger.info("✅ Song {} removed from playlist {}", songId, playlistId);
            } else {
                logger.error("❌ Failed to remove song");
            }
        } catch (Exception e) {
            logger.error("Failed to remove song from playlist: {}", e.getMessage());
        }
    }

    private static void updatePlaylist(long userId) {
        try {
            viewMyPlaylists(userId);
            
            System.out.print("Playlist ID: ");
            long playlistId = scanner.nextLong();
            scanner.nextLine();

            System.out.print("New name (or press Enter to keep current): ");
            String name = scanner.nextLine();

            System.out.print("New description (or press Enter to keep current): ");
            String description = scanner.nextLine();

            System.out.print("Privacy (PUBLIC/PRIVATE, or press Enter to keep current): ");
            String privacy = scanner.nextLine();

            Playlist playlist = new Playlist();
            playlist.setPlaylistId(playlistId);
            if (!name.isEmpty()) playlist.setName(name);
            if (!description.isEmpty()) playlist.setDescription(description);
            if (!privacy.isEmpty()) playlist.setPublic("PUBLIC".equals(privacy.toUpperCase()));

            if (playlistService.updatePlaylist(playlist)) {
                logger.info("✅ Playlist {} updated!", playlistId);
            } else {
                logger.error("❌ Failed to update playlist");
            }
        } catch (Exception e) {
            logger.error("Failed to update playlist: {}", e.getMessage());
        }
    }

    private static void deletePlaylist(long userId) {
        try {
            viewMyPlaylists(userId);
            
            System.out.print("Playlist ID to delete: ");
            long playlistId = scanner.nextLong();
            scanner.nextLine();

            System.out.print("Are you sure? (y/N): ");
            String confirm = scanner.nextLine();

            if ("y".equalsIgnoreCase(confirm)) {
                if (playlistService.deletePlaylist(playlistId, userId)) {
                    logger.info("✅ Playlist {} deleted!", playlistId);
                } else {
                    logger.error("❌ Failed to delete playlist");
                }
            } else {
                logger.info("Deletion cancelled");
            }
        } catch (Exception e) {
            logger.error("Failed to delete playlist: {}", e.getMessage());
        }
    }

    private static void viewPublicPlaylists() {
        try {
            var playlists = playlistService.getPublicPlaylists();
            if (playlists.isEmpty()) {
                logger.info("No public playlists available.");
                return;
            }

            System.out.println("\n=== PUBLIC PLAYLISTS ===");
            for (Playlist playlist : playlists) {
                System.out.printf("%d. %s - %s%n", 
                    playlist.getPlaylistId(), 
                    playlist.getName(), 
                    playlist.getDescription());
                
                // Show songs in playlist
                var songs = playlistService.getPlaylistSongs(playlist.getPlaylistId());
                if (!songs.isEmpty()) {
                    System.out.println("   Songs:");
                    for (var song : songs) {
                        System.out.printf("   - %s by %s%n", song.getTitle(), song.getArtistName());
                    }
                }
                System.out.println();
            }
        } catch (Exception e) {
            logger.error("Failed to view public playlists: {}", e.getMessage());
        }
    }
}
