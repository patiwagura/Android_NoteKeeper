package com.pato.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

public class NoteActivity extends AppCompatActivity {

    // We can have one class containing all constants or Create constant in the target class.
    //public static final String NOTE_INFO = "com.pato.notekeeper.NOTE_INFO";  //package-qualify a constant-value to make it unique.
    public static final String NOTE_POSITION = "com.pato.notekeeper.NOTE_POSITION";  //package-qualify a constant-value to make it unique.
    public static final int POSITION_NOT_SET = -1;
    private NoteInfo mNote;
    private boolean mIsNewNote;
    private Spinner mSpinnerCourses;
    private EditText mTextNoteTitle;
    private EditText mTextNoteText;
    private int mNotePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //create a reference to a spinner declared in resource file.
        mSpinnerCourses = findViewById(R.id.spinner_courses);

        List<CourseInfo> courses = DataManager.getInstance().getCourses();  //list of courses stored in an array.
        //create an adapter that can handle Lists /Arrays.
        ArrayAdapter<CourseInfo> adapterCourses = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courses);
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //Set Layout_resource to manage list of available selections.

        //Link the spinner to the Adapter.
        mSpinnerCourses.setAdapter(adapterCourses);

        //Method to get intent that started this activity & the intent-extras.
        readDisplayStateValues();

        //populate and display the note user selected.
        mTextNoteTitle = findViewById(R.id.text_note_title);
        mTextNoteText = findViewById(R.id.text_note_text);

        // We don't display a new-Note. It has no-content to display.
        if (!mIsNewNote)
            displayNote(mSpinnerCourses, mTextNoteTitle, mTextNoteText);

    }

    private void displayNote(Spinner spinnerCourses, EditText textNoteTitle, EditText textNoteText) {
        //get list of all courses.
        List<CourseInfo> courses = DataManager.getInstance().getCourses();

        int courseIndex = courses.indexOf(mNote.getCourse()); //get index of the selected course.
        spinnerCourses.setSelection(courseIndex);  //set selected course in the spinner.

        // set note_title and note_text texts
        textNoteTitle.setText(mNote.getTitle());
        textNoteText.setText(mNote.getText());

    }

    /**
     * Get the note user selected and was passed via intent-extras.
     * Incase of a new-Note the intent-extras will be null.
     */
    private void readDisplayStateValues() {
        Intent intent = getIntent();  // get the intent that started this Activity.

        //get reference to note user selected and was passed via intent-extras.
        //mNote = intent.getParcelableExtra(NOTE_INFO);
        int position = intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET);

        //Incase of a new note we have not passed any intent-extras, therefore mNote = null.
        //Only true when mNote or Selected-Note is null.
        //mIsNewNote = mNote == null;
        mIsNewNote = position == POSITION_NOT_SET;

        if (mIsNewNote) {
            //We are creating a new Note.
            //In Android we create the backing store of the new entry when creating the entry. Usually on Activity's onCreate() method.
            createNewNote();
        } else {
            //get note using specified index/position.
            mNote = DataManager.getInstance().getNotes().get(position);
        }


    }

    private void createNewNote() {
        DataManager dm = DataManager.getInstance(); //get dataManager instance.
        //create a newNote and get its position.
        mNotePosition = dm.createNewNote();
        mNote = dm.getNotes().get(mNotePosition);  //get Note at the specified index/position.

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send_mail) {
            sendEmail();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveNote();  //method to save changes made to our note.
    }

    /**
     * Save Note method, We get all Values from Activity. This is because user could have made a change in UI which is yet to be saved.
     */
    private void saveNote() {
        //Obtain values of the view items on Screen and save to Note instance.
        mNote.setCourse((CourseInfo) mSpinnerCourses.getSelectedItem()); //selected Spinner-Item.
        mNote.setTitle(mTextNoteTitle.getText().toString());
        mNote.setText(mTextNoteText.getText().toString());
    }

    private void sendEmail() {
        // using implicit intents to send an email.
        // Our email should have subject = Title of our note.
        // Email message body = message that talks about the notes course + the note text.

        CourseInfo course = (CourseInfo) mSpinnerCourses.getSelectedItem(); //get selected item from spinner.
        String subject = mTextNoteTitle.getText().toString();  //Our Email Subject.
        String text = "Checkout what I learned in the pluralsight course \" " + course.getTitle() + " \" \n " + mTextNoteText.getText().toString();

        //implicit intent to send an email.
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc2822");  //standard internet mime-type for sending email.
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);

        startActivity(intent);

    }
}
