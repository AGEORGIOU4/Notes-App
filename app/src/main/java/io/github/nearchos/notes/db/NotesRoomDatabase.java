package io.github.nearchos.notes.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.Executors;

import io.github.nearchos.notes.Note;

/**
 * The database used by Room persistence library to manage the notes database.
 */
@Database(entities = {Note.class}, version = 1)
public abstract class NotesRoomDatabase extends RoomDatabase {

    public abstract NotesDao notesDao();

    private static volatile NotesRoomDatabase INSTANCE;

    public static NotesRoomDatabase getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (NotesRoomDatabase.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            NotesRoomDatabase.class, "notes_database")
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    Executors.newSingleThreadScheduledExecutor().execute(() -> getDatabase(context).notesDao().insert(INITIAL_NOTES));
                                }
                            })
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final Note [] INITIAL_NOTES = new Note[]{
            new Note("A test note", "This is the body.\nIt can have multiple lines.", System.currentTimeMillis(), false),
            new Note("Another test", "This is the body again.\nIt can a second line.\nAnd a third.", System.currentTimeMillis(), true)
    };
}