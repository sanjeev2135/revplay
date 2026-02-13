# RevPlay Music Player - Test Documentation

## Overview
This document explains the test scenarios and examples for the RevPlay Music Player application. The tests cover core functionality including user authentication, music playback, pause/resume functionality, and user session management.

## Test Categories

### 1. User Authentication Tests

#### Test Case: Login Functionality
**Purpose**: Verify users can log in with valid credentials
**Example**:
```
Input:
- Username: "san"
- Password: "san,."

Expected Output:
- "? Login successful! Welcome, san!"
- User menu appears with user's name displayed
```

#### Test Case: Invalid Login
**Purpose**: Verify system rejects invalid credentials
**Example**:
```
Input:
- Username: "invalid"
- Password: "wrong"

Expected Output:
- "❌ Invalid username or password!"
- Returns to authentication menu
```

### 2. Music Playback Tests

#### Test Case: Song Search and Play
**Purpose**: Verify users can search and play songs
**Example**:
```
Input:
- Search term: "s"
- Select song: 2 (New Day by Josh)

Expected Output:
- "NOW PLAYING: New Day"
- "Artist: Josh"
- "Duration: 3:50"
- Progress bar starts counting: "Elapsed: 0:01 / 3:50"
```

#### Test Case: Progress Tracking
**Purpose**: Verify progress bar updates correctly
**Example**:
```
Expected Behavior:
- Progress updates every second
- Format: "Elapsed: 0:05 / 3:50"
- Continues until song ends or user pauses
```

### 3. Pause/Resume Tests (CRITICAL)

#### Test Case: Basic Pause/Resume Cycle
**Purpose**: Verify pause and resume work correctly
**Example**:
```
Step 1: Play song until 0:08
Step 2: Press Enter to pause
Expected: "⏸️ Paused: New Day"

Step 3: Choose Resume (option 1)
Expected: 
- "▶️ Resumed: New Day"
- "⏱️ Resuming from: 0:08 / 3:50"
- Progress continues from 0:09, 0:10, etc.
```

#### Test Case: Multiple Pause/Resume Cycles
**Purpose**: Verify multiple pause/resume cycles work
**Example**:
```
Cycle 1: Pause at 0:05 → Resume → Continues from 0:06
Cycle 2: Pause at 0:15 → Resume → Continues from 0:16
Cycle 3: Pause at 0:25 → Resume → Continues from 0:27

Expected: Each cycle continues from exact pause point
```

#### Test Case: Resume Time Display
**Purpose**: Verify resume shows correct elapsed time
**Example**:
```
Pause at: 0:12
Resume message: "⏱️ Resuming from: 0:12 / 3:50"
Expected: Progress bar continues from 0:13
```

### 4. User Session Management Tests

#### Test Case: Song State Isolation
**Purpose**: Verify different users don't share song states
**Example**:
```
User A (san):
- Plays "New Day" and pauses at 0:10
- Logs out

User B (josh):
- Logs in
- Expected: No song playing, clean state
- Plays different song: "Diary of Jane"

Expected: User B doesn't see User A's paused song
```

#### Test Case: Logout Clears State
**Purpose**: Verify logout clears music player state
**Example**:
```
User plays song, pauses at 0:15
User logs out
Expected:
- MusicPlayerService.currentSong = null
- currentElapsedSeconds = 0
- Next user starts with clean state
```

### 5. Menu Navigation Tests

#### Test Case: Menu Options Update
**Purpose**: Verify menu options change based on playback state
**Example**:
```
No song playing:
- "1. Search & Play Song"
- "2. List Songs by Artist"
- "3. Back"

Song playing:
- "🎵 Now Playing: New Day"
- "1. Pause" (when playing)
- "1. Resume" (when paused)
- "2. Stop"
```

### 6. Edge Cases and Error Handling

#### Test Case: Invalid Song Selection
**Purpose**: Verify system handles invalid song numbers
**Example**:
```
Search results show 5 songs
Input: 99 (invalid)
Expected: "No songs found" or returns to menu
```

