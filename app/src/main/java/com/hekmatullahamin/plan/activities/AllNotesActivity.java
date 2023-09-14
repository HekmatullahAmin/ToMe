package com.hekmatullahamin.plan.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hekmatullahamin.plan.R;
import com.hekmatullahamin.plan.adapters.NotesRecyclerViewAdapter;
import com.hekmatullahamin.plan.data.DatabaseHandler;
import com.hekmatullahamin.plan.model.Note;
import com.hekmatullahamin.plan.utils.Constants;

import java.util.ArrayList;

public class AllNotesActivity extends AppCompatActivity {

    private TextView totalNotesCount;
    private TextView noteOrNotesText;
    private FloatingActionButton addNoteFAB;
    private RecyclerView notesRecyclerView;
    private NotesRecyclerViewAdapter notesRecyclerViewAdapter;
    private DatabaseHandler databaseHandler;
    private ArrayList<Note> notes;
    private CollapsingToolbarLayout collapsingToolbarLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_notes);

        fieldsInitialization();

        addNoteFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editNoteActivityIntent = new Intent(AllNotesActivity.this, NoteActivity.class);
                editNoteActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                editNoteActivityIntent.putExtra(Constants.ACTIVITY_OR_ADAPTER_CLASS_KEY, Constants.ALL_NOTES_ACTIVITY_NAME);
                startActivity(editNoteActivityIntent);
            }
        });
    }

    private void fieldsInitialization() {
        databaseHandler = new DatabaseHandler(this);
        notes = new ArrayList<>();
        totalNotesCount = findViewById(R.id.allNotesActivityNotesCountTextViewId);
        noteOrNotesText = findViewById(R.id.allNotesActivityNotesTextTextViewId);
        addNoteFAB = findViewById(R.id.allNotesActivityAddNoteFABId);
        notesRecyclerView = findViewById(R.id.allNotesActivityRecyclerViewId);

        collapsingToolbarLayout = findViewById(R.id.allNotesActivityCollapsingToolbarLayoutId);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.black, null));
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white, null));

        notesRecyclerView.setHasFixedSize(true);
        notesRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
    }

    private void populateTotalCountOfNotes() {
        int count = databaseHandler.totalNotesCount();
        totalNotesCount.setText(String.valueOf(count));
        if (count > 1) {
            noteOrNotesText.setText("notes");
        } else {
            noteOrNotesText.setText("note");
        }
    }

    private void refreshRecyclerView() {
        notes.clear();
        notes = databaseHandler.getAllNotes();
        notesRecyclerViewAdapter = new NotesRecyclerViewAdapter(AllNotesActivity.this, notes, totalNotesCount, noteOrNotesText);
        notesRecyclerView.setAdapter(notesRecyclerViewAdapter);
        notesRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateTotalCountOfNotes();
        refreshRecyclerView();
    }
}