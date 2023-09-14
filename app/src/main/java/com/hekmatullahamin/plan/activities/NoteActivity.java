package com.hekmatullahamin.plan.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.hekmatullahamin.plan.R;
import com.hekmatullahamin.plan.data.DatabaseHandler;
import com.hekmatullahamin.plan.model.Note;
import com.hekmatullahamin.plan.utils.Constants;

public class NoteActivity extends AppCompatActivity {

    private EditText noteTitleET, noteTextET;
    private ImageView goBack;

    private DatabaseHandler databaseHandler;
    private Bundle myNoteBundle;
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        fieldsInitialization();
        populateTitleAndText();

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void fieldsInitialization() {
        databaseHandler = new DatabaseHandler(this);
        myNoteBundle = getIntent().getExtras();
        note = (Note) getIntent().getSerializableExtra(Constants.NOTE_KEY);
        noteTitleET = findViewById(R.id.noteActivityTitleEditTextId);
        noteTextET = findViewById(R.id.noteActivityTypeNoteEditTextId);
        goBack = findViewById(R.id.noteActivityBackImageViewId);
    }

    private void populateTitleAndText() {
        if (note != null) {
            if (!TextUtils.isEmpty(note.getNoteTextTitle())) {
                noteTitleET.setText(note.getNoteTextTitle());
            }
            if (!TextUtils.isEmpty(note.getNoteText())) {
                noteTextET.setText(note.getNoteText());
            }
        }

    }

    @Override
    public void onBackPressed() {
        String titleString, textString;
        titleString = noteTitleET.getText().toString();
        textString = noteTextET.getText().toString();
        String fromActivityOrAdapterClass = myNoteBundle.getString(Constants.ACTIVITY_OR_ADAPTER_CLASS_KEY);

        if (fromActivityOrAdapterClass.equals(Constants.ALL_NOTES_ACTIVITY_NAME)) {
            if (!titleString.isEmpty() || !textString.isEmpty()) {

                Note note = new Note();
                note.setNoteTextTitle(titleString);
                note.setNoteText(textString);
                databaseHandler.addNote(note);
            }
        } else if (fromActivityOrAdapterClass.equals(Constants.NOTES_RECYCLER_VIEW_ADAPTER)) {
            note.setNoteTextTitle(titleString);
            note.setNoteText(textString);
            databaseHandler.updateNote(note);

        }
        Intent allNotesActivityIntent = new Intent(this, AllNotesActivity.class);
        allNotesActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(allNotesActivityIntent);
    }
}