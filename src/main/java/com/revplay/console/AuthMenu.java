package com.revplay.console;

import com.revplay.model.User;
import com.revplay.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class AuthMenu {
    private static final Logger logger = LogManager.getLogger(AuthMenu.class);
    private static final UserService userService = new UserService();
    private static final Scanner scanner = new Scanner(System.in);

    public static User showAuthMenu() {
        int choice;
        do {
            System.out.println("\n=== AUTHORIZATION MENU ===");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose: ");

            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (java.util.InputMismatchException e) {
                logger.warn("❌ Invalid input! Please enter a number (1-3).");
                scanner.nextLine(); // Clear the invalid input
                choice = 0; // Set to invalid choice to continue loop
            }

            switch (choice) {
                case 1 -> {
                    User user = register();
                    if (user != null) return user;
                }
                case 2 -> {
                    User user = login();
                    if (user != null) return user;
                }
                case 3 -> {
                    logger.info("👋 Goodbye!");
                    return null;
                }
                default -> logger.warn("❌ Invalid choice!");
            }
        } while (choice != 3);
        return null;
    }

    private static User register() {
        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        System.out.print("Artist? (y/n): ");
        boolean isArtist = scanner.nextLine().toLowerCase().startsWith("y");

        String socialLinks = null;
        if (isArtist) {
            System.out.print("Social media links (e.g., Instagram, Twitter): ");
            socialLinks = scanner.nextLine();
            if (socialLinks.isEmpty()) {
                socialLinks = "{}";
            }
        }

        User user = new User(username, email, password, isArtist, socialLinks);
        if (userService.register(user)) {
            System.out.println("✅ Registered as " + username);
            // Fetch the user from DB to get the generated user_id
            User registeredUser = userService.login(username, password);
            return registeredUser;
        }
        System.out.println("❌ Registration failed");
        return null;
    }

    private static User login() {
        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        System.out.println("🔍 Attempting login: " + username);  // Debug
        User user = userService.login(username, password);

        if (user != null) {
            logger.info("✅ Welcome, {}!", user.getUsername());
            return user;
        } else {
            logger.error("❌ Invalid credentials for {}", username);
            return null;
        }
    }
}
