package io.github.nearchos.notes.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.github.nearchos.notes.Note;

/**
 * The DAO interface used by Room persistence library to manage the notes database.
 */
@Dao
public interface NotesDao {

    @Insert
    void insert(Note... notes);

    @Update
    void update(Note note);

    @Query("SELECT * FROM notes ORDER BY title ASC")
    List<Note> getNotesSortedByTitle();

    @Query("SELECT * FROM notes ORDER BY timestamp DESC")
    List<Note> getNotesSortedByTimestamp();

    @Delete
    void delete(Note note);
}