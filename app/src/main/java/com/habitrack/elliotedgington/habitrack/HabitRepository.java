package com.habitrack.elliotedgington.habitrack;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

// Custom API for habit_table database.
// Can be made scalable for other databases/ fetch requests.
public class HabitRepository {
    private HabitDao habitDao;
    private LiveData<List<Habit>> allHabits;

    public HabitRepository(Application application){
        HabitDatabase database = HabitDatabase.getInstance(application);
        habitDao =  database.habitDao();
        allHabits = habitDao.getAllHabits();
    }

    // Modification Methods.

    public void insert(Habit habit){
        new InsertHabitAsyncTask(habitDao).execute(habit);
    }
    public void update(Habit habit){
        new UpdateHabitAsyncTask(habitDao).execute(habit);
    }
    public void delete(Habit habit){
        new DeleteHabitAsyncTask(habitDao).execute(habit);
    }

    // Returns all habits as a list to display on screen.
    public LiveData<List<Habit>> getAllHabits(){
        return allHabits;
    }

    // Room database only allows modifications to the database in separate threads.
    // A precaution to make sure the modification isn't interrupted, possibly corrupting the
    // database.
    // Async Methods.

    private static class InsertHabitAsyncTask extends AsyncTask<Habit, Void, Void> {
        private HabitDao habitDao;

        private InsertHabitAsyncTask(HabitDao habitDao){
            this.habitDao = habitDao;
        }
        @Override
        protected Void doInBackground(Habit... habits) {
            habitDao.insert(habits[0]);
            return null;
        }
    }

    private static class UpdateHabitAsyncTask extends AsyncTask<Habit, Void, Void> {
        private HabitDao habitDao;

        private UpdateHabitAsyncTask(HabitDao habitDao){
            this.habitDao = habitDao;
        }
        @Override
        protected Void doInBackground(Habit... habits) {
            habitDao.update(habits[0]);
            return null;
        }
    }

    private static class DeleteHabitAsyncTask extends AsyncTask<Habit, Void, Void> {
        private HabitDao habitDao;

        private DeleteHabitAsyncTask(HabitDao habitDao){
            this.habitDao = habitDao;
        }
        @Override
        protected Void doInBackground(Habit... habits) {
            habitDao.delete(habits[0]);
            return null;
        }
    }
}
