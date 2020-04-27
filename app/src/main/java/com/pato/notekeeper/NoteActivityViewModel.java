package com.pato.notekeeper;

import android.os.Bundle;

import androidx.lifecycle.ViewModel;

public class NoteActivityViewModel extends ViewModel {

    // This class saves Activity's state before activity is destroyed due to Device configuration change (portrait to LandScape).
    // When recreating activity we restore its state from this class.

    //When saving values to Bundle, we use key/CONSTANTS to name the values saved in bundle.
    public static final String ORIGINAL_NOTE_COURSE_ID = "com.pato.notekeeper.ORIGINAL_NOTE_COURSE_ID";
    public static final String ORIGINAL_NOTE_TITLE = "com.pato.notekeeper.ORIGINAL_NOTE_TITLE";
    public static final String ORIGINAL_NOTE_TEXT = "com.pato.notekeeper.ORIGINAL_NOTE_TEXT";

    // Activity instance state-fields, used to save instance state.
    public String mOriginalNoteCourseId;
    public String mOriginalNoteTitle;
    public String mOriginalNoteText;

    // ViewModel isNewlyCreated when we create the model for the first-time.
    public boolean mIsNewlyCreated = true;

    public void restoreState(Bundle inState) {
        // Restore instance state from the saved bundle and Assign the instance state to variables in this class.
        mOriginalNoteCourseId = inState.getString(ORIGINAL_NOTE_COURSE_ID); //Read value from bundle.
        mOriginalNoteTitle = inState.getString(ORIGINAL_NOTE_TITLE);
        mOriginalNoteText = inState.getString(ORIGINAL_NOTE_TEXT);

    }

    public void saveState(Bundle outState) {
        // we use constants as keys to name values we save to bundle.
        outState.putString(ORIGINAL_NOTE_COURSE_ID, mOriginalNoteCourseId);
        outState.putString(ORIGINAL_NOTE_TITLE, mOriginalNoteTitle);
        outState.putString(ORIGINAL_NOTE_TEXT, mOriginalNoteText);

    }
}
