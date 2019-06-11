package com.habitrack.elliotedgington.habitrack;

import java.util.Date;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Room database table.
@Entity(tableName = "habit_table")
public class Habit {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;

    private boolean completed;

    private boolean reminder_enabled;

    private Date reminder_time;

    public Habit(String title, boolean completed, boolean reminder_enabled, Date reminder_time) {
        this.title = title;
        this.completed = completed;
        this.reminder_enabled = reminder_enabled;
        this.reminder_time = reminder_time;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCompleted(boolean completed) { this.completed = completed; }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean getCompleted() {
        return completed;
    }

    public boolean isReminder_enabled() {
        return reminder_enabled;
    }

    public Date getReminder_time() {
        return reminder_time;
    }
}
