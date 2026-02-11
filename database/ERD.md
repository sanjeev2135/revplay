# RevPlay Database ERD

## Entity Relationship Diagram

```mermaid
erDiagram
    users {
        bigint user_id PK
        varchar username UK
        varchar email UK
        varchar password_hash
        boolean is_artist
        varchar security_question
        varchar security_answer
        timestamp created_at
        timestamp updated_at
    }

    artists {
        bigint artist_id PK
        bigint user_id FK
        text bio
        varchar genre
        json social_media_links
        timestamp created_at
        timestamp updated_at
    }

    albums {
        bigint album_id PK
        bigint artist_id FK
        varchar title
        text description
        date release_date
        varchar cover_image_url
        timestamp created_at
        timestamp updated_at
    }

    songs {
        bigint song_id PK
        varchar title
        bigint artist_id FK
        bigint album_id FK
        varchar genre
        int duration_seconds
        varchar file_url
        bigint play_count
        date release_date
        timestamp created_at
        timestamp updated_at
    }

    podcasts {
        bigint podcast_id PK
        varchar title
        varchar host_name
        text description
        varchar genre
        varchar cover_image_url
        timestamp created_at
        timestamp updated_at
    }

    podcast_episodes {
        bigint episode_id PK
        bigint podcast_id FK
        varchar title
        text description
        int duration_seconds
        varchar file_url
        date publish_date
        bigint play_count
        timestamp created_at
    }

    playlists {
        bigint playlist_id PK
        bigint user_id FK
        varchar name
        text description
        boolean is_public
        timestamp created_at
        timestamp updated_at
    }

    playlist_songs {
        bigint playlist_id FK
        bigint song_id FK
        timestamp added_at
        int position_order
    }

    favorites {
        bigint favorite_id PK
        bigint user_id FK
        bigint song_id FK
        timestamp added_at
    }

    listening_history {
        bigint history_id PK
        bigint user_id FK
        bigint song_id FK
        timestamp played_at
        int duration_played_seconds
    }

    %% Relationships
    users ||--o| artists : "1:1 (if is_artist=true)"
    artists ||--o{ albums : "1:N"
    artists ||--o{ songs : "1:N"
    albums ||--o{ songs : "1:N (optional)"
    users ||--o{ playlists : "1:N"
    playlists ||--o{ playlist_songs : "1:N"
    songs ||--o{ playlist_songs : "M:N"
    users ||--o{ favorites : "1:N"
    songs ||--o{ favorites : "M:N"
    users ||--o{ listening_history : "1:N"
    songs ||--o{ listening_history : "M:N"
    podcasts ||--o{ podcast_episodes : "1:N"
```

## Database Relationships Summary

### Core Entities:
- **Users**: Base table for all users (listeners and artists)
- **Artists**: Extended profile for users who are artists
- **Songs**: Individual music tracks
- **Albums**: Collections of songs by artists
- **Podcasts**: Podcast shows
- **Podcast Episodes**: Individual episodes within podcasts
- **Playlists**: User-created song collections
- **Favorites**: User's favorite songs
- **Listening History**: Track user's listening activity

### Key Relationships:
1. **User-Artist**: 1:1 relationship (only users with is_artist=true have artist profiles)
2. **Artist-Song**: 1:N relationship (artists can have multiple songs)
3. **Artist-Album**: 1:N relationship (artists can have multiple albums)
4. **Album-Song**: 1:N relationship (albums contain multiple songs, optional for singles)
5. **User-Playlist**: 1:N relationship (users can create multiple playlists)
6. **Playlist-Song**: M:N relationship (playlists can contain multiple songs, songs can be in multiple playlists)
7. **User-Favorite**: M:N relationship (users can favorite multiple songs, songs can be favorited by multiple users)
8. **User-Listening History**: M:N relationship (users can listen to multiple songs, songs can be listened by multiple users)
9. **Podcast-Episode**: 1:N relationship (podcasts have multiple episodes)

### Constraints:
- All foreign key relationships have proper CASCADE/SET NULL rules
- Unique constraints on usernames, emails, and user-song favorite combinations
- Proper indexing for performance optimization
- Timestamp tracking for audit trails

### Data Integrity:
- Users must exist before creating artist profiles
- Artists must exist before creating songs/albums
- Songs can exist without albums (singles)
- Playlists must belong to existing users
- All junction tables maintain referential integrity
