package com.pato.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class NoteListActivity extends AppCompatActivity {
    private NoteRecyclerAdapter mNoteRecyclerAdapter;


    //private ArrayAdapter<NoteInfo> mAdapterNotes;  //ListView ArrayAdapter.

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

        //displayMyListView();  // Code was used to display a ListView.=> replaced with RecyclerView.

        final RecyclerView recyclerNotes = (RecyclerView) findViewById(R.id.list_notes);
        final LinearLayoutManager notesLayoutManager = new LinearLayoutManager(this);
        recyclerNotes.setLayoutManager(notesLayoutManager);

        //associate Adapter with RecyclerView.
        List<NoteInfo> notes = DataManager.getInstance().getNotes();
        mNoteRecyclerAdapter = new NoteRecyclerAdapter(this, notes);
        recyclerNotes.setAdapter(mNoteRecyclerAdapter);

    }

    private void displayMyListView() {


//        // Code to display ListView. We have replaced ListView with RecyclerView.
//
//
//
//        //Populate the ListView with data from DataManager.
//        final ListView listNotes = findViewById(R.id.list_notes);
//
//        //get the list of notes from dataManager.
//        List<NoteInfo> notes = DataManager.getInstance().getNotes();
//
//        //ArrayAdapter to manage listView data-Loading and listView-Layouts.
//        mAdapterNotes = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notes);
//        listNotes.setAdapter(mAdapterNotes);
//
//        //add onItemClickListener to listView to enable user to make a selection.
//        listNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                //Create intent to start NoteActivity which will display the note user selected/clicked.
//                Intent intent = new Intent(NoteListActivity.this, NoteActivity.class);
//
//                //get the note user selected from the listView.
//                //NoteInfo note = (NoteInfo) listNotes.getItemAtPosition(position);
//                //intent.putExtra(NoteActivity.NOTE_POSITION, note);  //attach selected note to the intent-extras.
//                intent.putExtra(NoteActivity.NOTE_POSITION, position);  //using intent-extras send index/position of selected note.
//                startActivity(intent);
//
//            }
//        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        //mAdapterNotes.notifyDataSetChanged(); //Notify the adapter when resuming Activity about any data changes.
        mNoteRecyclerAdapter.notifyDataSetChanged();  // Notify adapter about data changes.
    }

}
