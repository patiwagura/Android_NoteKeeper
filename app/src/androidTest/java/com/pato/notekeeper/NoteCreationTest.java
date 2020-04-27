package com.pato.notekeeper;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.runners.AndroidJUnit4;  //support library Junit.
import androidx.test.rule.ActivityTestRule;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


//static import methods in the classes. Allows using methodName without prefixing class.methodName
import static org.junit.Assert.*;
import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.Espresso.pressBack;  //press backButton.
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard; //close soft KeyBoard.
import static org.hamcrest.Matchers.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;

@RunWith(AndroidJUnit4.class)
public class NoteCreationTest {

    static DataManager sDataManager;

    @BeforeClass
    public static void classSetUp(){
        sDataManager = DataManager.getInstance();
    }

    // User interface tests require an Activity which is the starting point for test operation.
    // In our case we start at NoteListActivity where we click new button.
    @Rule
    public ActivityTestRule<NoteListActivity> mNoteListActivityRule = new ActivityTestRule<>(NoteListActivity.class);


    @Test
    public void createNewNote() {
        // A User interface test to verify createNewNote works as expected.

        //We don't have to create reference-Variables when working with tests unless we need to reuse the references repeatedly.
        //ViewInteraction fabNewNote = onView(withId(R.id.fab)); // Reference to ViewInteraction/View object.
        //fabNewNote.perform(click()); // click fab button

        final CourseInfo course = sDataManager.getCourse("java_lang");
        final String noteTitle ="Test note Title";
        final  String noteText = "This is the body of our test note";

        // chain call methods we don't need to declare reference-variables.
        onView(withId(R.id.fab)).perform(click());

        // mimic how a user interacts with a spinner. click spinner then make selection.
        onView(withId(R.id.spinner_courses)).perform(click()); //click spinner.
        // make selection from spinner, We will match selected course using combination of type<CourseInfo> and the actual-value selected.
        onData(allOf(instanceOf(CourseInfo.class),equalTo(course))).perform(click());

        //check UI behavior. We expect spinner to display the course_title for the selected course.
        onView(withId(R.id.spinner_courses)).check(matches(withSpinnerText(containsString(course.getTitle()))));

        //program will automatically navigate to required Activities. e.g NoteActivity.
        //input note Title & check if UI behavior was performed as we expected.
        onView(withId(R.id.text_note_title)).perform(typeText(noteTitle))
        .check(matches(withText(containsString(noteTitle))));

        // Input text_note for the note and then check ui behavior. this can be done in one chained method call.
        onView(withId(R.id.text_note_text)).perform(typeText(noteText), closeSoftKeyboard());
        // check user-interface behavior worked as expected.
        onView(withId(R.id.text_note_text)).check(matches(withText(containsString(noteText))));

        pressBack(); // go back to NoteListActivity & save the note.

        // We save the note when back_button is pressed.
        // verify the Logic to make sure application behaved as expected.
        int noteIndex = sDataManager.getNotes().size()-1;  //get index for the last saved note.
        NoteInfo note = sDataManager.getNotes().get(noteIndex);
        assertEquals(course, note.getCourse());
        assertEquals(noteTitle, note.getTitle());
        assertEquals(noteText, note.getText());
    }
}