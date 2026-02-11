package com.revplay.console;

import com.revplay.model.Album;
import com.revplay.model.Artist;
import com.revplay.model.Playlist;
import com.revplay.model.Song;
import com.revplay.service.AlbumService;
import com.revplay.service.ArtistService;
import com.revplay.service.FavoriteService;
import com.revplay.service.ListeningHistoryService;
import com.revplay.service.PlaylistService;
import com.revplay.service.PodcastService;
import com.revplay.service.SongService;
import com.revplay.model.Podcast;
import com.revplay.model.PodcastEpisode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class MusicLibraryMenu {
    private static final Logger logger = LogManager.getLogger(MusicLibraryMenu.class);
    private static final Scanner scanner = new Scanner(System.in);
    private static final SongService songService = new SongService();
    private static final AlbumService albumService = new AlbumService();
    private static final ArtistService artistService = new ArtistService();
    private static final PlaylistService playlistService = new PlaylistService();
    private static final FavoriteService favoriteService = new FavoriteService();
    private static final ListeningHistoryService historyService = new ListeningHistoryService();
    private static final PodcastService podcastService = new PodcastService();
    private static long currentUserId = 0;

    public static void show(long userId) {
        currentUserId = userId;
        int choice;
        do {
            System.out.println("\n=== MUSIC LIBRARY ===");
            System.out.println("1. Search Songs");
            System.out.println("2. Search Artists");
            System.out.println("3. Search Albums");
            System.out.println("4. Search Playlists");
            System.out.println("5. Browse by Genre");
            System.out.println("6. Browse by Artist");
            System.out.println("7. Browse by Album");
            System.out.println("8. View My Favorites");
            System.out.println("9. View Recently Played");
            System.out.println("10. View Listening History");
            System.out.println("11. Browse Podcasts");
            System.out.println("12. Music Player");
            System.out.println("13. Back");
            System.out.print("Choose: ");

            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> searchSongs();
                case 2 -> searchArtists();
                case 3 -> searchAlbums();
                case 4 -> searchPlaylists();
                case 5 -> browseByGenre();
                case 6 -> browseByArtist();
                case 7 -> browseByAlbum();
                case 8 -> viewFavorites();
                case 9 -> viewRecentlyPlayed();
                case 10 -> viewListeningHistory();
                case 11 -> browsePodcasts();
                case 12 -> com.revplay.console.MusicMenu.show(currentUserId);
                case 13 -> System.out.println("← Back to user menu");
                default -> System.out.println("❌ Invalid choice!");
            }
        } while (choice != 13);
    }

    private static void searchSongs() {
        try {
            System.out.print("Enter keyword: ");
            String keyword = scanner.nextLine();
            
            List<Song> songs = songService.searchSongs(keyword);
            if (songs.isEmpty()) {
                logger.info("No songs found for '{}'", keyword);
                return;
            }

            System.out.println("\n=== SEARCH RESULTS ===");
            for (int i = 0; i < songs.size(); i++) {
                Song song = songs.get(i);
                System.out.printf("%d. %s by %s [%d:%02d]%n", 
                    i + 1, song.getTitle(), song.getArtistName(),
                    song.getDurationSeconds() / 60, song.getDurationSeconds() % 60);
            }

            System.out.print("\nEnter song number to play (0 to skip): ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice > 0 && choice <= songs.size()) {
                Song selectedSong = songs.get(choice - 1);
                com.revplay.console.MusicPlayerMenu.playSongImmediate(selectedSong.getSongId(), currentUserId);
            }
        } catch (Exception e) {
            logger.error("Failed to search songs: {}", e.getMessage());
        }
    }

    private static void browseByGenre() {
        try {
            System.out.println("\n=== GENRES ===");
            System.out.println("1. Pop");
            System.out.println("2. Rock");
            System.out.println("3. Jazz");
            System.out.println("4. Classical");
            System.out.println("5. Hip Hop");
            System.out.println("6. Electronic");
            System.out.println("7. Indie");
            System.out.print("Choose genre: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            String[] genres = {"Pop", "Rock", "Jazz", "Classical", "Hip Hop", "Electronic", "Indie"};
            if (choice < 1 || choice > genres.length) {
                logger.warn("❌ Invalid genre choice!");
                return;
            }

            String genre = genres[choice - 1];
            List<Song> songs = songService.getSongsByGenre(genre);
            
            if (songs.isEmpty()) {
                logger.info("No songs found in {} genre", genre);
                return;
            }

            System.out.printf("\n=== %s SONGS ===%n", genre);
            for (int i = 0; i < songs.size(); i++) {
                Song song = songs.get(i);
                System.out.printf("%d. %s by %s [%d:%02d]%n", 
                    i + 1, song.getTitle(), song.getArtistName(),
                    song.getDurationSeconds() / 60, song.getDurationSeconds() % 60);
            }

            System.out.print("\nEnter song number to play (0 to skip): ");
            int songChoice = scanner.nextInt();
            scanner.nextLine();

            if (songChoice > 0 && songChoice <= songs.size()) {
                Song selectedSong = songs.get(songChoice - 1);
                com.revplay.console.MusicPlayerMenu.playSongImmediate(selectedSong.getSongId(), currentUserId);
            }
        } catch (Exception e) {
            logger.error("Failed to browse by genre: {}", e.getMessage());
        }
    }

    private static void viewFavorites() {
        try {
            var favorites = favoriteService.getUserFavorites(currentUserId);
            if (favorites.isEmpty()) {
                logger.info("You have no favorite songs yet.");
                return;
            }

            System.out.println("\n=== YOUR FAVORITES ===");
            for (int i = 0; i < favorites.size(); i++) {
                var favorite = favorites.get(i);
                System.out.printf("%d. %s by %s%n", 
                    i + 1, favorite.getSongTitle(), favorite.getArtistName());
            }

            System.out.print("\nEnter song number to play (0 to skip): ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice > 0 && choice <= favorites.size()) {
                var selectedFavorite = favorites.get(choice - 1);
                com.revplay.console.MusicPlayerMenu.playSongImmediate(selectedFavorite.getSongId(), currentUserId);
            }
        } catch (Exception e) {
            logger.error("Failed to view favorites: {}", e.getMessage());
        }
    }

    private static void viewRecentlyPlayed() {
        try {
            var recentlyPlayed = historyService.getRecentlyPlayed(currentUserId, 20);
            if (recentlyPlayed.isEmpty()) {
                logger.info("No recently played songs.");
                return;
            }

            System.out.println("\n=== RECENTLY PLAYED ===");
            for (int i = 0; i < recentlyPlayed.size(); i++) {
                var history = recentlyPlayed.get(i);
                System.out.printf("%d. %s by %s%n", 
                    i + 1, history.getSongTitle(), history.getArtistName());
            }

            System.out.print("\nEnter song number to play (0 to skip): ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice > 0 && choice <= recentlyPlayed.size()) {
                var selectedHistory = recentlyPlayed.get(choice - 1);
                com.revplay.console.MusicPlayerMenu.playSongImmediate(selectedHistory.getSongId(), currentUserId);
            }
        } catch (Exception e) {
            logger.error("Failed to view recently played: {}", e.getMessage());
        }
    }

    private static void viewListeningHistory() {
        try {
            var history = historyService.getUserHistory(currentUserId, 50);
            if (history.isEmpty()) {
                logger.info("No listening history available.");
                return;
            }

            System.out.println("\n=== LISTENING HISTORY ===");
            for (int i = 0; i < history.size(); i++) {
                var entry = history.get(i);
                System.out.printf("%d. %s by %s - Played at %s%n", 
                    i + 1, entry.getSongTitle(), entry.getArtistName(), entry.getPlayedAt());
            }

            System.out.print("\nEnter song number to play (0 to skip): ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice > 0 && choice <= history.size()) {
                var selectedHistory = history.get(choice - 1);
                com.revplay.console.MusicPlayerMenu.playSongImmediate(selectedHistory.getSongId(), currentUserId);
            }
        } catch (Exception e) {
            logger.error("Failed to view listening history: {}", e.getMessage());
        }
    }

    private static void browsePodcasts() {
        try {
            System.out.println("\n=== PODCASTS ===");
            System.out.println("1. All Podcasts");
            System.out.println("2. Search Podcasts");
            System.out.println("3. Browse by Genre");
            System.out.print("Choose: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> showAllPodcasts();
                case 2 -> searchPodcasts();
                case 3 -> browsePodcastsByGenre();
                default -> logger.warn("❌ Invalid choice!");
            }
        } catch (Exception e) {
            logger.error("Failed to browse podcasts: {}", e.getMessage());
        }
    }

    private static void showAllPodcasts() {
        try {
            List<Podcast> podcasts = podcastService.getAllPodcasts();
            if (podcasts.isEmpty()) {
                logger.info("No podcasts available.");
                return;
            }

            System.out.println("\n=== ALL PODCASTS ===");
            for (int i = 0; i < podcasts.size(); i++) {
                Podcast podcast = podcasts.get(i);
                System.out.printf("%d. %s - Host: %s (%s)%n", 
                    i + 1, podcast.getTitle(), podcast.getHostName(), podcast.getGenre());
            }

            System.out.print("\nEnter podcast number to view episodes (0 to skip): ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice > 0 && choice <= podcasts.size()) {
                Podcast selectedPodcast = podcasts.get(choice - 1);
                showPodcastEpisodes(selectedPodcast);
            }
        } catch (Exception e) {
            logger.error("Failed to show all podcasts: {}", e.getMessage());
        }
    }

    private static void searchPodcasts() {
        try {
            System.out.print("Enter keyword: ");
            String keyword = scanner.nextLine();
            
            List<Podcast> podcasts = podcastService.searchPodcasts(keyword);
            if (podcasts.isEmpty()) {
                logger.info("No podcasts found for '{}'", keyword);
                return;
            }

            System.out.println("\n=== SEARCH RESULTS ===");
            for (int i = 0; i < podcasts.size(); i++) {
                Podcast podcast = podcasts.get(i);
                System.out.printf("%d. %s - Host: %s (%s)%n", 
                    i + 1, podcast.getTitle(), podcast.getHostName(), podcast.getGenre());
            }

            System.out.print("\nEnter podcast number to view episodes (0 to skip): ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice > 0 && choice <= podcasts.size()) {
                Podcast selectedPodcast = podcasts.get(choice - 1);
                showPodcastEpisodes(selectedPodcast);
            }
        } catch (Exception e) {
            logger.error("Failed to search podcasts: {}", e.getMessage());
        }
    }

    private static void browsePodcastsByGenre() {
        try {
            System.out.print("Enter genre: ");
            String genre = scanner.nextLine();
            
            List<Podcast> podcasts = podcastService.getPodcastsByGenre(genre);
            if (podcasts.isEmpty()) {
                logger.info("No podcasts found in {} genre", genre);
                return;
            }

            System.out.printf("\n=== %s PODCASTS ===%n", genre);
            for (int i = 0; i < podcasts.size(); i++) {
                Podcast podcast = podcasts.get(i);
                System.out.printf("%d. %s - Host: %s%n", 
                    i + 1, podcast.getTitle(), podcast.getHostName());
            }

            System.out.print("\nEnter podcast number to view episodes (0 to skip): ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice > 0 && choice <= podcasts.size()) {
                Podcast selectedPodcast = podcasts.get(choice - 1);
                showPodcastEpisodes(selectedPodcast);
            }
        } catch (Exception e) {
            logger.error("Failed to browse podcasts by genre: {}", e.getMessage());
        }
    }

    private static void showPodcastEpisodes(Podcast podcast) {
        try {
            List<PodcastEpisode> episodes = podcastService.getEpisodesByPodcast(podcast.getPodcastId());
            if (episodes.isEmpty()) {
                logger.info("No episodes available for '{}'", podcast.getTitle());
                return;
            }

            System.out.printf("\n=== EPISODES OF %s ===%n", podcast.getTitle());
            for (int i = 0; i < episodes.size(); i++) {
                PodcastEpisode episode = episodes.get(i);
                System.out.printf("%d. %s [%d:%02d]%n", 
                    i + 1, episode.getTitle(),
                    episode.getDurationSeconds() / 60, episode.getDurationSeconds() % 60);
            }

            System.out.print("\nEnter episode number to play (0 to skip): ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice > 0 && choice <= episodes.size()) {
                PodcastEpisode selectedEpisode = episodes.get(choice - 1);
                podcastService.simulateEpisodePlay(currentUserId, selectedEpisode);
                logger.info("Now playing: {}", selectedEpisode.getTitle());
            }
        } catch (Exception e) {
            logger.error("Failed to show podcast episodes: {}", e.getMessage());
        }
    }

    private static void searchArtists() {
        try {
            System.out.print("Enter artist name: ");
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

            System.out.print("\nEnter artist number to view songs (0 to skip): ");
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
                
                System.out.print("\nEnter song number to play (0 to skip): ");
                int songChoice = scanner.nextInt();
                scanner.nextLine();
                
                if (songChoice > 0 && songChoice <= songs.size()) {
                    Song selectedSong = songs.get(songChoice - 1);
                    com.revplay.console.MusicPlayerMenu.playSongImmediate(selectedSong.getSongId(), currentUserId);
                }
            }
        } catch (Exception e) {
            logger.error("Failed to search artists: {}", e.getMessage());
        }
    }

    private static void searchAlbums() {
        try {
            System.out.print("Enter album name or artist: ");
            String keyword = scanner.nextLine();
            
            // Search through all albums by looking up artists first
            List<Artist> artists = artistService.searchArtistsByName(keyword);
            List<Album> allAlbums = new ArrayList<>();
            
            for (Artist artist : artists) {
                List<Album> artistAlbums = albumService.getAlbumsByArtist(artist.getArtistId());
                allAlbums.addAll(artistAlbums);
            }
            
            if (allAlbums.isEmpty()) {
                System.out.println("No albums found for '" + keyword + "'");
                return;
            }

            System.out.println("\n=== ALBUMS ===");
            for (int i = 0; i < allAlbums.size(); i++) {
                Album album = allAlbums.get(i);
                System.out.printf("%d. %s - %s%n", 
                    i + 1, album.getTitle(),
                    album.getDescription() != null ? album.getDescription() : "No description");
            }
        } catch (Exception e) {
            logger.error("Failed to search albums: {}", e.getMessage());
        }
    }

    private static void searchPlaylists() {
        try {
            System.out.print("Enter playlist name: ");
            String keyword = scanner.nextLine();
            
            List<Playlist> playlists = playlistService.getPublicPlaylists();
            if (playlists.isEmpty()) {
                System.out.println("No public playlists found.");
                return;
            }

            System.out.println("\n=== PUBLIC PLAYLISTS ===");
            int count = 0;
            for (Playlist playlist : playlists) {
                if (playlist.getName().toLowerCase().contains(keyword.toLowerCase())) {
                    count++;
                    System.out.printf("%d. %s - %s (%s)%n", 
                        count, playlist.getName(), 
                        playlist.getDescription(),
                        playlist.isPublic() ? "Public" : "Private");
                    
                    var songs = playlistService.getPlaylistSongs(playlist.getPlaylistId());
                    if (!songs.isEmpty()) {
                        System.out.println("   Songs:");
                        for (var song : songs) {
                            System.out.printf("   - %s by %s%n", song.getTitle(), song.getArtistName());
                        }
                    }
                    System.out.println();
                }
            }
            
            if (count == 0) {
                System.out.println("No playlists matching '" + keyword + "' found.");
            }
        } catch (Exception e) {
            logger.error("Failed to search playlists: {}", e.getMessage());
        }
    }

    private static void browseByArtist() {
        try {
            System.out.print("Enter artist name: ");
            String keyword = scanner.nextLine();
            
            List<Artist> artists = artistService.searchArtistsByName(keyword);
            if (artists.isEmpty()) {
                System.out.println("No artists found.");
                return;
            }

            System.out.println("\n=== ARTISTS ===");
            for (int i = 0; i < artists.size(); i++) {
                Artist artist = artists.get(i);
                System.out.printf("%d. %s (Genre: %s)%n", 
                    i + 1, artist.getStageName(), artist.getGenre());
            }

            System.out.print("\nEnter artist number to view songs (0 to skip): ");
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
                
                System.out.print("\nEnter song number to play (0 to skip): ");
                int songChoice = scanner.nextInt();
                scanner.nextLine();
                
                if (songChoice > 0 && songChoice <= songs.size()) {
                    Song selectedSong = songs.get(songChoice - 1);
                    com.revplay.console.MusicPlayerMenu.playSongImmediate(selectedSong.getSongId(), currentUserId);
                }
            }
        } catch (Exception e) {
            logger.error("Failed to browse by artist: {}", e.getMessage());
        }
    }

    private static void browseByAlbum() {
        try {
            System.out.print("Enter artist name to see their albums: ");
            String keyword = scanner.nextLine();
            
            List<Artist> artists = artistService.searchArtistsByName(keyword);
            if (artists.isEmpty()) {
                System.out.println("No artists found.");
                return;
            }

            System.out.println("\n=== ARTISTS ===");
            for (int i = 0; i < artists.size(); i++) {
                Artist artist = artists.get(i);
                System.out.printf("%d. %s%n", i + 1, artist.getStageName());
            }

            System.out.print("\nEnter artist number to view albums (0 to skip): ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice > 0 && choice <= artists.size()) {
                Artist selectedArtist = artists.get(choice - 1);
                List<Album> albums = albumService.getAlbumsByArtist(selectedArtist.getArtistId());
                
                if (albums.isEmpty()) {
                    System.out.println("No albums found for " + selectedArtist.getStageName());
                    return;
                }
                
                System.out.println("\n=== ALBUMS BY " + selectedArtist.getStageName().toUpperCase() + " ===");
                for (int i = 0; i < albums.size(); i++) {
                    Album album = albums.get(i);
                    System.out.printf("%d. %s - %s%n", 
                        i + 1, album.getTitle(),
                        album.getDescription() != null ? album.getDescription() : "No description");
                }
            }
        } catch (Exception e) {
            logger.error("Failed to browse by album: {}", e.getMessage());
        }
    }
}
