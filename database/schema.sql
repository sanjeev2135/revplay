-- RevPlay Music Streaming Database Schema
-- MySQL Database Schema

CREATE DATABASE IF NOT EXISTS revplay_db;
USE revplay_db;

-- Users table (both listeners and artists)
CREATE TABLE users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    is_artist BOOLEAN DEFAULT FALSE,
    security_question VARCHAR(255),
    security_answer VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Artists table (extended profile for artist users)
CREATE TABLE artists (
    artist_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    bio TEXT,
    genre VARCHAR(50),
    social_media_links JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Albums table
CREATE TABLE albums (
    album_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    artist_id BIGINT NOT NULL,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    release_date DATE,
    cover_image_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (artist_id) REFERENCES artists(artist_id) ON DELETE CASCADE
);

-- Songs table
CREATE TABLE songs (
    song_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    artist_id BIGINT NOT NULL,
    album_id BIGINT,
    genre VARCHAR(50),
    duration_seconds INT NOT NULL,
    file_url VARCHAR(255),
    play_count BIGINT DEFAULT 0,
    release_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (artist_id) REFERENCES artists(artist_id) ON DELETE CASCADE,
    FOREIGN KEY (album_id) REFERENCES albums(album_id) ON DELETE SET NULL
);

-- Podcasts table
CREATE TABLE podcasts (
    podcast_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    host_name VARCHAR(100) NOT NULL,
    description TEXT,
    genre VARCHAR(50),
    cover_image_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Podcast Episodes table
CREATE TABLE podcast_episodes (
    episode_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    podcast_id BIGINT NOT NULL,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    duration_seconds INT NOT NULL,
    file_url VARCHAR(255),
    publish_date DATE,
    play_count BIGINT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (podcast_id) REFERENCES podcasts(podcast_id) ON DELETE CASCADE
);

-- Playlists table
CREATE TABLE playlists (
    playlist_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    is_public BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Playlist Songs junction table
CREATE TABLE playlist_songs (
    playlist_id BIGINT NOT NULL,
    song_id BIGINT NOT NULL,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    position_order INT NOT NULL,
    PRIMARY KEY (playlist_id, song_id),
    FOREIGN KEY (playlist_id) REFERENCES playlists(playlist_id) ON DELETE CASCADE,
    FOREIGN KEY (song_id) REFERENCES songs(song_id) ON DELETE CASCADE
);

-- Favorites table (user's favorite songs)
CREATE TABLE favorites (
    favorite_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    song_id BIGINT NOT NULL,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY unique_favorite (user_id, song_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (song_id) REFERENCES songs(song_id) ON DELETE CASCADE
);

-- Listening History table
CREATE TABLE listening_history (
    history_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    song_id BIGINT NOT NULL,
    played_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    duration_played_seconds INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (song_id) REFERENCES songs(song_id) ON DELETE CASCADE
);

-- Recently Played view (can be created as a materialized view or query)
CREATE VIEW recently_played AS
SELECT DISTINCT
    u.user_id,
    u.username,
    s.song_id,
    s.title AS song_title,
    a.artist_id,
    ar.username AS artist_name,
    lh.played_at
FROM listening_history lh
JOIN users u ON lh.user_id = u.user_id
JOIN songs s ON lh.song_id = s.song_id
JOIN artists a ON s.artist_id = a.artist_id
JOIN users ar ON a.user_id = ar.user_id
ORDER BY lh.played_at DESC;

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

-- Insert sample data for testing
INSERT INTO users (username, email, password_hash, is_artist, security_question, security_answer) VALUES
('john_listener', 'john@example.com', 'hashed_password_1', FALSE, 'What was your first pet?', 'fluffy'),
('jane_artist', 'jane@example.com', 'hashed_password_2', TRUE, 'What city were you born in?', 'newyork'),
('mike_user', 'mike@example.com', 'hashed_password_3', FALSE, 'What is your mother''s maiden name?', 'smith');

INSERT INTO artists (user_id, bio, genre, social_media_links) VALUES
(2, 'Indie artist creating soulful music', 'Indie Pop', '{"twitter": "@janeartist", "instagram": "@jane_music"}');

INSERT INTO albums (artist_id, title, description, release_date) VALUES
(1, 'First Album', 'My debut album with heartfelt songs', '2023-01-15'),
(1, 'Second Album', 'Continuing the musical journey', '2024-06-20');

INSERT INTO songs (title, artist_id, album_id, genre, duration_seconds, release_date) VALUES
('Sunrise Dreams', 1, 1, 'Indie Pop', 210, '2023-01-15'),
('Midnight Thoughts', 1, 1, 'Indie Pop', 195, '2023-01-15'),
('Summer Breeze', 1, 2, 'Indie Pop', 180, '2024-06-20'),
('Autumn Leaves', 1, 2, 'Indie Pop', 220, '2024-06-20');

INSERT INTO podcasts (title, host_name, description, genre) VALUES
('Tech Talks', 'Alex Johnson', 'Weekly technology discussions', 'Technology'),
('Music History', 'Sarah Williams', 'Exploring the evolution of music', 'Music');

INSERT INTO podcast_episodes (podcast_id, title, description, duration_seconds, publish_date) VALUES
(1, 'AI Revolution', 'Discussing the impact of AI on society', 1800, '2024-01-10'),
(1, 'Blockchain Basics', 'Understanding blockchain technology', 1500, '2024-01-17'),
(2, 'The Beatles Legacy', 'How The Beatles changed music forever', 2400, '2024-02-01');

INSERT INTO playlists (user_id, name, description, is_public) VALUES
(1, 'My Favorites', 'Collection of my favorite songs', FALSE),
(1, 'Chill Vibes', 'Relaxing songs for unwinding', TRUE),
(3, 'Workout Mix', 'High energy songs for exercise', FALSE);

INSERT INTO playlist_songs (playlist_id, song_id, position_order) VALUES
(1, 1, 1),
(1, 3, 2),
(2, 2, 1),
(2, 4, 2),
(3, 1, 1),
(3, 3, 2);

INSERT INTO favorites (user_id, song_id) VALUES
(1, 1),
(1, 3),
(3, 2),
(3, 4);

INSERT INTO listening_history (user_id, song_id, duration_played_seconds) VALUES
(1, 1, 210),
(1, 3, 180),
(1, 2, 195),
(3, 2, 195),
(3, 4, 220);
