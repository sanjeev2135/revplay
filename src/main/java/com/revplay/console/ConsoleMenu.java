package com.revplay.console;

import com.revplay.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

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
        Scanner scanner = new Scanner(System.in);

        do {
            if (currentUser.isArtist()) {
                System.out.println("\n=== ARTIST MENU (" + currentUser.getUsername() + ") ===");
            } else {
                System.out.println("\n=== USER MENU (" + currentUser.getUsername() + ") ===");
            }
            System.out.println("1. Music Library (Search, Browse, Favorites, History)");
            System.out.println("2. My Playlists (Create, Edit, Delete)");
            System.out.println("3. Music Player (Play, Pause, Skip, Repeat)");
            // Only show Artist Profile option for artists
            if (currentUser.isArtist()) {
                System.out.println("4. Artist Profile");
                System.out.println("5. Logout");
            } else {
                System.out.println("4. Logout");
            }
            System.out.print("Choose: ");

            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> MusicLibraryMenu.show(currentUser.getUserId());
                case 2 -> PlaylistMenu.show(currentUser.getUserId());
                case 3 -> MusicMenu.show(currentUser.getUserId());
                case 4 -> {
                    if (currentUser.isArtist()) {
                        ArtistMenu.show(currentUser.getUserId());
                    } else {
                        System.out.println("👋 Logged out, " + currentUser.getUsername());
                        currentUser = null;
                        start();
                    }
                }
                case 5 -> {
                    if (currentUser.isArtist()) {
                        System.out.println("👋 Logged out, " + currentUser.getUsername());
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
