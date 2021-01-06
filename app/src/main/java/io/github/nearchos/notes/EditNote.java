package io.github.nearchos.notes;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


public class EditNote extends AppCompatActivity {

    EditText edit_Title;
    EditText edit_Body;
    Long timestamp;
    CheckBox edit_Starred;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        // Set title, body and star as the previous notes data
        edit_Title = findViewById(R.id.edit_Title);
        edit_Body = findViewById(R.id.edit_Body);
        edit_Starred = findViewById(R.id.edit_Starred);

        Intent intent = getIntent();

        edit_Title.setText(intent.getStringExtra("clicked_item_title"));
        edit_Body.setText(intent.getStringExtra("clicked_item_body"));
        edit_Starred.setChecked(intent.getBooleanExtra("clicked_item_starred", true));
        timestamp = intent.getLongExtra("clicked_item_timestamp", 0L);

    }


    public void goBack(View v) {
        finish();
        overridePendingTransition( 0, 0);
        Intent intent = new Intent(EditNote.this, MainActivity.class);
        overridePendingTransition( 0, 0);
        startActivity(intent);
    }

    // edit note
    public void editItem(View view) {
        edit_Title = findViewById(R.id.edit_Title);
        edit_Body = findViewById(R.id.edit_Body);
        edit_Starred = findViewById(R.id.edit_Starred);

        // Get clicked item's position and title
        int listItemPosition;
        Intent mainActivityIntent = getIntent();
        listItemPosition = mainActivityIntent.getIntExtra("clicked_item_position", 0);

        if (edit_Title.getText().toString().matches("")) {
            Toast.makeText(this, "Enter Title", Toast.LENGTH_SHORT).show();
        } else {
            String edited_Title = edit_Title.getText().toString();
            String edited_Body = edit_Body.getText().toString();
            Long edited_Timestamp = timestamp;
            Boolean edited_Starred = edit_Starred.isChecked();

            mainActivityIntent = new Intent(this, MainActivity.class);
            mainActivityIntent.putExtra("edited_Title", edited_Title);
            mainActivityIntent.putExtra("edited_Body", edited_Body);
            mainActivityIntent.putExtra("edited_Timestamp", edited_Timestamp);
            mainActivityIntent.putExtra("edited_Starred", edited_Starred);
            mainActivityIntent.putExtra("clicked_item_position", listItemPosition);

            finish();
            startActivity(mainActivityIntent);
        }
    }

}