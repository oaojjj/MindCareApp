package com.teamopendata.mindcareapp.common.model.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity
public class Task implements Cloneable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String contents;
    private boolean completed;

    @Ignore
    public Task() {
    }

    @Ignore
    public Task(String contents, boolean completed) {
        this.contents = contents;
        this.completed = completed;
    }

    @Ignore
    public Task(String contents) {
        this.contents = contents;
        completed = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return completed == task.completed && contents.equals(task.contents);
    }

    @NonNull
    @Override
    public Task clone() {
        try {
            return (Task) super.clone();
        } catch (CloneNotSupportedException e) {
            return new Task();
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(contents, completed);
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    @Override
    public String toString() {
        return "Task{" +
                "contents='" + contents + '\'' +
                ", completed=" + completed +
                '}';
    }

}
