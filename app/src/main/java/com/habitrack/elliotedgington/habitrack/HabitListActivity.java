package com.habitrack.elliotedgington.habitrack;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HabitListActivity extends AppCompatActivity {

    public static final String CHANNEL_ID
            = "com.habitrack.elliotedgington.habitrack.CHANNEL_ID";
    private HabitListViewModel habitListViewModel;
    FloatingActionButton buttonCreateHabit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_list);

        // Create notification channel for API 26+ to display notifications.
        createNotificationChannel();

        // Create layout manager to manage all habit items.
        RecyclerView recyclerViewHabitList = findViewById(R.id.recycler_view_habit_list);
        recyclerViewHabitList.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewHabitList.setHasFixedSize(true);

        // Set adapter to our habit items.
        final HabitItemAdapter adapter = new HabitItemAdapter();
        recyclerViewHabitList.setAdapter(adapter);


        // On click of the FAB intent to change screen.
        buttonCreateHabit = findViewById(R.id.button_create_habit);
        buttonCreateHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HabitListActivity.this,
                        HabitCreateActivity.class);
                startActivity(intent);
            }
        });

        // Add all habits from database to our layout.
        habitListViewModel = ViewModelProviders.of(this).get(HabitListViewModel.class);
        habitListViewModel.getAllHabits().observe(this, new Observer<List<Habit>>() {
            @Override
            public void onChanged(List<Habit> habits) {
                adapter.submitList(habits);
            }
        });

        adapter.setOnItemContextMenuClickListener(new HabitItemAdapter.onItemContextMenuClickListener() {
            @Override
            public void onItemContextMenuClick(Habit habit, MenuItem item) {
                switch(item.getItemId()){
                    case 0:
                        startEditActivity(habit);
                        break;
                    case 1:
                        habitListViewModel.delete(habit);
                        Toast.makeText(HabitListActivity.this, "Habit deleted.",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        // On Switch in recycler view change.
        adapter.onSwitchCheckedChangeListener(new HabitItemAdapter.onSwitchCheckedChangeListener() {
            @Override
            public void onSwitchCheckedChange(Habit habit, boolean isChecked) {
                habit.setCompleted(isChecked);
                habitListViewModel.update(habit);
            }
        });
    }

    // Starts the edit activity with parsed habit.
    private void startEditActivity(Habit habit){
        Intent intent = new Intent(HabitListActivity.this,
                HabitCreateActivity.class);
        // Put all habits data as extras in intent.
        intent.putExtra(HabitCreateActivity.EXTRA_ID, habit.getId());
        intent.putExtra(HabitCreateActivity.EXTRA_TITLE, habit.getTitle());
        intent.putExtra(HabitCreateActivity.EXTRA_COMPLETED, habit.getCompleted());
        intent.putExtra(HabitCreateActivity.EXTRA_REMINDER_ENABLED,
                habit.isReminder_enabled());
        intent.putExtra(HabitCreateActivity.EXTRA_REMINDER_TIME,
                habit.getReminder_time().getTime());

        startActivity(intent);
    }

    // Code from https://developer.android.com/training/notify-user/build-notification.html
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "HabiTrack";
            String description = "Habit tracking application";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
