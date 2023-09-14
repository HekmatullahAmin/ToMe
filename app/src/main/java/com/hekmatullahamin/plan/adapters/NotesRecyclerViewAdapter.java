package com.hekmatullahamin.plan.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.hekmatullahamin.plan.R;
import com.hekmatullahamin.plan.activities.NoteActivity;
import com.hekmatullahamin.plan.data.DatabaseHandler;
import com.hekmatullahamin.plan.model.MainViewModel;
import com.hekmatullahamin.plan.model.Note;
import com.hekmatullahamin.plan.utils.Constants;
import com.hekmatullahamin.plan.utils.Utils;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotesRecyclerViewAdapter extends RecyclerView.Adapter<NotesRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Note> noteArrayList;
    private TextView totalCountsOfNotes, noteOrNotesText;
    private MainViewModel mainViewModel;
    private boolean isEnable = false;
    private boolean isSelectAll = false;
    public boolean isLongPressed = false;
    private DatabaseHandler databaseHandler;
    private ArrayList<Note> selectedList = new ArrayList<>();

    public NotesRecyclerViewAdapter(Context context, ArrayList<Note> noteArrayList, TextView totalCountsOfNotes, TextView noteOrNotesText) {
        this.context = context;
        this.noteArrayList = noteArrayList;
        this.totalCountsOfNotes = totalCountsOfNotes;
        this.noteOrNotesText = noteOrNotesText;
        databaseHandler = new DatabaseHandler(context);
    }

    @NonNull
    @Override
    public NotesRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View customNoteCardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_note_card_view, parent, false);
        mainViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(MainViewModel.class);
        return new ViewHolder(customNoteCardView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesRecyclerViewAdapter.ViewHolder holder, int position) {
        Note note = noteArrayList.get(position);
        String date = Utils.formatDate(note.getNoteTextDate());

        holder.noteTitle.setText(note.getNoteTextTitle());
        holder.noteDate.setText(date);
        holder.noteText.setText(note.getNoteText());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isLongPressed = true;
//                check condition
                if (!isEnable) {
//                    when action mode is not enable
//                    enable action mode
                    ActionMode.Callback callback = new ActionMode.Callback() {
                        @Override
                        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                            MenuInflater menuInflater = mode.getMenuInflater();
                            menuInflater.inflate(R.menu.custom_action_mode_menu, menu);
                            return true;
                        }

                        @Override
                        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//                            when action mode is ready
                            isEnable = true;
                            clickItem(holder);
                            mainViewModel.getText().observe((LifecycleOwner) context, new Observer<String>() {
                                @Override
                                public void onChanged(String s) {
                                    mode.setTitle(String.format("%s Selected", s));
                                }
                            });
                            return true;
                        }

                        @Override
                        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                            int menuId = item.getItemId();
                            switch (menuId) {
                                case R.id.customActionModeMenuDeleteId:
                                    for (Note noteToBeDeleted : selectedList) {
                                        databaseHandler.deleteNote(noteToBeDeleted.getNoteId());
                                        noteArrayList.remove(noteToBeDeleted);
                                        notifyItemRemoved(position);
                                    }
                                    mode.finish();
                                    break;
                                case R.id.customActionModeSelectAllId:
                                    if (selectedList.size() == noteArrayList.size()) {
                                        isSelectAll = false;
                                        selectedList.clear();
                                    } else {
                                        isSelectAll = true;
                                        selectedList.clear();
                                        selectedList.addAll(noteArrayList);
                                    }
                                    mainViewModel.setText(String.valueOf(selectedList.size()));
                                    break;
                            }
//                            if not check box will not work
                            notifyDataSetChanged();
                            return true;
                        }

                        @Override
                        public void onDestroyActionMode(ActionMode mode) {
                            isEnable = false;
                            isSelectAll = false;
                            isLongPressed = false;
                            selectedList.clear();
                            mode.finish();
                            populateTotalCountOfNotes();
                            notifyDataSetChanged();
                        }
                    };
                    ((AppCompatActivity) v.getContext()).startActionMode(callback);
                } else {
                    clickItem(holder);
                }
                return true;
            }

        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLongPressed == true) {
                    if (isEnable) {
                        clickItem(holder);
                    }
                } else {
                    int position = holder.getAdapterPosition();
                    Note note = noteArrayList.get(position);
                    Bundle myNoteObjectBundle = new Bundle();
                    myNoteObjectBundle.putSerializable(Constants.NOTE_KEY, note);
                    Intent noteActivityIntent = new Intent(context, NoteActivity.class);
                    noteActivityIntent.putExtras(myNoteObjectBundle);
                    noteActivityIntent.putExtra(Constants.ACTIVITY_OR_ADAPTER_CLASS_KEY, Constants.NOTES_RECYCLER_VIEW_ADAPTER);
                    noteActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(noteActivityIntent);
                }
            }
        });

        if (isSelectAll) {
            holder.checkBoxCircleImageView.setVisibility(View.VISIBLE);
        } else {
            holder.checkBoxCircleImageView.setVisibility(View.GONE);

        }
    }

    private void clickItem(ViewHolder holder) {
        Note note = noteArrayList.get(holder.getAdapterPosition());
        if (holder.checkBoxCircleImageView.getVisibility() == View.GONE) {
            holder.checkBoxCircleImageView.setVisibility(View.VISIBLE);
            selectedList.add(note);
        } else {
            holder.checkBoxCircleImageView.setVisibility(View.GONE);
            selectedList.remove(note);
        }
        mainViewModel.setText(String.valueOf(selectedList.size()));
    }

    private void populateTotalCountOfNotes() {
        int count = databaseHandler.totalNotesCount();
        totalCountsOfNotes.setText(String.valueOf(count));
        if (count > 1) {
            noteOrNotesText.setText("notes");
        }else {
            noteOrNotesText.setText("note");
        }
    }

    @Override
    public int getItemCount() {
        return noteArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView noteTitle, noteDate, noteText;
        public CircleImageView checkBoxCircleImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.customNoteCardViewNoteTitleTextViewId);
            noteDate = itemView.findViewById(R.id.customNoteCardViewNoteDateTextViewId);
            noteText = itemView.findViewById(R.id.customNoteCardViewNoteTextTextViewId);
            checkBoxCircleImageView = itemView.findViewById(R.id.customNoteCardViewCheckBoxCircleImageViewId);
        }
    }
}
