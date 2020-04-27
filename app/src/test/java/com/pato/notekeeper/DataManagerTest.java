package com.pato.notekeeper;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DataManagerTest {
    //variables set/used by a static method must be static.
    static DataManager sDataManager;

    @BeforeClass
    public static void classSetUp() {
        // This method will be run once in the class, before any tests.
        // Frequently used references can be obtained and re-used in class.
        sDataManager = DataManager.getInstance();
    }

    @Before
    public void setUp() {
        // method marked with @Before will run before each test in the class. If we have 3 tests this method will run 3-times before each test.
        // We make sure that all tests run in a consistent state.
        sDataManager.getNotes().clear();  // Clear the list of notes to start with a fresh copy.
        sDataManager.initializeExampleNotes();  // add a consistent example list.

    }

    @Test
    public void createNewNote() {
        // To create a new Note we need 3 things. Course, Note_title & Note_text.
        // This test creates a new note at specified index, Retrieves the note and verifies the note contains the data we saved.

        //DataManager sDataManager = DataManager.getInstance();  //get instance.
        final CourseInfo course = sDataManager.getCourse("android_async"); //get course.
        final String noteTitle = "Test note title";   //Note_title
        final String noteText = "This is the body text of my test note";

        //get index/position of the new note created.
        int noteIndex = sDataManager.createNewNote();
        NoteInfo newNote = sDataManager.getNotes().get(noteIndex);  //get reference to new note created. This is an empty note.

        //## - We want to introduce an assertion-error to find out what happens inCase of an error.
        // noteIndex - 1 will get the note before the one we created. We shall modify the wrong note.
        //NoteInfo newNote = sDataManager.getNotes().get(noteIndex - 1);

        //Populate the empty note with data.
        newNote.setCourse(course);
        newNote.setTitle(noteTitle);
        newNote.setText(noteText);

        // Up to this point we have created the newNote, but we haven't tested anything.
        // When testing we have to verify what we expected to happen. In this case we expected the new note created at specified index should have the values we set.
        NoteInfo compareNote = sDataManager.getNotes().get(noteIndex);  //Get reference to note at specified index.

        // A valid functional test has to check if the values we saved when creating the new note are equal to note values we retrieve at that index.
        assertEquals(course, compareNote.getCourse());
        assertEquals(noteTitle, compareNote.getTitle());
        assertEquals(noteText, compareNote.getText());

        // assertSame(newNote, compareNote);  //Checks if two references point to same object. In our case this is not a valid functional test.
    }

    @Test
    public void findSimilarNotes() {
        // The purpose of this test is to verify if findNote works as expected.
        // We are testing an edge-case, We create two notes which have similar course & note_title but different note_text. How does findNote behave when notes are very similar.
        final CourseInfo course = sDataManager.getCourse("android_async");
        final String noteTitle = "Test note title";
        final String noteText1 = "This is the body text of my test note";
        final String noteText2 = "This is the body of my second test note";

        //create the first note => the two notes have similar course & note_title.
        int noteIndex1 = sDataManager.createNewNote();
        NoteInfo newNote1 = sDataManager.getNotes().get(noteIndex1);
        newNote1.setCourse(course);
        newNote1.setTitle(noteTitle);
        newNote1.setText(noteText1);

        // Create the second note => the two notes have similar course & note_title.
        int noteIndex2 = sDataManager.createNewNote();
        NoteInfo newNote2 = sDataManager.getNotes().get(noteIndex2);
        newNote2.setCourse(course);
        newNote2.setTitle(noteTitle);
        newNote2.setText(noteText2);

        //Find the 1st note & confirm that we get the index where we saved the note.
        int foundIndex1 = sDataManager.findNote(newNote1);
        assertEquals(noteIndex1, foundIndex1);

        //Find the 2nd note and confirm that we get the index where we saved the note.
        int foundIndex2 = sDataManager.findNote(newNote2);
        assertEquals(noteIndex2, foundIndex2);

    }

    // Test-Driven software development philosophy we create the test before we implement the method.
    // Create a test that fulfills the requirements of the new overloaded createNewNote(parameters .. ) method.
    @Test
    public void createNewNoteOneStepCreation() {
        // To create a new note we need a course, note_Title & note_Text.
        final CourseInfo course = sDataManager.getCourse("android_async");
        final String noteTitle = "Test note Title";
        final String noteText = "This is the body of my test note";

        // Call overloaded createNewNote(Parameters ....) to create the new note.
        int noteIndex = sDataManager.createNewNote(course, noteTitle, noteText);

        // Retrieve note at index and make sure it contains the values we saved.
        NoteInfo compareNote = sDataManager.getNotes().get(noteIndex);
        assertEquals(course, compareNote.getCourse());
        assertEquals(noteTitle, compareNote.getTitle());
        assertEquals(noteText, compareNote.getText());
    }


}