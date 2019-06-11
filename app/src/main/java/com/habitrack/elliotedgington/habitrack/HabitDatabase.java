package com.habitrack.elliotedgington.habitrack;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

// Creates a singleton of the room database (to stop multiple being created).
// Version incremented after every database change. Migration not supported.
@Database(entities = {Habit.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class HabitDatabase extends RoomDatabase {
    private static HabitDatabase singleton;

    public abstract HabitDao habitDao();

    public static synchronized HabitDatabase getInstance(Context context) {
        if (singleton == null)
            singleton = Room.databaseBuilder(context.getApplicationContext(),
                    HabitDatabase.class, "habit_database")
                    .fallbackToDestructiveMigration()
                    .build();

        return singleton;
    }
}
