package com.habitrack.elliotedgington.habitrack;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class HabitListViewModel extends AndroidViewModel {
    private HabitRepository repository;
    private LiveData<List<Habit>> allHabits;

    public HabitListViewModel(@NonNull Application application) {
        super(application);
        repository = new HabitRepository(application);
        allHabits = repository.getAllHabits();
    }

    public void insert(Habit habit){
        repository.insert(habit);
    }

    public void update(Habit habit){
        repository.update(habit);
    }

    public void delete(Habit habit){
        repository.delete(habit);
    }

    public LiveData<List<Habit>> getAllHabits() {
        return allHabits;
    }
}
