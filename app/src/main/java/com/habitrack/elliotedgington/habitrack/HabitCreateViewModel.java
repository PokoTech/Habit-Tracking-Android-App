package com.habitrack.elliotedgington.habitrack;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class HabitCreateViewModel extends AndroidViewModel {

    private HabitRepository repository;

    public HabitCreateViewModel(@NonNull Application application) {
        super(application);
        repository = new HabitRepository(application);
    }

    public void insert(Habit habit) {
        repository.insert(habit);
    }

    public void update(Habit habit) {
        repository.update(habit);
    }

}
