package com.hekmatullahamin.plan.model;

public class Task {
    private int taskId, taskState = 0;
    private String taskNote;

    public Task() {
    }

    public Task(int taskId, int taskState, String taskNote) {
        this.taskId = taskId;
        this.taskState = taskState;
        this.taskNote = taskNote;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getTaskState() {
        return taskState;
    }

    public void setTaskState(int taskState) {
        this.taskState = taskState;
    }

    public String getTaskNote() {
        return taskNote;
    }

    public void setTaskNote(String taskNote) {
        this.taskNote = taskNote;
    }
}
