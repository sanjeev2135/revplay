package com.revplay.model;



public class User {

    private long userId;

    private String username;

    private String email;

    private String passwordHash;  // Never store plaintext in real app

    private boolean isArtist;

    private String socialLinks;  // For artist social media

    private String securityQuestions;  // For password recovery



    // Default constructor

    public User() {}



    // Constructor for registration

    public User(String username, String email, String passwordHash, boolean isArtist) {

        this.username = username;

        this.email = email;

        this.passwordHash = passwordHash;

        this.isArtist = isArtist;

    }



    // Constructor for registration with social links

    public User(String username, String email, String passwordHash, boolean isArtist, String socialLinks) {

        this.username = username;

        this.email = email;

        this.passwordHash = passwordHash;

        this.isArtist = isArtist;

        this.socialLinks = socialLinks;

    }



    // Getters and setters

    public long getUserId() { return userId; }

    public void setUserId(long userId) { this.userId = userId; }



    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }



    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }



    public String getPasswordHash() { return passwordHash; }

    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }



    public boolean isArtist() { return isArtist; }

    public void setArtist(boolean artist) { isArtist = artist; }



    public String getSocialLinks() { return socialLinks; }

    public void setSocialLinks(String socialLinks) { this.socialLinks = socialLinks; }



    public String getSecurityQuestions() { return securityQuestions; }

    public void setSecurityQuestions(String securityQuestions) { this.securityQuestions = securityQuestions; }



    @Override

    public String toString() {

        return "User{id=" + userId + ", username='" + username + "', isArtist=" + isArtist + "}";

    }

}

