package com.revplay.console;

import com.revplay.model.User;
import com.revplay.service.ServiceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConsoleMenu {
    private static final Logger logger = LogManager.getLogger(ConsoleMenu.class);
    private static User currentUser = null;

    public static void start() {
        logger.info("🎵 Welcome to RevPlay Music Streaming! 🎵");

        currentUser = AuthMenu.showAuthMenu();
        if (currentUser != null) {
            showUserMenu();
        }
    }

    private static void showUserMenu() {
        int choice;

        do {
            // Show permanent username header
            System.out.println("\n" + "=".repeat(50));
            System.out.println("👤 Logged in as: " + currentUser.getUsername());
            if (currentUser.isArtist()) {
                System.out.println("🎵 Artist Account");
            } else {
                System.out.println("🎧 User Account");
            }
            System.out.println("=".repeat(50));
            
            if (currentUser.isArtist()) {
                System.out.println("\n=== ARTIST MENU ===");
            } else {
                System.out.println("\n=== USER MENU ===");
            }
            System.out.println("1. Music Library ");
            System.out.println("2. My Playlists ");
            System.out.println("3. Music Player ");
            
            if (currentUser.isArtist()) {
                System.out.println("4. Artist Profile");
                System.out.println("5. Logout");
            } else {
                System.out.println("4. Logout");
            }
            System.out.print("Choose: ");

            choice = ServiceManager.getScanner().nextInt();
            ServiceManager.getScanner().nextLine();

            switch (choice) {
                case 1 -> MusicLibraryMenu.show(currentUser.getUserId());
                case 2 -> PlaylistMenu.show(currentUser.getUserId());
                case 3 -> MusicMenu.show(currentUser.getUserId());
                case 4 -> {
                    if (currentUser.isArtist()) {
                        ArtistMenu.show(currentUser.getUserId());
                    } else {
                        System.out.println("👋 Logged out");
                        ServiceManager.getMusicPlayerService().clearCurrentSong(); // Clear music player state
                        currentUser = null;
                        start();
                    }
                }
                case 5 -> {
                    if (currentUser.isArtist()) {
                        System.out.println("👋 Logged out");
                        ServiceManager.getMusicPlayerService().clearCurrentSong(); // Clear music player state
                        currentUser = null;
                        start();
                    } else {
                        System.out.println("❌ Invalid choice!");
                    }
                }
                default -> System.out.println("❌ Invalid choice!");
            }
        } while (choice != 4 && choice != 5 || (currentUser.isArtist() && choice == 4));
    }
}
