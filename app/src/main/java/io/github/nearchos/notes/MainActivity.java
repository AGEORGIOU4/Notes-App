package io.github.nearchos.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.List;

import io.github.nearchos.notes.db.NotesRoomDatabase;

public class MainActivity extends AppCompatActivity {

    Button deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create a List of Notes to populate the recycler view
        List<Note> notesList = new ArrayList<>();

        // Get Recycler from activity_main and set parameters
        RecyclerView recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize each note from the db to the notesList
        for (int i = 0; i <= getAllNotesSortedByTimestamp().size() - 1; i++) {
            notesList.add(getAllNotesSortedByTimestamp().get(i));
        }
        RecyclerView.Adapter adapter = new MyAdapter(notesList, this);
        recyclerView.setAdapter(adapter);

    }


    List<Note> getAllNotesSortedByTimestamp() {
        return NotesRoomDatabase.getDatabase(this).notesDao().getNotesSortedByTimestamp();
    }

    public void refresh(View view) {
        finish();
        startActivity(getIntent());
    }

    public void addNoteActivity(View view) {
        Intent intent = new Intent(this, AddNote.class);
        startActivity(intent);
    }


}