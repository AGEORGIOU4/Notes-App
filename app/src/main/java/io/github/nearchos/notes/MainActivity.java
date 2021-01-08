package io.github.nearchos.notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.Executors;

import io.github.nearchos.notes.db.NotesDao;
import io.github.nearchos.notes.db.NotesRoomDatabase;

public class MainActivity extends AppCompatActivity implements MyAdapter.OnItemClickListener, MyAdapter.OnItemLongClickListener,
        MyAdapter.OnStarClickListener, MyAdapter.OnEditClickListener {

    public Vector<Note> notesList = new Vector<>();
    int listItemPosition = -1;
    int listItemPositionOnStarEdit = -1;

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private CheckBox starredCheckBox;

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            // Create a temp note if user wants to undo
            Note tmpNote = notesList.get(viewHolder.getAdapterPosition());

            delete(notesList.get(viewHolder.getAdapterPosition()));
            // Clear the list and update it
        }
    };

    @Override
    protected void onResume() {
        // Clear the list and update it
        super.onResume();
        Executors.newSingleThreadExecutor().execute(() -> {
            final NotesDao myDAO = NotesRoomDatabase.getDatabase(this).notesDao();
            notesList.clear();
            notesList.addAll(myDAO.getNotesSortedByTimestamp());
            runOnUiThread(() -> adapter.notifyDataSetChanged());
        });
    }

    List<Note> getAllNotesSortedByTimestamp() {
        return NotesRoomDatabase.getDatabase(this).notesDao().getNotesSortedByTimestamp();
    }

    public void startAddNoteActivity(View v) {
        finish();
        overridePendingTransition(0, 0);
        Intent intent = new Intent(MainActivity.this, AddNote.class);
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    // Show item body on a toast
    @Override
    public void itemClicked(View v, int pos, String value) {
        Snackbar.make(recyclerView, notesList.get(pos).getBody() + " (long press to delete)", Snackbar.LENGTH_SHORT).show();
    }

    // Delete Item
    @Override
    public boolean itemLongClicked(View v, int pos, String value) {
        // Create a temp note if user wants to undo
        Note tmpNote = notesList.get(pos);

        delete(notesList.get(pos));
        // Clear the list and update it
        Executors.newSingleThreadExecutor().execute(() -> {
            final NotesDao myDAO = NotesRoomDatabase.getDatabase(this).notesDao();
            notesList.clear();
            notesList.addAll(myDAO.getNotesSortedByTimestamp());
            runOnUiThread(() -> adapter.notifyDataSetChanged());
        });
        Snackbar snackbar = Snackbar.make(v, "Do you want to delete " + tmpNote.getTitle() + " ?", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Undo
                        insert(tmpNote);
                        Executors.newSingleThreadExecutor().execute(() -> {
                            final NotesDao myDAO = NotesRoomDatabase.getDatabase(getApplicationContext()).notesDao();
                            notesList.clear();
                            notesList.addAll(myDAO.getNotesSortedByTimestamp());
                            runOnUiThread(() -> adapter.notifyDataSetChanged());
                        });
                    }
                });
        snackbar.show();
        return true;
    }

    void update(Note note) {
        NotesRoomDatabase.getDatabase(this).notesDao().update(note);
    }

    void insert(Note note) {
        NotesRoomDatabase.getDatabase(this).notesDao().insert(note);
    }

    void delete(final Note note) {
        NotesRoomDatabase.getDatabase(this).notesDao().delete(note);
    }

    public void getEditNoteData() {
        String editedText;
        String editedBody;
        Boolean editedStarred;
        Long editedTimestamp;

        Intent intent = getIntent();

        editedText = intent.getStringExtra("edited_Title");
        editedBody = intent.getStringExtra("edited_Body");
        editedTimestamp = intent.getLongExtra("edited_Timestamp", 0L);
        editedStarred = intent.getBooleanExtra("edited_Starred", false);
        listItemPosition = intent.getIntExtra("clicked_item_position", -1);

        if (listItemPosition >= 0) {
            notesList.get(listItemPosition).setTitle(editedText);
            notesList.get(listItemPosition).setBody(editedBody);
            notesList.get(listItemPosition).setTimestamp(editedTimestamp);
            notesList.get(listItemPosition).setStarred(editedStarred);
            update(notesList.get(listItemPosition));
            listItemPosition = -1;
        }
    }

    @Override
    public void itemClicked(View v, int pos, boolean checked) {
        boolean check = notesList.get(pos).isStarred();

        listItemPositionOnStarEdit = pos;

        if (listItemPositionOnStarEdit >= 0) {
            check = !check;
            System.out.println("new check is " + check);
            notesList.get(listItemPositionOnStarEdit).setStarred(check);
            update(notesList.get(listItemPositionOnStarEdit));

            Executors.newSingleThreadExecutor().execute(() -> {
                final NotesDao myDAO = NotesRoomDatabase.getDatabase(getApplicationContext()).notesDao();
                notesList.clear();
                notesList.addAll(myDAO.getNotesSortedByTimestamp());
                runOnUiThread(() -> adapter.notifyDataSetChanged());
            });
            listItemPositionOnStarEdit = -1;
        }
    }

    // Pass values to Edit Note Activity
    @Override
    public void itemClicked(View v, int pos, String title, String body, boolean checked, long timestamp) {
        Intent editNoteIntent = new Intent(this, EditNote.class);
        editNoteIntent.putExtra("clicked_item_position", pos);
        editNoteIntent.putExtra("clicked_item_title", title);
        editNoteIntent.putExtra("clicked_item_body", body);
        editNoteIntent.putExtra("clicked_item_timestamp", timestamp);
        editNoteIntent.putExtra("clicked_item_starred", checked);

        finish();
        overridePendingTransition(0, 0);
        startActivity(editNoteIntent);
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        new ItemTouchHelper((itemTouchHelperCallback)).attachToRecyclerView(recyclerView);

        // Initialize each note from the db to the notesList
        for (int i = 0; i <= getAllNotesSortedByTimestamp().size() - 1; i++) {
            notesList.add(getAllNotesSortedByTimestamp().get(i));
        }

        // Get Recycler from activity_main and set parameters
        recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new MyAdapter(notesList, this, this, this::itemClicked, this::itemClicked);
        recyclerView.setAdapter(adapter);

        getEditNoteData();
        //getStarNoteData();
    }
}