package com.revplay.console;

import com.revplay.model.User;
import com.revplay.service.ServiceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AuthMenu {
    private static final Logger logger = LogManager.getLogger(AuthMenu.class);

    public static User showAuthMenu() {
        int choice;

        do {
            System.out.println("\n=== AUTHENTICATION ===");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Choose: ");

            choice = ServiceManager.getScanner().nextInt();
            ServiceManager.getScanner().nextLine();

            switch (choice) {
                case 1 -> {
                    User user = login();
                    if (user != null) {
                        return user;
                    }
                }
                case 2 -> {
                    User user = register();
                    if (user != null) {
                        return user;
                    }
                }
                case 3 -> {
                    System.out.println("👋 Goodbye!");
                    return null;
                }
                default -> System.out.println("❌ Invalid choice!");
            }
        } while (choice != 3);

        return null;
    }

    private static User login() {
        try {
            System.out.print("Username: ");
            String username = ServiceManager.getScanner().nextLine().trim();
            System.out.print("Password: ");
            String password = ServiceManager.getScanner().nextLine().trim();

            User user = ServiceManager.getUserService().login(username, password);
            if (user != null) {
                System.out.println("✅ Login successful! Welcome, " + user.getUsername() + "!");
                return user;
            } else {
                System.out.println("❌ Invalid username or password!");
                return null;
            }
        } catch (Exception e) {
            logger.error("Login failed: {}", e.getMessage());
            System.out.println("❌ Login failed. Please try again.");
            return null;
        }
    }

    private static User register() {
        try {
            System.out.print("Username: ");
            String username = ServiceManager.getScanner().nextLine().trim();
            System.out.print("Password: ");
            String password = ServiceManager.getScanner().nextLine().trim();
            System.out.print("Email: ");
            String email = ServiceManager.getScanner().nextLine().trim();
            System.out.print("Is this an artist account? (y/n): ");
            boolean isArtist = ServiceManager.getScanner().nextLine().trim().equalsIgnoreCase("y");

            User user = new User();
            user.setUsername(username);
            user.setPasswordHash(password); // Store password as hash
            user.setEmail(email);
            user.setArtist(isArtist);

            boolean registered = ServiceManager.getUserService().register(user);
            if (registered) {
                System.out.println("✅ Registration successful! Welcome, " + user.getUsername() + "!");
                return user;
            } else {
                System.out.println("❌ Registration failed. Username may already exist.");
                return null;
            }
        } catch (Exception e) {
            logger.error("Registration failed: {}", e.getMessage());
            System.out.println("❌ Registration failed. Please try again.");
            return null;
        }
    }
}
