# RevPlay Music Streaming Application

## Overview

RevPlay is a console-based music streaming application built with **Java 25**, **Oracle 26ai**, and **PL/SQL stored procedures**. It provides comprehensive music streaming functionality including user management, real-time music playback simulation with progress bars, playlist management, and artist content management.

## Features

### User Features
- **User Registration & Login**: Create accounts with automatic artist profile setup
- **Music Library**: Search songs by keywords, browse by genre
- **Favorites**: Mark songs as favorites and manage favorite lists
- **Playlists**: Create, update, delete private/public playlists
- **Music Player**: Real-time playback simulation with progress bar and timer
- **Listening History**: Track recently played songs and full listening history
- **Podcasts**: Browse and play podcast episodes

### Artist Features
- **Automatic Artist Profile**: Created on registration with default album
- **Music Management**: Upload songs to default album
- **Statistics**: View play counts and total songs uploaded
- **Social Links**: Add social media links during registration

## Architecture

### Technology Stack
- **Backend**: Java 25 (JDK 25)
- **Database**: Oracle 26ai Free Edition
- **JDBC Driver**: Oracle JDBC Thin Driver (ojdbc11)
- **Database Access**: PL/SQL Stored Procedures
- **Logging**: Log4j2
- **Build Tool**: Maven

### Project Structure
```
src/main/java/com/revplay/
├── Main.java                 # Application entry point
├── AppConfig.java           # Application configuration
├── model/                   # Data models
│   ├── User.java
│   ├── Artist.java
│   ├── Song.java
│   ├── Album.java
│   ├── Playlist.java
│   ├── Podcast.java
│   ├── PodcastEpisode.java
│   ├── Favorite.java
│   └── ListeningHistory.java
├── dao/                     # Data Access Objects
│   ├── ConnectionFactory.java
│   ├── UserDao.java
│   ├── ArtistDao.java
│   ├── SongDao.java
│   ├── AlbumDao.java
│   ├── PlaylistDao.java
│   ├── FavoriteDao.java
│   ├── ListeningHistoryDao.java
│   └── PodcastDao.java
├── service/                 # Business logic layer
│   ├── UserService.java
│   ├── ArtistService.java
│   ├── SongService.java
│   ├── PlaylistService.java
│   ├── MusicPlayerService.java
│   ├── FavoriteService.java
│   ├── ListeningHistoryService.java
│   ├── PodcastService.java
│   └── StatsService.java
└── console/                 # User interface layer
    ├── ConsoleMenu.java
    ├── AuthMenu.java
    ├── MusicLibraryMenu.java
    ├── MusicPlayerMenu.java
    ├── PlaylistMenu.java
    └── ArtistMenu.java
```

### Database Design

The application uses Oracle with PL/SQL stored procedures:

