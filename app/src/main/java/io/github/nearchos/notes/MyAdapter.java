package io.github.nearchos.notes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import io.github.nearchos.notes.db.NotesRoomDatabase;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<Note> listItems;
    public Context context;

    public MyAdapter(List<Note> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note listItem = listItems.get(position);

        holder.textViewHead.setText(listItem.getTitle());
        holder.textViewDesc.setText(listItem.getBody());

        // Move to edit activity when click on a list item
        holder.linearLayout.setOnClickListener(v -> {
            String title = listItem.getTitle();
            System.out.println(title);
            Intent intent = new Intent(v.getContext(), UpdateNote.class);
            context.startActivity(intent);
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Note tmpNote = new Note(listItem.getTitle(), listItem.getBody(), listItem.getTimestamp(), listItem.isStarred());

                Snackbar snackbar = Snackbar.make(v, "Do you want to delete " + listItem.getTitle() + " ?", Snackbar.LENGTH_LONG)
                        .setAction("DELETE", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(v.getContext(), "Deleted ", Toast.LENGTH_SHORT).show();
                                delete(listItem);
                                Intent intent = new Intent(v.getContext(), MainActivity.class);
                                context.startActivity(intent);
                            }
                        });
                snackbar.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewHead;
        public TextView textViewDesc;
        public LinearLayout linearLayout;
        public Button deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewHead = itemView.findViewById(R.id.textViewHead);
            textViewDesc = itemView.findViewById(R.id.textViewDesc);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }

    void delete(final Note note) {
        NotesRoomDatabase.getDatabase(context).notesDao().delete(note);
    }
}



