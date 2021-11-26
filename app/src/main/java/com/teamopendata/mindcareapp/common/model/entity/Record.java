package com.teamopendata.mindcareapp.common.model.entity;


import androidx.annotation.NonNull;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(tableName = "record_table")
public class Record implements Cloneable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;
    private LocalDate date;
    private List<Task> tasks;

    public Record(long id, String title, LocalDate date, List<Task> tasks) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.tasks = tasks;
    }

    @Ignore
    public Record(LocalDate date, String title, List<Task> tasks) {
        this.title = title;
        this.date = date;
        this.tasks = tasks;
    }

    @Ignore
    public Record(String title, LocalDate date) {
        this.title = title;
        this.date = date;
    }

    @Ignore
    public Record() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Record record = (Record) o;
        return Objects.equals(title, record.title) && Objects.equals(date, record.date) && tasks.equals(record.tasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, date, tasks);
    }

    @NonNull
    @Override
    public String toString() {
        if (tasks == null) {
            return "id:" + id + " 기록 제목:" + title + " 날짜:";
        } else {
            return "id:" + id + " 기록 제목:" + title + " 날짜:" + date.toString() + " 할 일:" + tasks.toString();
        }
    }


    @NonNull
    @Override
    public Record clone() {
        try {
            Record record = (Record) super.clone();
            ArrayList<Task> tasks = new ArrayList<>();
            for (Task task : record.tasks) {
                tasks.add(task.clone());
            }
            record.setTasks(tasks);
            return record;
        } catch (CloneNotSupportedException e) {
            return new Record();
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
