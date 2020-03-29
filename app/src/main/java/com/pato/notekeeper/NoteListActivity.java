package com.pato.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class NoteListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //initialize listView
        initializeDisplayContent();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show activity used to create new Note.
                startActivity(new Intent(NoteListActivity.this, NoteActivity.class));
            }
        });
    }

    /**
     * Method to load ListView and Display content
     */
    private void initializeDisplayContent() {
        //Populate the ListView with data from DataManager.
        final ListView listNotes = findViewById(R.id.list_notes);

        //get the list of notes from dataManager.
        List<NoteInfo> notes = DataManager.getInstance().getNotes();

        //ArrayAdapter to manage listView data-Loading and listView-Layouts.
        ArrayAdapter<NoteInfo> adapterNotes = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notes);
        listNotes.setAdapter(adapterNotes);

        //add onItemClickListener to listView to enable user to make a selection.
        listNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Create intent to start NoteActivity which will display the note user selected/clicked.
                Intent intent = new Intent(NoteListActivity.this, NoteActivity.class);

                //get the note user selected from the listView.
                //NoteInfo note = (NoteInfo) listNotes.getItemAtPosition(position);
                //intent.putExtra(NoteActivity.NOTE_POSITION, note);  //attach selected note to the intent-extras.
                intent.putExtra(NoteActivity.NOTE_POSITION, position);  //using intent-extras send index/position of selected note.
                startActivity(intent);

            }
        });

    }

}
