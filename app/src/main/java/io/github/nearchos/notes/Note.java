package io.github.nearchos.notes;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Abstracting a 'note' entity.
 * Annotated with proper values to be stored and managed by the Room persistence library.
 */
@Entity(tableName = "notes")
public class Note {

    @PrimaryKey(autoGenerate = true) private int id;
    private final String title;
    private final String body;
    private final long timestamp;
    private final boolean starred;

    public Note(int id, String title, String body, long timestamp, boolean starred) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.timestamp = timestamp;
        this.starred = starred;
    }

    @Ignore
    public Note(String title, String body, long timestamp, boolean starred) {
        this.title = title;
        this.body = body;
        this.timestamp = timestamp;
        this.starred = starred;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean isStarred() {
        return starred;
    }
}