# RevPlay Application Architecture

## Architecture Overview

RevPlay follows a **layered (n-tier) architecture pattern** with clear separation of concerns. This design enables maintainability, scalability, and future extensibility to microservices.

```
┌─────────────────────────────────────────────────────────────┐
│                    Presentation Layer                        │
│                  (Console Interface)                        │
├─────────────────────────────────────────────────────────────┤
│                     Business Layer                          │
│                   (Service Classes)                          │
├─────────────────────────────────────────────────────────────┤
│                    Data Access Layer                         │
│                    (DAO Classes)                             │
├─────────────────────────────────────────────────────────────┤
│                    Database Layer                             │
│                     (MySQL)                                  │
└─────────────────────────────────────────────────────────────┘
```

## Layer Responsibilities

### 1. Presentation Layer (Console)
**Location**: `com.revplay.console`

**Responsibilities**:
- User interface and interaction
- Input validation and sanitization
- Navigation between application features
- Display formatting and user feedback

**Key Components**:
- `ConsoleMenu`: Main application controller
- `AuthMenu`: User authentication interface
- `MusicLibraryMenu`: Music browsing and search
- `MusicPlayerMenu`: Playback controls
- `PlaylistMenu`: Playlist management
- `ArtistMenu`: Artist-specific features

### 2. Business Layer (Services)
**Location**: `com.revplay.service`

**Responsibilities**:
- Business logic implementation
- Transaction management
- Cross-cutting concerns (logging, validation)
- Coordination between DAOs
- Business rule enforcement

**Key Components**:
- `UserService`: User management and authentication
- `SongService`: Music catalog operations
- `PlaylistService`: Playlist CRUD operations
- `MusicPlayerService`: Playback simulation
- `FavoriteService`: User favorites management
- `ListeningHistoryService`: Play tracking
- `PodcastService`: Podcast management

### 3. Data Access Layer (DAOs)
**Location**: `com.revplay.dao`

**Responsibilities**:
- Database communication
- SQL query execution
- Result set mapping to objects
- Connection management
- Data persistence operations

**Key Components**:
- `UserDao`: User data operations
- `SongDao`: Music catalog data access
- `PlaylistDao`: Playlist data management
- `FavoriteDao`: Favorites persistence
- `ListeningHistoryDao`: History tracking
- `PodcastDao`: Podcast data access
- `ConnectionFactory`: Database connection management

### 4. Database Layer
**Technology**: MySQL 8.4

**Responsibilities**:
- Data persistence
- Transaction management
- Referential integrity
- Indexing and performance optimization

## Design Patterns Used

### 1. Data Access Object (DAO) Pattern
Separates data access logic from business logic:
```java
// Service Layer
public class SongService {
    private final SongDao songDao = new SongDao();
    
    public List<Song> searchSongs(String keyword) {
        return songDao.searchSongs(keyword);
    }
}

// DAO Layer
public class SongDao {
    public List<Song> searchSongs(String keyword) throws SQLException {
        // Database query implementation
    }
}
```

### 2. Service Layer Pattern
Encapsulates business logic and provides transaction boundaries:
```java
public class PlaylistService {
    public boolean addSongToPlaylist(long playlistId, long songId) {
        try {
            // Business validation
            // DAO operations
            // Logging
            return true;
        } catch (Exception e) {
            // Error handling
            return false;
        }
    }
}
```

### 3. Factory Pattern (Connection Factory)
Centralized database connection management:
```java
public class ConnectionFactory {
    private static final String URL = "jdbc:mysql://localhost:3306/revplay_db";
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
```

### 4. Model/View/Controller (MVC) Pattern
- **Models**: `com.revplay.model` package
- **Views**: Console menu classes
- **Controllers**: Service classes coordinate between models and views

## Data Flow Architecture

### User Registration Flow
```
ConsoleMenu → AuthMenu → UserService → UserDao → Database
     ↓              ↓           ↓          ↓         ↓
   Display    Collect     Validate    Persist   Store
   Results     Input       Logic      Data     Data
```

### Music Playback Flow
```
MusicPlayerMenu → MusicPlayerService → SongDao → Database
       ↓                ↓                 ↓         ↓
   User Input      Business Logic     Get Song  Update
   Controls        Play Simulation    Details   Play Count
```

## Scalability Considerations

### Current Architecture Strengths
1. **Loose Coupling**: Layers communicate through well-defined interfaces
2. **High Cohesion**: Each class has a single responsibility
3. **Testability**: Easy to mock dependencies for unit testing
4. **Maintainability**: Clear separation makes modifications easier

