package com.revplay;

import com.revplay.model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestMain {

    @Test
    void testBasicMath() {
        assertEquals(2, 1 + 1);
    }

    @Test
    void testUserModel() {
        User user = new User("test", "test@test.com", "pass", true);
        assertEquals("test", user.getUsername());
        assertTrue(user.isArtist());
    }
}
