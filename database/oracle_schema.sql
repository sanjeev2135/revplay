-- RevPlay Music Streaming Database Schema
-- Oracle Database with PL/SQL Procedures

-- Create sequences for auto-increment IDs
CREATE SEQUENCE user_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE artist_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE album_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE song_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE podcast_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE podcast_episode_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE playlist_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE favorite_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE listening_history_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

-- Users table (both listeners and artists)
CREATE TABLE users (
    user_id NUMBER PRIMARY KEY,
    username VARCHAR2(50) NOT NULL UNIQUE,
    email VARCHAR2(100) NOT NULL UNIQUE,
    password_hash VARCHAR2(255) NOT NULL,
    is_artist CHAR(1) DEFAULT 'N' CHECK (is_artist IN ('Y', 'N')),
    security_question VARCHAR2(255),
    security_answer VARCHAR2(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Artists table (extended profile for artist users)
CREATE TABLE artists (
    artist_id NUMBER PRIMARY KEY,
    user_id NUMBER NOT NULL UNIQUE,
    bio CLOB,
    genre VARCHAR2(50),
    social_media_links CLOB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Albums table
CREATE TABLE albums (
    album_id NUMBER PRIMARY KEY,
    artist_id NUMBER NOT NULL,
    title VARCHAR2(100) NOT NULL,
    description CLOB,
    release_date DATE,
    cover_image_url VARCHAR2(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (artist_id) REFERENCES artists(artist_id) ON DELETE CASCADE
);

-- Songs table
CREATE TABLE songs (
    song_id NUMBER PRIMARY KEY,
    title VARCHAR2(100) NOT NULL,
    artist_id NUMBER NOT NULL,
    album_id NUMBER,
    genre VARCHAR2(50),
    duration_seconds NUMBER NOT NULL,
    file_url VARCHAR2(255),
    play_count NUMBER DEFAULT 0,
    release_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (artist_id) REFERENCES artists(artist_id) ON DELETE CASCADE,
    FOREIGN KEY (album_id) REFERENCES albums(album_id) ON DELETE SET NULL
);

-- Podcasts table
CREATE TABLE podcasts (
    podcast_id NUMBER PRIMARY KEY,
    title VARCHAR2(100) NOT NULL,
    host_name VARCHAR2(100) NOT NULL,
    description CLOB,
    genre VARCHAR2(50),
    cover_image_url VARCHAR2(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Podcast Episodes table
CREATE TABLE podcast_episodes (
    episode_id NUMBER PRIMARY KEY,
    podcast_id NUMBER NOT NULL,
    title VARCHAR2(100) NOT NULL,
    description CLOB,
    duration_seconds NUMBER NOT NULL,
    file_url VARCHAR2(255),
    publish_date DATE,
    play_count NUMBER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (podcast_id) REFERENCES podcasts(podcast_id) ON DELETE CASCADE
);

-- Playlists table
CREATE TABLE playlists (
    playlist_id NUMBER PRIMARY KEY,
    user_id NUMBER NOT NULL,
    name VARCHAR2(100) NOT NULL,
    description CLOB,
    is_public CHAR(1) DEFAULT 'N' CHECK (is_public IN ('Y', 'N')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Playlist Songs junction table
CREATE TABLE playlist_songs (
    playlist_id NUMBER NOT NULL,
    song_id NUMBER NOT NULL,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    position_order NUMBER NOT NULL,
    PRIMARY KEY (playlist_id, song_id),
    FOREIGN KEY (playlist_id) REFERENCES playlists(playlist_id) ON DELETE CASCADE,
    FOREIGN KEY (song_id) REFERENCES songs(song_id) ON DELETE CASCADE
);

-- Favorites table (user's favorite songs)
CREATE TABLE favorites (
    favorite_id NUMBER PRIMARY KEY,
    user_id NUMBER NOT NULL,
    song_id NUMBER NOT NULL,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_favorite UNIQUE (user_id, song_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (song_id) REFERENCES songs(song_id) ON DELETE CASCADE
);

-- Listening History table
CREATE TABLE listening_history (
    history_id NUMBER PRIMARY KEY,
    user_id NUMBER NOT NULL,
    song_id NUMBER NOT NULL,
    played_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    duration_played_seconds NUMBER NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (song_id) REFERENCES songs(song_id) ON DELETE CASCADE
);

-- Create triggers for auto-increment IDs
CREATE OR REPLACE TRIGGER user_trigger
BEFORE INSERT ON users
FOR EACH ROW
BEGIN
    :NEW.user_id := user_seq.NEXTVAL;
END;
/

CREATE OR REPLACE TRIGGER artist_trigger
BEFORE INSERT ON artists
FOR EACH ROW
BEGIN
    :NEW.artist_id := artist_seq.NEXTVAL;
END;
/

CREATE OR REPLACE TRIGGER album_trigger
BEFORE INSERT ON albums
FOR EACH ROW
BEGIN
    :NEW.album_id := album_seq.NEXTVAL;
END;
/

CREATE OR REPLACE TRIGGER song_trigger
BEFORE INSERT ON songs
FOR EACH ROW
BEGIN
    :NEW.song_id := song_seq.NEXTVAL;
END;
/

CREATE OR REPLACE TRIGGER podcast_trigger
BEFORE INSERT ON podcasts
FOR EACH ROW
BEGIN
    :NEW.podcast_id := podcast_seq.NEXTVAL;
END;
/

CREATE OR REPLACE TRIGGER podcast_episode_trigger
BEFORE INSERT ON podcast_episodes
FOR EACH ROW
BEGIN
    :NEW.episode_id := podcast_episode_seq.NEXTVAL;
END;
/

CREATE OR REPLACE TRIGGER playlist_trigger
BEFORE INSERT ON playlists
FOR EACH ROW
BEGIN
    :NEW.playlist_id := playlist_seq.NEXTVAL;
END;
/

CREATE OR REPLACE TRIGGER favorite_trigger
BEFORE INSERT ON favorites
FOR EACH ROW
BEGIN
    :NEW.favorite_id := favorite_seq.NEXTVAL;
END;
/

CREATE OR REPLACE TRIGGER listening_history_trigger
BEFORE INSERT ON listening_history
FOR EACH ROW
BEGIN
    :NEW.history_id := listening_history_seq.NEXTVAL;
END;
/

-- Indexes for performance optimization
CREATE INDEX idx_songs_artist ON songs(artist_id);
CREATE INDEX idx_songs_album ON songs(album_id);
CREATE INDEX idx_songs_genre ON songs(genre);
CREATE INDEX idx_albums_artist ON albums(artist_id);
CREATE INDEX idx_playlists_user ON playlists(user_id);
CREATE INDEX idx_playlist_songs_playlist ON playlist_songs(playlist_id);
CREATE INDEX idx_favorites_user ON favorites(user_id);
CREATE INDEX idx_listening_history_user ON listening_history(user_id);
CREATE INDEX idx_listening_history_song ON listening_history(song_id);
CREATE INDEX idx_podcast_episodes_podcast ON podcast_episodes(podcast_id);

-- ==================== PL/SQL STORED PROCEDURES ====================

-- User registration procedure
CREATE OR REPLACE PROCEDURE register_user(
    p_username IN VARCHAR2,
    p_email IN VARCHAR2,
    p_password_hash IN VARCHAR2,
    p_is_artist IN CHAR
) AS
    v_user_id NUMBER;
BEGIN
    -- Insert user
    INSERT INTO users (username, email, password_hash, is_artist)
    VALUES (p_username, p_email, p_password_hash, p_is_artist);
    
    -- Get the newly created user_id
    SELECT user_id INTO v_user_id FROM users WHERE username = p_username;
    
    -- If user is an artist, create artist profile and default album
    IF p_is_artist = 'Y' THEN
        -- Create artist profile
        INSERT INTO artists (user_id, bio, genre, social_media_links)
        VALUES (v_user_id, 'Artist bio for ' || p_username, 'General', '{}');
        
        -- Create default album for the artist
        INSERT INTO albums (artist_id, title, description, release_date)
        VALUES ((SELECT artist_id FROM artists WHERE user_id = v_user_id), 
                'Default Album', 'Default album for ' || p_username, SYSDATE);
    END IF;
END register_user;
/

-- Playlist creation procedure
CREATE OR REPLACE PROCEDURE create_playlist(
    p_user_id IN NUMBER,
    p_name IN VARCHAR2,
    p_description IN CLOB,
    p_is_public IN CHAR
) AS
BEGIN
    INSERT INTO playlists (user_id, name, description, is_public)
    VALUES (p_user_id, p_name, p_description, p_is_public);
END create_playlist;
/

-- Song upload procedure
CREATE OR REPLACE PROCEDURE upload_song(
    p_album_id IN NUMBER,
    p_title IN VARCHAR2,
    p_genre IN VARCHAR2,
    p_duration_seconds IN NUMBER,
    p_release_date IN DATE,
    p_file_url IN VARCHAR2
) AS
    v_artist_id NUMBER;
BEGIN
    -- Get artist_id from album
    SELECT artist_id INTO v_artist_id FROM albums WHERE album_id = p_album_id;
    
    INSERT INTO songs (title, artist_id, album_id, genre, duration_seconds, release_date, file_url)
    VALUES (p_title, v_artist_id, p_album_id, p_genre, p_duration_seconds, p_release_date, p_file_url);
END upload_song;
/

-- Add song to playlist procedure
CREATE OR REPLACE PROCEDURE add_song_to_playlist(
    p_playlist_id IN NUMBER,
    p_song_id IN NUMBER
) AS
    v_max_position NUMBER;
BEGIN
    -- Get the next position order
    SELECT NVL(MAX(position_order), 0) + 1 
    INTO v_max_position 
    FROM playlist_songs 
    WHERE playlist_id = p_playlist_id;
    
    INSERT INTO playlist_songs (playlist_id, song_id, position_order)
    VALUES (p_playlist_id, p_song_id, v_max_position);
END add_song_to_playlist;
/

-- Remove song from playlist procedure
CREATE OR REPLACE PROCEDURE remove_song_from_playlist(
    p_playlist_id IN NUMBER,
    p_song_id IN NUMBER
) AS
BEGIN
    DELETE FROM playlist_songs 
    WHERE playlist_id = p_playlist_id AND song_id = p_song_id;
END remove_song_from_playlist;
/

-- Increment song play count procedure
CREATE OR REPLACE PROCEDURE increment_song_play_count(
    p_song_id IN NUMBER
) AS
BEGIN
    UPDATE songs 
    SET play_count = play_count + 1 
    WHERE song_id = p_song_id;
END increment_song_play_count;
/

-- Add to favorites procedure
CREATE OR REPLACE PROCEDURE add_favorite(
    p_user_id IN NUMBER,
    p_song_id IN NUMBER
) AS
BEGIN
    INSERT INTO favorites (user_id, song_id)
    VALUES (p_user_id, p_song_id);
EXCEPTION
    WHEN DUP_VAL_ON_INDEX THEN
        -- Song already in favorites, update added_at
        UPDATE favorites 
        SET added_at = CURRENT_TIMESTAMP 
        WHERE user_id = p_user_id AND song_id = p_song_id;
END add_favorite;
/

-- Remove from favorites procedure
CREATE OR REPLACE PROCEDURE remove_favorite(
    p_user_id IN NUMBER,
    p_song_id IN NUMBER
) AS
BEGIN
    DELETE FROM favorites 
    WHERE user_id = p_user_id AND song_id = p_song_id;
END remove_favorite;
/

-- Add to listening history procedure
CREATE OR REPLACE PROCEDURE add_listening_history(
    p_user_id IN NUMBER,
    p_song_id IN NUMBER,
    p_duration_played_seconds IN NUMBER
) AS
BEGIN
    INSERT INTO listening_history (user_id, song_id, duration_played_seconds)
    VALUES (p_user_id, p_song_id, p_duration_played_seconds);
END add_listening_history;
/

-- Increment podcast episode play count procedure
CREATE OR REPLACE PROCEDURE increment_episode_play_count(
    p_episode_id IN NUMBER
) AS
BEGIN
    UPDATE podcast_episodes 
    SET play_count = play_count + 1 
    WHERE episode_id = p_episode_id;
END increment_episode_play_count;
/

-- Sample data insertion
BEGIN
    -- Insert sample users
    INSERT INTO users (username, email, password_hash, is_artist, security_question, security_answer)
    VALUES ('john_listener', 'john@example.com', 'hashed_password_1', 'N', 'What was your first pet?', 'fluffy');
    
    INSERT INTO users (username, email, password_hash, is_artist, security_question, security_answer)
    VALUES ('jane_artist', 'jane@example.com', 'hashed_password_2', 'Y', 'What city were you born in?', 'newyork');
    
    INSERT INTO users (username, email, password_hash, is_artist, security_question, security_answer)
    VALUES ('mike_user', 'mike@example.com', 'hashed_password_3', 'N', 'What is your mother''s maiden name?', 'smith');
    
    -- Insert artist profile for jane_artist
    INSERT INTO artists (user_id, bio, genre, social_media_links)
    VALUES (2, 'Indie artist creating soulful music', 'Indie Pop', '{"twitter": "@janeartist", "instagram": "@jane_music"}');
    
    -- Insert sample albums
    INSERT INTO albums (artist_id, title, description, release_date)
    VALUES (1, 'First Album', 'My debut album with heartfelt songs', TO_DATE('2023-01-15', 'YYYY-MM-DD'));
    
    INSERT INTO albums (artist_id, title, description, release_date)
    VALUES (1, 'Second Album', 'Continuing the musical journey', TO_DATE('2024-06-20', 'YYYY-MM-DD'));
    
    -- Insert sample songs using the procedure
    upload_song(1, 'Sunrise Dreams', 'Indie Pop', 210, TO_DATE('2023-01-15', 'YYYY-MM-DD'), '/uploads/Sunrise_Dreams.mp3');
    upload_song(1, 'Midnight Thoughts', 'Indie Pop', 195, TO_DATE('2023-01-15', 'YYYY-MM-DD'), '/uploads/Midnight_Thoughts.mp3');
    upload_song(2, 'Summer Breeze', 'Indie Pop', 180, TO_DATE('2024-06-20', 'YYYY-MM-DD'), '/uploads/Summer_Breeze.mp3');
    upload_song(2, 'Autumn Leaves', 'Indie Pop', 220, TO_DATE('2024-06-20', 'YYYY-MM-DD'), '/uploads/Autumn_Leaves.mp3');
    
    -- Insert sample podcasts
    INSERT INTO podcasts (podcast_id, title, host_name, description, genre)
    VALUES (podcast_seq.NEXTVAL, 'Tech Talks', 'Alex Johnson', 'Weekly technology discussions', 'Technology');
    
    INSERT INTO podcasts (podcast_id, title, host_name, description, genre)
    VALUES (podcast_seq.NEXTVAL, 'Music History', 'Sarah Williams', 'Exploring the evolution of music', 'Music');
    
    -- Insert sample podcast episodes
    INSERT INTO podcast_episodes (episode_id, podcast_id, title, description, duration_seconds, publish_date)
    VALUES (podcast_episode_seq.NEXTVAL, 1, 'AI Revolution', 'Discussing the impact of AI on society', 1800, TO_DATE('2024-01-10', 'YYYY-MM-DD'));
    
    INSERT INTO podcast_episodes (episode_id, podcast_id, title, description, duration_seconds, publish_date)
    VALUES (podcast_episode_seq.NEXTVAL, 1, 'Blockchain Basics', 'Understanding blockchain technology', 1500, TO_DATE('2024-01-17', 'YYYY-MM-DD'));
    
    -- Insert sample playlists using the procedure
    create_playlist(1, 'My Favorites', 'Collection of my favorite songs', 'N');
    create_playlist(1, 'Chill Vibes', 'Relaxing songs for unwinding', 'Y');
    create_playlist(3, 'Workout Mix', 'High energy songs for exercise', 'N');
    
    -- Add songs to playlists using the procedure
    add_song_to_playlist(1, 1);
    add_song_to_playlist(1, 3);
    add_song_to_playlist(2, 2);
    add_song_to_playlist(2, 4);
    add_song_to_playlist(3, 1);
    add_song_to_playlist(3, 3);
    
    -- Add to favorites using the procedure
    add_favorite(1, 1);
    add_favorite(1, 3);
    add_favorite(3, 2);
    add_favorite(3, 4);
    
    -- Add to listening history using the procedure
    add_listening_history(1, 1, 210);
    add_listening_history(1, 3, 180);
    add_listening_history(1, 2, 195);
    add_listening_history(3, 2, 195);
    add_listening_history(3, 4, 220);
END;
/

COMMIT;
