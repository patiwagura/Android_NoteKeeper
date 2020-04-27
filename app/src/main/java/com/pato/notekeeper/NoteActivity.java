package com.pato.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

public class NoteActivity extends AppCompatActivity {

    // We can have one class containing all constants or Create constant in the target class.
    //public static final String NOTE_INFO = "com.pato.notekeeper.NOTE_INFO";  //package-qualify a constant-value to make it unique.
    public static final String NOTE_POSITION = "com.pato.notekeeper.NOTE_POSITION";  //package-qualify a constant-value to make it unique.
    public static final int POSITION_NOT_SET = -1;

    //Common practice is to write debug messages for Activity LifeCycle methods. so that we can tell what is happening.
    private final String TAG = getClass().getSimpleName();
    private NoteInfo mNote;
    private boolean mIsNewNote;
    private Spinner mSpinnerCourses;
    private EditText mTextNoteTitle;
    private EditText mTextNoteText;
    private int mNotePosition;
    private boolean mIsCancelling;

    // ViewModel is used to save activity instance state.
    // When Activity is destroyed due to Device configuration change (Portrait to Landscape) => ViewModels are destroyed together with Activity.
    private NoteActivityViewModel mViewModel;

    //Fields to persist/save Note-instance-State when activity is destroyed
//    private String mOriginalNoteCourseId;
//    private String mOriginalNoteTitle;
//    private String mOriginalNoteText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //initialize viewModel => we use ViewModelProvider to manage ViewModel instances.
        // Singleton Means Entire application has only one instance. e.g AndroidViewModelFactory.
        ViewModelProvider viewModelProvider = new ViewModelProvider(getViewModelStore(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()));
        mViewModel = viewModelProvider.get(NoteActivityViewModel.class); //get viewModel instance.

        // check if bundle is null. We only restore instance-state from bundle when ViewModel does not exist.
        if (mViewModel.mIsNewlyCreated && savedInstanceState != null) {
            // Restore the saved activity instance-state from bundle. ViewModel does not exist if isNewlyCreated = true.
            mViewModel.restoreState(savedInstanceState);
        }
        mViewModel.mIsNewlyCreated = false;  // We have an existing ViewModel instance.

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
        saveOriginalNoteValues();

        //populate and display the note user selected.
        mTextNoteTitle = findViewById(R.id.text_note_title);
        mTextNoteText = findViewById(R.id.text_note_text);

        // We don't display a new-Note. It has no-content to display.
        if (!mIsNewNote)
            displayNote(mSpinnerCourses, mTextNoteTitle, mTextNoteText);

        Log.d(TAG, "onCreate-LifeCycle");

    }

    private void saveOriginalNoteValues() {
        //method to save original Note values, so that we can revert back to Original values if need be.
        // We have nothing to save for a newNote.
        if (mIsNewNote)
            return;

        //Save all original Note values. CourseId is unique to all courses.
        // Saving Activity instance state to ViewModel class.
        // When activity is destroyed, instance state is lost. e.g instance fields loose their data.
        //mOriginalNoteCourseId = mNote.getCourse().getCourseId();
        mViewModel.mOriginalNoteCourseId = mNote.getCourse().getCourseId();
        mViewModel.mOriginalNoteTitle = mNote.getTitle();
        mViewModel.mOriginalNoteText = mNote.getText();

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
     * InCase of a new-Note the intent-extras will be null.
     */
    private void readDisplayStateValues() {
        Intent intent = getIntent();  // get the intent that started this Activity.

        //get reference to note user selected and was passed via intent-extras.
        //mNote = intent.getParcelableExtra(NOTE_INFO);
        mNotePosition = intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET);

        //Incase of a new note we have not passed any intent-extras, therefore mNote = null.
        //Only true when mNote or Selected-Note is null.
        //mIsNewNote = mNote == null;
        mIsNewNote = mNotePosition == POSITION_NOT_SET;

        if (mIsNewNote) {
            //We are creating a new Note.
            //In Android we create the backing store of the new entry when creating the entry. Usually on Activity's onCreate() method.
            createNewNote();
        }
        //Log the note's position.
        Log.i(TAG, "mNotePosition : " + mNotePosition);
        //get note at the specified index/position.
        mNote = DataManager.getInstance().getNotes().get(mNotePosition);

    }

    private void createNewNote() {
        DataManager dm = DataManager.getInstance(); //get dataManager instance.
        //create a new Note and get its position/Index in the List.
        mNotePosition = dm.createNewNote();

        // This method is called by readDisplayStateValues we don't have to retrieve note in both methods.
        //mNote = dm.getNotes().get(mNotePosition);  //get Note at the specified index/position.

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
        } else if (id == R.id.action_next) {
            moveNext();

            return true;

        } else if (id == R.id.action_cancel) {
            // We no-longer wish to proceed with creating the new-Note. Don't save any changes.
            mIsCancelling = true;
            finish();  //destroy this activity. onPause() will be called as we destroy activity.
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_next); // Reference to menu.
        int lastNoteIndex = DataManager.getInstance().getNotes().size() - 1; //Arrays have zero-based indexes.
        item.setEnabled(mNotePosition < lastNoteIndex); //true when lastNoteIndex is less than currentNotePosition.

        return super.onPrepareOptionsMenu(menu);
    }

    // move to the next note.
    private void moveNext() {
        saveNote();  // before we move to next note save current note changes.

        ++mNotePosition; // increment note position.
        mNote = DataManager.getInstance().getNotes().get(mNotePosition);  //note at index position.

        saveOriginalNoteValues(); //If we cancel, revert to original note values.
        displayNote(mSpinnerCourses, mTextNoteTitle, mTextNoteText);

        invalidateOptionsMenu();  //trigger system to call onPrepareOptionsMenu. Provides chance to change menu.
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //save the activity instance state to bundle.
        if (outState != null) {
            mViewModel.saveState(outState);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mIsCancelling) {
            // We are cancelling the note.
            Log.i(TAG, "Cancelling note at position : " + mNotePosition);
            if (mIsNewNote) {
                // we were creating a new Note but decided to delete it. Delete Note from backing store.
                DataManager.getInstance().removeNote(mNotePosition);
            } else {
                // We have decided to discard the new changes, Restore the original state of the existing Note.
                storePreviousNoteValues();
            }

        } else {
            // Save changes made to Note to the backing store.
            saveNote();
        }

        Log.d(TAG, "onPause-LifeCycle");
    }

    private void storePreviousNoteValues() {
        // Restore the original note values. Discard any new changes made to Note.
        // We Restore original Note values from the ViewModel.
        CourseInfo course = DataManager.getInstance().getCourse(mViewModel.mOriginalNoteCourseId);
        mNote.setCourse(course);
        mNote.setTitle(mViewModel.mOriginalNoteTitle);
        mNote.setText(mViewModel.mOriginalNoteText);
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
