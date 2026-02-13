package com.revplay.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Scanner;


public class ServiceManager {
    private static final Logger logger = LogManager.getLogger(ServiceManager.class);
    private static final Scanner scanner = new Scanner(System.in);
    
    private static final UserService userService = new UserService();
    private static final SongService songService = new SongService();
    private static final ArtistService artistService = new ArtistService();
    private static final AlbumService albumService = new AlbumService();
    private static final PlaylistService playlistService = new PlaylistService();
    private static final FavoriteService favoriteService = new FavoriteService();
    private static final ListeningHistoryService historyService = new ListeningHistoryService();
    private static final PodcastService podcastService = new PodcastService();
    private static final StatsService statsService = new StatsService();
    private static final MusicPlayerService musicPlayerService = new MusicPlayerService();
    
    // Private constructor to prevent instantiation
    private ServiceManager() {}
    
    public static UserService getUserService() { return userService; }
    public static SongService getSongService() { return songService; }
    public static ArtistService getArtistService() { return artistService; }
    public static AlbumService getAlbumService() { return albumService; }
    public static PlaylistService getPlaylistService() { return playlistService; }
    public static FavoriteService getFavoriteService() { return favoriteService; }
    public static ListeningHistoryService getHistoryService() { return historyService; }
    public static PodcastService getPodcastService() { return podcastService; }
    public static StatsService getStatsService() { return statsService; }
    public static MusicPlayerService getMusicPlayerService() { return musicPlayerService; }
    
    public static Scanner getScanner() { return scanner; }
   
    public static void logInfo(String message) { logger.info(message); }
    public static void logError(String message) { logger.error(message); }
    public static void logWarn(String message) { logger.warn(message); }
}