- **Users**: Base user table (C##revplay schema)
- **Artists**: Extended profile with bio, genre, social links
- **Songs**: Music tracks with artist/album references
- **Albums**: Collections of songs (auto-created default album for artists)
- **Playlists**: User-created song collections
- **Favorites**: User's favorite songs
- **Listening History**: Track user listening activity
- **Sequences & Triggers**: Auto-increment primary keys (Oracle-style)

See `database/oracle_schema.sql` for complete schema and `ER_DIAGRAM.md` for entity relationships.

## Installation & Setup

### Prerequisites
- Java Development Kit (JDK) 25 or higher
- Apache Maven
- Oracle 26ai Free Edition (or Oracle XE)

### Oracle Database Setup

1. **Create the database user** (as SYS or SYSTEM):
```sql
CREATE USER C##revplay IDENTIFIED BY revplay123;
GRANT CREATE SESSION TO C##revplay;
GRANT CREATE TABLE TO C##revplay;
GRANT CREATE SEQUENCE TO C##revplay;
GRANT CREATE TRIGGER TO C##revplay;
GRANT CREATE PROCEDURE TO C##revplay;
GRANT UNLIMITED TABLESPACE TO C##revplay;
```

2. **Connect as C##revplay and run the schema**:
```sql
-- In SQL Developer or SQL*Plus:
-- Connect as C##revplay/revplay123@//localhost:1521/FREE

-- Run: database/oracle_schema.sql
```

3. **Verify connection settings** in `src/main/java/com/revplay/dao/ConnectionFactory.java`:
```java
private static final String URL = "jdbc:oracle:thin:@//localhost:1521/FREE";
private static final String USER = "C##revplay";
private static final String PASS = "revplay123";
```

### Build & Run
1. Clone the repository
2. Navigate to project directory
3. Build the project:
```bash
mvn clean compile
```

4. Run the application:
```bash
mvn exec:java -Dexec.mainClass="com.revplay.Main"
```

### Running Tests
```bash
mvn test
```

## Usage Guide

### Starting the Application
1. Run the main class `com.revplay.Main`
2. Choose **Register** or **Login**
3. If registering as **Artist**: Profile and default album auto-created!

### Main Menu Options
- **Create Playlist**: Make private or public playlists
- **Play Music**: Browse library, search songs, play with timer
- **Artist Profile**: (Artists only) View stats, upload songs
- **Logout**: Return to auth menu

### Music Player - Real-Time Playback

When you play a song, you'll see:

```
🎵 NOW PLAYING: Song Title
   Artist: Artist Name
   Duration: 3:45

   [Press ENTER to stop during playback]

   ████████████████░░░░░░░░░░░░░░░░░░░░░░░░ 40% [1:30 / 3:45]
```

**Controls During Playback:**
- **Wait for song to finish** → Auto-returns to menu
- **Press ENTER** → Stop immediately
- **Select menu option** → Invalid choice stops playback

**Music Menu Options:**
1. **Play Song** - Enter song ID, watch real-time progress
2. **Pause/Resume** - Control playback
3. **Skip/Previous** - Navigate playlist
4. **Toggle Repeat** - Loop current song/playlist
5. **Toggle Favorite** - Add/remove from favorites
6. **Stop** - End playback immediately

### Artist Registration Flow

1. Register as **Artist** (is_artist = Y)
2. System automatically:
   - Creates user account
   - Creates artist profile with bio/genre
   - Creates default album "Default Album"
3. **Upload songs immediately** - No setup needed!

## API Documentation

### Service Layer

#### UserService
- `register(User)`: Register new user
- `login(String, String)`: Authenticate user
- `updatePassword(long, String)`: Update user password
- `recoverPassword(String, String, String)`: Password recovery

#### SongService
- `searchSongs(String)`: Search songs by keyword
- `getSongsByGenre(String)`: Get songs by genre
- `getSongById(long)`: Get song details
- `getAllSongs()`: Get all songs

#### PlaylistService
- `createPlaylist(Playlist)`: Create new playlist
- `addSongToPlaylist(long, long)`: Add song to playlist
- `getUserPlaylists(long)`: Get user's playlists
- `getPublicPlaylists()`: Get all public playlists

#### MusicPlayerService
- `playSong(long, long)`: Play song for user
- `pause()`: Pause current playback
- `skip()`: Skip to next track
- `toggleRepeat()`: Toggle repeat mode
- `toggleFavorite()`: Toggle favorite status

## Testing

The application includes comprehensive unit tests using JUnit 5 and Mockito:

- **UserServiceTest**: User management functionality
- **SongServiceTest**: Song search and management
- **PlaylistServiceTest**: Playlist operations
- **MusicPlayerServiceTest**: Music player controls

Run tests with:
```bash
mvn test
```

## Future Enhancements

### Planned Features
- Web-based interface using Spring Boot
- Mobile application support
- Real-time music streaming
- Social features (following, sharing)
- Advanced recommendation algorithms
- Audio file upload and processing
- Payment integration for premium features

### Scalability Improvements
- Microservices architecture
- Caching with Redis
- Load balancing
- Database sharding
- Cloud deployment

## Security Considerations

- Password hashing (implementation ready for BCrypt)
- SQL injection prevention via prepared statements
- Input validation in console interface
- Role-based access control
- Security question-based password recovery

## Contributing

1. Fork the repository
2. Create feature branch
3. Make changes with appropriate tests
4. Run test suite
5. Submit pull request

## License

This project is for educational purposes. See LICENSE file for details.

## Support

For questions or issues, please create an issue in the repository or contact the development team.

---

**RevPlay** - Your Console-Based Music Streaming Solution