#### Test Case: Empty Search Results
**Purpose**: Verify system handles no search results
**Example**:
```
Search term: "nonexistent"
Expected: "No songs found for 'nonexistent'"
```

#### Test Case: Interrupt During Playback
**Purpose**: Verify system handles interruptions gracefully
**Example**:
```
During playback:
- Press Ctrl+C or interrupt
Expected: "⏹️ Playback interrupted"
- Clean state reset
```

## Test Scenarios Matrix

| Test ID | Scenario | Expected Result | Priority |
|---------|----------|------------------|----------|
| AUTH-01 | Valid login | Success message + user menu | High |
| AUTH-02 | Invalid login | Error message | High |
| PLAY-01 | Search and play song | Song starts playing | High |
| PLAY-02 | Progress tracking | Time updates every second | High |
| PAUSE-01 | Single pause/resume | Continues from pause point | Critical |
| PAUSE-02 | Multiple pause/resume cycles | Each cycle works correctly | Critical |
| PAUSE-03 | Resume time display | Shows correct elapsed time | High |
| SESSION-01 | User isolation | No state sharing between users | Critical |
| SESSION-02 | Logout clears state | Clean state for next user | Critical |
| MENU-01 | Dynamic menu options | Options change based on state | Medium |
| ERROR-01 | Invalid inputs | Graceful error handling | Medium |

## Automated Test Examples

### SimplePauseResumeTest.java (Deleted)
```java
// Purpose: Test basic pause/resume functionality
@Test
public void testPauseResumeContinuity() {
    // Play song for 5 seconds
    // Pause at 0:05
    // Resume
    // Verify progress continues from 0:06
}
```

### MusicPlayerServiceTest.java
```java
// Purpose: Test music player service methods
@Test
public void testPlaySong() {
    // Verify song starts playing
    // Verify currentSong is set
}

@Test
public void testPauseResume() {
    // Verify pause sets isPlaying = false
    // Verify resume sets isPlaying = true
}
```

## Manual Testing Checklist

### Before Each Test:
- [ ] Application starts successfully
- [ ] Database connection is working
- [ ] No previous session data

### During Each Test:
- [ ] Input is accepted correctly
- [ ] Output matches expected format
- [ ] No error messages unless expected
- [ ] Progress updates correctly

### After Each Test:
- [ ] State is clean for next test
- [ ] No memory leaks or hanging threads
- [ ] Logs show expected behavior

## Performance Tests

### Test Case: Large Song Library
**Purpose**: Verify performance with many songs
**Example**:
```
Scenario: 1000+ songs in library
Search for common term like "the"
Expected: Search completes within 2 seconds
Results display correctly
```

### Test Case: Long Playback Session
**Purpose**: Verify stability during long sessions
**Example**:
```
Scenario: Play 10+ songs with multiple pause/resume cycles
Expected: No memory leaks
Progress tracking remains accurate
UI remains responsive
```

## Integration Tests

### Test Case: End-to-End User Journey
**Purpose**: Verify complete user workflow
**Example**:
```
1. User logs in
2. Searches for songs
3. Plays song with multiple pause/resume
4. Logs out
5. Different user logs in
6. Verifies clean state
```

## Test Data

### Sample Users:
- Username: "san", Password: "san,." (Artist Account)
- Username: "josh", Password: "josh" (User Account)

### Sample Songs:
- "Diary of Jane" by san [3:40]
- "New Day" by Josh [3:50]
- "bye" by asdd [4:10]
- "hello" by asdd [3:20]
- "main hoon" by asdf [4:10]

## Conclusion

These tests ensure the RevPlay Music Player provides a reliable, user-friendly experience with proper pause/resume functionality, user session isolation, and robust error handling. The critical focus is on the pause/resume mechanism working seamlessly across multiple cycles while maintaining accurate progress tracking.
