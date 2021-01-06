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

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String body;
    private long timestamp;
    private boolean starred;

    public Note(int id, String title, String body, long timestamp, boolean starred) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.timestamp = timestamp;
        this.starred = starred;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }
}