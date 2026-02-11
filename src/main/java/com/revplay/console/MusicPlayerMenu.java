package com.revplay.console;

import com.revplay.service.MusicPlayerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class MusicPlayerMenu {
    private static final Logger logger = LogManager.getLogger(MusicPlayerMenu.class);
    private static final MusicPlayerService player = new MusicPlayerService();
    private static final Scanner scanner = new Scanner(System.in);
    private static long currentUserId = 0;

    public static void show(long userId) {
        currentUserId = userId;
        int choice;
        do {
            System.out.println("\n=== MUSIC PLAYER ===");
            System.out.println("1. Play Song");
            System.out.println("2. Pause");
            System.out.println("3. Resume");
            System.out.println("4. Skip");
            System.out.println("5. Previous");
            System.out.println("6. Stop");
            System.out.println("7. Toggle Repeat");
            System.out.println("8. Toggle Favorite");
            System.out.println("9. Current Song Info");
            System.out.println("10. Back");
            System.out.print("Choose: ");

            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Song ID: ");
                    long songId = scanner.nextLong();
                    scanner.nextLine();
                    player.playSong(songId, currentUserId);
                }
                case 2 -> player.pause();
                case 3 -> player.resume();
                case 4 -> player.skip();
                case 5 -> player.previous();
                case 6 -> player.stop();
                case 7 -> player.toggleRepeat();
                case 8 -> player.toggleFavorite();
                case 9 -> logger.info("{}", player.getCurrentSongInfo());
                case 10 -> logger.info("← Back to music menu");
                default -> logger.warn("❌ Invalid choice!");
            }
        } while (choice != 10);
    }

    public static void playSongImmediate(long songId, long userId) {
        player.playSong(songId, userId);
        logger.info("Now playing: {}", player.getCurrentSongInfo());
        
        // Simple immediate play interface
        System.out.println("\n=== NOW PLAYING ===");
        System.out.println(player.getCurrentSongInfo());
        System.out.println("1. Pause/Resume");
        System.out.println("2. Next");
        System.out.println("3. Favorite");
        System.out.println("4. Stop");
        System.out.print("Choose: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1 -> {
                if (player.isPlaying()) {
                    player.pause();
                    logger.info("Paused");
                } else {
                    player.resume();
                    logger.info("Resumed");
                }
            }
            case 2 -> player.skip();
            case 3 -> player.toggleFavorite();
            case 4 -> player.stop();
        }
    }
}
