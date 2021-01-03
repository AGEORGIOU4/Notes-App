package io.github.nearchos.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.github.nearchos.notes.db.NotesRoomDatabase;

public class AddNote extends AppCompatActivity {

    private EditText add_Title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        add_Title = findViewById(R.id.add_Title);

        // Set first letter as capital, auto focus on edit box and automatically open keyboard
        add_Title.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        add_Title.requestFocus();

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

    }

    void insert(Note note) {
        NotesRoomDatabase.getDatabase(this).notesDao().insert(note);
    }

    // Add note
    public void add(View view) {
        // Format current milliseconds in Date and Time
        long currentTimeInMillis = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        Date resultDate = new Date(currentTimeInMillis);

        if(add_Title.getText().toString().length() != 0) {
            Note testNote = new Note(add_Title.getText().toString(), sdf.format(resultDate), System.currentTimeMillis(), true);
            insert(testNote);
            Snackbar.make(view.getRootView(), "Added!", BaseTransientBottomBar.LENGTH_SHORT).show();
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(add_Title.getWindowToken(), 0);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Please Add a Title", Toast.LENGTH_SHORT).show();
        }
    }

    public void goBack(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}