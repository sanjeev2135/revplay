package com.revplay.model;

public class Playlist {
    private long playlistId;
    private long userId;
    private String name;
    private String description;
    private boolean isPublic;
    private java.sql.Timestamp createdAt;
    private java.sql.Timestamp updatedAt;

    public Playlist() {}

    public Playlist(long userId, String name, String description, String privacy) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.isPublic = "PUBLIC".equals(privacy);
    }

    // Getters and setters
    public long getPlaylistId() { return playlistId; }
    public void setPlaylistId(long playlistId) { this.playlistId = playlistId; }

    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }

    public long getOwnerId() { return userId; } // For backward compatibility
    public void setOwnerId(long ownerId) { this.userId = ownerId; } // For backward compatibility

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPrivacy() { return isPublic ? "PUBLIC" : "PRIVATE"; }
    public void setPrivacy(String privacy) { this.isPublic = "PUBLIC".equals(privacy); }

    public boolean isPublic() { return isPublic; }
    public void setPublic(boolean isPublic) { this.isPublic = isPublic; }

    public java.sql.Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(java.sql.Timestamp createdAt) { this.createdAt = createdAt; }

    public java.sql.Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(java.sql.Timestamp updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "Playlist{id=" + playlistId + ", name='" + name + "', userId=" + userId + "}";
    }
}
