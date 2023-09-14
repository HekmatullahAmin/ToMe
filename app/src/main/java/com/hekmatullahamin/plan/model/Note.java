package com.hekmatullahamin.plan.model;

import java.io.Serializable;

public class Note implements Serializable {
    private int noteId;
    private String noteTextTitle, noteText;
    private long noteTextDate;

    public Note() {
    }

    public Note(int noteId, String noteTextTitle, String noteText, long noteTextDate) {
        this.noteId = noteId;
        this.noteTextTitle = noteTextTitle;
        this.noteText = noteText;
        this.noteTextDate = noteTextDate;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public String getNoteTextTitle() {
        return noteTextTitle;
    }

    public void setNoteTextTitle(String noteTextTitle) {
        this.noteTextTitle = noteTextTitle;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public long getNoteTextDate() {
        return noteTextDate;
    }

    public void setNoteTextDate(long noteTextDate) {
        this.noteTextDate = noteTextDate;
    }
}
