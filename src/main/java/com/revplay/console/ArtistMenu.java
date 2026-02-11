package com.revplay.console;

import com.revplay.model.Album;
import com.revplay.model.Artist;
import com.revplay.model.Song;
import com.revplay.service.ArtistService;
import com.revplay.service.FavoriteService;
import com.revplay.service.SongService;
import com.revplay.service.StatsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Scanner;

public class ArtistMenu {
    private static final Logger logger = LogManager.getLogger(ArtistMenu.class);
    private static final ArtistService artistService = new ArtistService();
    private static final SongService songService = new SongService();
    private static final StatsService statsService = new StatsService();
    private static final FavoriteService favoriteService = new FavoriteService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void show(long userId) {
        int choice;
        do {
            System.out.println("\n=== ARTIST DASHBOARD ===");
            System.out.println("1. View Profile");
            System.out.println("2. Edit Profile");
            System.out.println("3. Song Stats");
            System.out.println("4. Upload New Song");
            System.out.println("5. View My Songs");
            System.out.println("6. View My Albums");
            System.out.println("7. Create Album");
            System.out.println("8. View Song Favorites");
            System.out.println("9. Back");
            System.out.print("Choose: ");

            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> viewProfile(userId);
                case 2 -> editProfile(userId);
                case 3 -> viewStats(userId);
                case 4 -> uploadSong(userId);
                case 5 -> viewMySongs(userId);
                case 6 -> viewMyAlbums(userId);
                case 7 -> createAlbum(userId);
                case 8 -> viewSongFavorites(userId);
                case 9 -> System.out.println("← Back to user menu");
                default -> System.out.println("❌ Invalid choice!");
            }
        } while (choice != 9);
    }

    private static void viewProfile(long userId) {
        Artist artist = artistService.getArtistProfile(userId);
        if (artist != null) {
            System.out.println("\n=== ARTIST PROFILE ===");
            System.out.println("Bio: " + artist.getBio());
            System.out.println("Genre: " + artist.getGenre());
            System.out.println("Social Links: " + artist.getSocialLinks());
        } else {
            System.out.println("\n❌ No artist profile found!");
        }
    }

    private static void editProfile(long userId) {
        Artist artist = artistService.getArtistProfile(userId);
        if (artist == null) {
            System.out.println("❌ No artist profile found!");
            return;
        }

        System.out.println("\n=== EDIT PROFILE ===");
        System.out.println("Current Bio: " + artist.getBio());
        System.out.print("New Bio (press Enter to keep current): ");
        String bio = scanner.nextLine();
        if (!bio.isEmpty()) {
            artist.setBio(bio);
        }

        System.out.println("Current Genre: " + artist.getGenre());
        System.out.print("New Genre (press Enter to keep current): ");
        String genre = scanner.nextLine();
        if (!genre.isEmpty()) {
            artist.setGenre(genre);
        }

        System.out.println("Current Social Links: " + artist.getSocialLinks());
        System.out.print("New Social Links (press Enter to keep current): ");
        String socialLinks = scanner.nextLine();
        if (!socialLinks.isEmpty()) {
            artist.setSocialLinks(socialLinks);
        }

        artist.setUserId(userId);
        if (artistService.updateArtistProfile(artist)) {
            System.out.println("✅ Profile updated successfully!");
        } else {
            System.out.println("❌ Failed to update profile.");
        }
    }

    private static void viewStats(long userId) {
        long artistId = artistService.getArtistIdByUserId(userId);
        int songCount = statsService.getUserSongCount(artistId);
        System.out.println("\n=== SONG STATS ===");
        System.out.println("Total songs uploaded: " + songCount);
    }

    private static void uploadSong(long userId) {
        long artistId = artistService.getArtistIdByUserId(userId);
        if (artistId == -1) {
            System.out.println("❌ Artist profile not found!");
            return;
        }

        System.out.print("Song title: ");
        String title = scanner.nextLine();

        System.out.print("Genre: ");
        String genre = scanner.nextLine();

        System.out.print("Duration (seconds): ");
        int duration = scanner.nextInt();
        scanner.nextLine();

        if (songService.uploadSong(artistId, title, genre, duration)) {
            System.out.println("\n✅ SUCCESS! '" + title + "' uploaded!");
        } else {
            System.out.println("\n❌ Upload failed.");
        }
    }

    private static void viewMySongs(long userId) {
        long artistId = artistService.getArtistIdByUserId(userId);
        if (artistId == -1) {
            System.out.println("❌ Artist profile not found!");
            return;
        }

        List<Song> songs = songService.getSongsByArtist(artistId);
        if (songs.isEmpty()) {
            System.out.println("\n🎵 No songs uploaded yet.");
            return;
        }

        System.out.println("\n=== MY SONGS ===");
        for (int i = 0; i < songs.size(); i++) {
            Song song = songs.get(i);
            System.out.printf("%d. %s [%d:%02d] - %s - Plays: %d%n",
                i + 1, song.getTitle(),
                song.getDurationSeconds() / 60, song.getDurationSeconds() % 60,
                song.getGenre(), song.getPlayCount());
        }

        System.out.println("\nOptions:");
        System.out.println("1. Edit Song");
        System.out.println("2. Delete Song");
        System.out.println("3. Back");
        System.out.print("Choose: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> editSong(songs);
            case 2 -> deleteSong(songs);
        }
    }

    private static void editSong(List<Song> songs) {
        System.out.print("Enter song number to edit: ");
        int songNum = scanner.nextInt();
        scanner.nextLine();

        if (songNum < 1 || songNum > songs.size()) {
            System.out.println("❌ Invalid song number.");
            return;
        }

        Song song = songs.get(songNum - 1);
        System.out.println("Editing: " + song.getTitle());
        System.out.print("New title (press Enter to keep '" + song.getTitle() + "'): ");
        String title = scanner.nextLine();
        if (title.isEmpty()) title = song.getTitle();

        System.out.print("New genre (press Enter to keep '" + song.getGenre() + "'): ");
        String genre = scanner.nextLine();
        if (genre.isEmpty()) genre = song.getGenre();

        System.out.print("New duration in seconds (0 to keep " + song.getDurationSeconds() + "): ");
        int duration = scanner.nextInt();
        scanner.nextLine();
        if (duration == 0) duration = song.getDurationSeconds();

        if (songService.updateSong(song.getSongId(), title, genre, duration)) {
            System.out.println("✅ Song updated successfully!");
        } else {
            System.out.println("❌ Failed to update song.");
        }
    }

    private static void deleteSong(List<Song> songs) {
        System.out.print("Enter song number to delete: ");
        int songNum = scanner.nextInt();
        scanner.nextLine();

        if (songNum < 1 || songNum > songs.size()) {
            System.out.println("❌ Invalid song number.");
            return;
        }

        Song song = songs.get(songNum - 1);
        System.out.print("Are you sure you want to delete '" + song.getTitle() + "'? (yes/no): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("yes")) {
            if (songService.deleteSong(song.getSongId())) {
                System.out.println("✅ Song deleted successfully!");
            } else {
                System.out.println("❌ Failed to delete song.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private static void viewMyAlbums(long userId) {
        long artistId = artistService.getArtistIdByUserId(userId);
        if (artistId == -1) {
            System.out.println("❌ Artist profile not found!");
            return;
        }

        List<Album> albums = artistService.getMyAlbums(artistId);
        if (albums.isEmpty()) {
            System.out.println("\n💿 No albums created yet.");
            return;
        }

        System.out.println("\n=== MY ALBUMS ===");
        for (int i = 0; i < albums.size(); i++) {
            Album album = albums.get(i);
            System.out.printf("%d. %s - %s%n",
                i + 1, album.getTitle(),
                album.getDescription() != null ? album.getDescription() : "No description");
        }

        System.out.println("\nOptions:");
        System.out.println("1. Edit Album");
        System.out.println("2. Delete Album");
        System.out.println("3. Back");
        System.out.print("Choose: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> editAlbum(albums, artistId);
            case 2 -> deleteAlbum(albums, artistId);
        }
    }

    private static void editAlbum(List<Album> albums, long artistId) {
        System.out.print("Enter album number to edit: ");
        int albumNum = scanner.nextInt();
        scanner.nextLine();

        if (albumNum < 1 || albumNum > albums.size()) {
            System.out.println("❌ Invalid album number.");
            return;
        }

        Album album = albums.get(albumNum - 1);
        System.out.println("Editing: " + album.getTitle());
        System.out.print("New title (press Enter to keep '" + album.getTitle() + "'): ");
        String title = scanner.nextLine();
        if (title.isEmpty()) title = album.getTitle();

        System.out.print("New description (press Enter to keep current): ");
        String description = scanner.nextLine();
        if (description.isEmpty()) description = album.getDescription();

        album.setTitle(title);
        album.setDescription(description);

        if (artistService.updateAlbum(album)) {
            System.out.println("✅ Album updated successfully!");
        } else {
            System.out.println("❌ Failed to update album.");
        }
    }

    private static void deleteAlbum(List<Album> albums, long artistId) {
        System.out.print("Enter album number to delete: ");
        int albumNum = scanner.nextInt();
        scanner.nextLine();

        if (albumNum < 1 || albumNum > albums.size()) {
            System.out.println("❌ Invalid album number.");
            return;
        }

        Album album = albums.get(albumNum - 1);
        System.out.print("Are you sure you want to delete '" + album.getTitle() + "'? (yes/no): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("yes")) {
            if (artistService.deleteAlbum(album.getAlbumId(), artistId)) {
                System.out.println("✅ Album deleted successfully!");
            } else {
                System.out.println("❌ Failed to delete album.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private static void createAlbum(long userId) {
        long artistId = artistService.getArtistIdByUserId(userId);
        if (artistId == -1) {
            System.out.println("❌ Artist profile not found!");
            return;
        }

        System.out.print("Album title: ");
        String title = scanner.nextLine();

        System.out.print("Album description: ");
        String description = scanner.nextLine();

        if (artistService.createAlbum(artistId, title, description)) {
            System.out.println("✅ Album '" + title + "' created successfully!");
        } else {
            System.out.println("❌ Failed to create album.");
        }
    }

    private static void viewSongFavorites(long userId) {
        long artistId = artistService.getArtistIdByUserId(userId);
        if (artistId == -1) {
            System.out.println("❌ Artist profile not found!");
            return;
        }

        List<Song> songs = songService.getSongsByArtist(artistId);
        if (songs.isEmpty()) {
            System.out.println("\n🎵 No songs uploaded yet.");
            return;
        }

        System.out.println("\n=== SONG FAVORITES ===");
        for (Song song : songs) {
            int favCount = favoriteService.getFavoriteCount(song.getSongId());
            System.out.printf("♥ %d - %s%n", favCount, song.getTitle());
        }
    }
}
