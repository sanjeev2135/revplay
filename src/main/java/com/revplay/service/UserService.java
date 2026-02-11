package com.revplay.service;



import com.revplay.dao.UserDao;

import com.revplay.dao.ArtistDao;

import com.revplay.dao.AlbumDao;

import com.revplay.model.User;

import com.revplay.model.Artist;

import com.revplay.model.Album;

import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;



public class UserService {

    private static final Logger logger = LogManager.getLogger(UserService.class);

    private final UserDao userDao = new UserDao();

    private final ArtistDao artistDao = new ArtistDao();

    private final AlbumDao albumDao = new AlbumDao();



    public boolean register(User user) {

        try {

            logger.info("Registering user: {}", user.getUsername());

            boolean success = userDao.registerUser(user);

            

            if (success && user.isArtist()) {

                // Get the newly created user to get user_id

                User createdUser = userDao.getUserByEmail(user.getEmail());

                if (createdUser != null) {

                    // Check if artist profile already exists

                    long existingArtistId = artistDao.findArtistIdByUserId(createdUser.getUserId());

                    long artistId;

                    

                    if (existingArtistId == -1) {

                        // Create artist profile only if it doesn't exist

                        Artist artist = new Artist();

                        artist.setUserId(createdUser.getUserId());

                        artist.setBio("Artist bio for " + user.getUsername());

                        artist.setGenre("General");

                        // Use social links from registration, or default to empty JSON

                        String socialLinks = user.getSocialLinks();

                        if (socialLinks == null || socialLinks.isEmpty()) {

                            socialLinks = "{}";

                        }

                        artist.setSocialLinks(socialLinks);

                        artistDao.createArtist(artist);

                        artistId = artistDao.findArtistIdByUserId(createdUser.getUserId());

                        logger.info("Created artist profile for: {}", user.getUsername());

                    } else {

                        artistId = existingArtistId;

                        logger.info("Artist profile already exists for: {}", user.getUsername());

                    }

                    

                    // Create default album for the artist (check if exists first)

                    if (!albumDao.hasAlbumForArtist(artistId)) {

                        Album album = new Album();

                        album.setArtistId(artistId);

                        album.setTitle("Default Album");

                        album.setDescription("Default album for " + user.getUsername());

                        albumDao.createAlbum(album);

                        logger.info("Created default album for: {}", user.getUsername());

                    }

                }

            }

            

            return success;

        } catch (Exception e) {

            logger.error("Registration failed for {}: {}", user.getUsername(), e.getMessage());

            return false;

        }

    }



    public User login(String username, String password) {

        try {

            logger.info("Login attempt for username: {}", username);

            return userDao.loginUser(username, password);

        } catch (Exception e) {

            logger.error("Login failed for {}: {}", username, e.getMessage());

            return null;

        }

    }



    public User getUserById(long userId) {

        try {

            return userDao.getUserById(userId);

        } catch (Exception e) {

            logger.error("Failed to get user by ID {}: {}", userId, e.getMessage());

            return null;

        }

    }



    public boolean updatePassword(long userId, String newPasswordHash) {

        try {

            return userDao.updatePassword(userId, newPasswordHash);

        } catch (Exception e) {

            logger.error("Failed to update password for user ID {}: {}", userId, e.getMessage());

            return false;

        }

    }



    public boolean updateSecurityInfo(long userId, String securityQuestion, String securityAnswer) {

        try {

            return userDao.updateSecurityInfo(userId, securityQuestion, securityAnswer);

        } catch (Exception e) {

            logger.error("Failed to update security info for user ID {}: {}", userId, e.getMessage());

            return false;

        }

    }



    public User getUserByEmail(String email) {

        try {

            return userDao.getUserByEmail(email);

        } catch (Exception e) {

            logger.error("Failed to get user by email {}: {}", email, e.getMessage());

            return null;

        }

    }



    public boolean recoverPassword(String email, String securityAnswer, String newPasswordHash) {

        try {

            User user = getUserByEmail(email);

            if (user != null && securityAnswer.equals(user.getSecurityQuestions())) {

                return updatePassword(user.getUserId(), newPasswordHash);

            }

            return false;

        } catch (Exception e) {

            logger.error("Password recovery failed for email {}: {}", email, e.getMessage());

            return false;

        }

    }

}

