package io.github.nearchos.notes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        // each data item has a 2 text views, a bool and a delete button
        public TextView textViewTitle;
        public TextView textViewBody;
        public TextView textViewTimestamp;
        public CheckBox starredCheckBox;
        public Button editNoteBtn;

        public ViewHolder(View itemView) {
            super(itemView);

            this.textViewTitle = itemView.findViewById(R.id.textViewTitle);
            this.textViewBody = itemView.findViewById(R.id.textViewBody);
            this.textViewTimestamp = itemView.findViewById(R.id.textViewTimestamp);
            this.starredCheckBox = itemView.findViewById(R.id.starredCheckBox);
            this.editNoteBtn = itemView.findViewById(R.id.editNoteBtn);
        }
    }

    private Vector<Note> listItems;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private OnStarClickListener onStarClickListener;
    private OnEditClickListener onEditClickListener;

    public MyAdapter(final Vector<Note> listItems, OnItemClickListener onItemClickListener, OnItemLongClickListener onItemLongClickListener,
                     OnStarClickListener onStarClickListener, OnEditClickListener onEditClickListener) {
        this.listItems = listItems;
        this.onItemClickListener = onItemClickListener;
        this.onItemLongClickListener = onItemLongClickListener;
        this.onStarClickListener = onStarClickListener;
        this.onEditClickListener = onEditClickListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Note listItem = listItems.get(position);

        //Convert System.currentTimeMillis to Date
        long noteMilliseconds = listItem.getTimestamp();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        Date resultDate = new Date(noteMilliseconds);

        // - get element from your data set at this position
        // - replace the contents of the view with that element
        holder.textViewTitle.setText(listItem.getTitle());
        holder.textViewBody.setText(listItem.getBody());
        holder.textViewTimestamp.setText(sdf.format(resultDate));
        holder.starredCheckBox.setChecked(listItem.isStarred());

        holder.itemView.setOnClickListener(v -> onItemClickListener.itemClicked(v, position, listItem.getTitle()));
        holder.itemView.setOnLongClickListener(v -> onItemLongClickListener.itemLongClicked(v, position, listItem.getTitle()));
        holder.starredCheckBox.setOnClickListener(v -> onStarClickListener.itemClicked(v, position, listItem.isStarred()));
        holder.editNoteBtn.setOnClickListener(v -> onEditClickListener.itemClicked(v, position, listItem.getTitle(), listItem.getBody(), listItem.isStarred(),
                listItem.getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    interface OnItemClickListener {
        void itemClicked(View v, int pos, String value);
    }

    interface OnItemLongClickListener {
        boolean itemLongClicked(View v, int pos, String value);
    }

    interface OnStarClickListener {
        void itemClicked(View v, int pos, boolean checked);
    }

    interface  OnEditClickListener {
        void itemClicked (View v, int pos, String title, String body, boolean checked, long timestamp);
    }

}