### Future Microservices Migration Path

The current layered architecture can be evolved into microservices:

```
┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐
│   User Service  │  │  Music Service  │  │ Playlist Service│
│                 │  │                 │  │                 │
│ - Auth          │  │ - Catalog       │  │ - Management    │
│ - Profiles      │  │ - Search        │  │ - Sharing      │
└─────────────────┘  └─────────────────┘  └─────────────────┘
         │                     │                     │
         └─────────────────────┼─────────────────────┘
                               │
                    ┌─────────────────┐
                    │  API Gateway    │
                    │                 │
                    │ - Routing       │
                    │ - Auth          │
                    │ - Load Balance  │
                    └─────────────────┘
```

### Migration Strategy
1. **Phase 1**: Extract service boundaries
2. **Phase 2**: Implement REST APIs
3. **Phase 3**: Deploy as separate services
4. **Phase 4**: Add service discovery and load balancing

## Technology Stack Rationale

### Java 25
- **Mature ecosystem**: Extensive libraries and frameworks
- **Performance**: Optimized JVM for enterprise applications
- **Cross-platform**: Run anywhere JVM is available
- **Strong typing**: Compile-time error detection

### MySQL
- **Reliability**: Proven track record in production
- **ACID compliance**: Transaction safety
- **Scalability**: Horizontal scaling capabilities
- **Rich feature set**: JSON support, window functions, etc.

### JDBC
- **Standardized**: Database-agnostic API
- **Performance**: Direct database connection
- **Control**: Fine-grained SQL control
- **Maturity**: Stable and well-documented

### Log4j2
- **Performance**: Asynchronous logging
- **Flexibility**: Multiple output destinations
- **Configuration**: XML/JSON/YAML configuration options
- **Integration**: Works well with Java ecosystem

## Security Architecture

### Authentication & Authorization
```
User Input → Validation → Hashing → Database Storage
     ↓            ↓          ↓           ↓
  Console    Input      Password    Secure
  Interface  Sanitize   Hashing     Storage
```

### Data Protection
- **Prepared Statements**: SQL injection prevention
- **Password Hashing**: Plaintext passwords never stored
- **Input Validation**: Console input sanitization
- **Role-based Access**: Artist vs. user permissions

## Performance Optimizations

### Database Level
- **Indexing**: Strategic indexes on frequently queried columns
- **Connection Pooling**: (Ready for HikariCP implementation)
- **Query Optimization**: Efficient SQL queries
- **Caching Strategy**: (Ready for Redis implementation)

### Application Level
- **Lazy Loading**: Load data only when needed
- **Batch Operations**: Process multiple records together
- **Memory Management**: Proper resource cleanup
- **Asynchronous Operations**: Non-blocking I/O (future enhancement)

## Monitoring & Observability

### Current Implementation
- **Logging**: Comprehensive application logging with Log4j2
- **Error Tracking**: Exception logging and stack traces
- **Performance Metrics**: Method timing and database query logging

### Future Enhancements
- **Metrics Collection**: Micrometer integration
- **Health Checks**: Service health endpoints
- **Distributed Tracing**: Request flow tracking
- **APM Integration**: Application performance monitoring

## Deployment Architecture

### Current Setup
```
┌─────────────────────────────────────┐
│           Single Instance           │
│  ┌─────────────┐ ┌─────────────┐   │
│  │   Java App  │ │   MySQL     │   │
│  │             │ │  Database   │   │
│  └─────────────┘ └─────────────┘   │
└─────────────────────────────────────┘
```

### Production Ready Architecture
```
┌─────────────────────────────────────────────────────────────┐
│                    Load Balancer                             │
└─────────────────────┬───────────────────────────────────────┘
                      │
        ┌─────────────┴─────────────┐
        │                           │
┌───────▼────────┐          ┌───────▼────────┐
│  App Instance  │          │  App Instance  │
│      #1        │          │      #2        │
└───────┬────────┘          └───────┬────────┘
        │                           │
        └─────────────┬─────────────┘
                      │
              ┌───────▼────────┐
              │   MySQL        │
              │   Cluster      │
              └────────────────┘
```

## Conclusion

The RevPlay architecture provides a solid foundation for a music streaming application with:

- **Maintainability**: Clear separation of concerns
- **Scalability**: Ready for microservices migration
- **Testability**: Comprehensive unit test coverage
- **Security**: Built-in security best practices
- **Performance**: Optimized database and application design

This architecture supports the current console-based implementation while providing a clear path for future enhancements including web interfaces, mobile apps, and distributed microservices deployment.
