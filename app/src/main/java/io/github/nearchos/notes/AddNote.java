package io.github.nearchos.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Executors;

import io.github.nearchos.notes.db.NotesDao;
import io.github.nearchos.notes.db.NotesRoomDatabase;

public class AddNote extends AppCompatActivity {

    EditText add_Title;
    EditText add_Body;
    CheckBox add_Starred;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        add_Title = findViewById(R.id.add_Title);
        add_Body = findViewById(R.id.add_Body);
        add_Starred = findViewById(R.id.add_Starred);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void goBack(View v) {
        finish();
        overridePendingTransition(0, 0);
        Intent intent = new Intent(AddNote.this, MainActivity.class);
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    void insert(Note note) {
        NotesRoomDatabase.getDatabase(this).notesDao().insert(note);
    }

    // Add note
    public void addItem(View view) {
        add_Title = findViewById(R.id.add_Title);
        add_Body = findViewById(R.id.add_Body);
        add_Starred = findViewById(R.id.add_Starred);

        if (add_Title.getText().toString().matches("")) {
            Toast.makeText(this, "Enter Title", Toast.LENGTH_SHORT).show();
        } else {
            Note testNote = new Note(add_Title.getText().toString(), add_Body.getText().toString(), System.currentTimeMillis(), add_Starred.isChecked());
            insert(testNote);

            finish();
            Intent mainActivityIntent = new Intent(this, MainActivity.class);
            startActivity(mainActivityIntent);
        }
    }
}