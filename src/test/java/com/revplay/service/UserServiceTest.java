package com.revplay.service;

import com.revplay.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }

    @Test
    void testUserServiceCreation() {
        assertNotNull(userService);
    }

    @Test
    void testUserModelValidation() {
        User user = new User("testuser", "test@example.com", "hashedpass", false);
        
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("hashedpass", user.getPasswordHash());
        assertFalse(user.isArtist());
    }

    @Test
    void testUserModelSettersAndGetters() {
        User user = new User();
        
        user.setUserId(1L);
        user.setUsername("newuser");
        user.setEmail("new@example.com");
        user.setPasswordHash("newpass");
        user.setArtist(true);
        
        assertEquals(1L, user.getUserId());
        assertEquals("newuser", user.getUsername());
        assertEquals("new@example.com", user.getEmail());
        assertEquals("newpass", user.getPasswordHash());
        assertTrue(user.isArtist());
    }

    @Test
    void testUserModelWithSocialLinks() {
        User user = new User("artist", "artist@example.com", "pass", true);
        user.setSocialLinks("{\"twitter\": \"@artist\"}");
        
        assertEquals("artist", user.getUsername());
        assertTrue(user.isArtist());
        assertEquals("{\"twitter\": \"@artist\"}", user.getSocialLinks());
    }

    @Test
    void testUserModelSecurityFields() {
        User user = new User();
        user.setSecurityQuestions("What is your first pet?");
        
        assertEquals("What is your first pet?", user.getSecurityQuestions());
    }

}
