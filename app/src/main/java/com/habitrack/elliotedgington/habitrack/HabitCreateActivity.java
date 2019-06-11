package com.habitrack.elliotedgington.habitrack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;

public class HabitCreateActivity extends AppCompatActivity {

    public static final String EXTRA_ID =
            "com.habitrack.elliotedgington.habitrack.EXTRA_ID";

    public static final String EXTRA_TITLE =
            "com.habitrack.elliotedgington.habitrack.EXTRA_TITLE";

    public static final String EXTRA_COMPLETED =
            "com.habitrack.elliotedgington.habitrack.EXTRA_COMPLETED";

    public static final String EXTRA_REMINDER_ENABLED =
            "com.habitrack.elliotedgington.habitrack.EXTRA_REMINDER_ENABLED";

    public static final String EXTRA_REMINDER_TIME =
            "com.habitrack.elliotedgington.habitrack.EXTRA_REMINDER_TIME";


    private HabitCreateViewModel habitCreateViewModel;

    private EditText editTextTitle;

    private Switch switchEnableReminder;
    private TimePicker timePickerReminderTime;

    private FloatingActionButton buttonSaveHabit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_create);

        // Get ViewModel
        habitCreateViewModel = ViewModelProviders.of(this).get(HabitCreateViewModel.class);
        // Get Views
        editTextTitle = findViewById(R.id.edit_text_title);

        switchEnableReminder = findViewById(R.id.switch_enable_reminder);
        timePickerReminderTime = findViewById(R.id.time_picker_reminder_time);
        timePickerReminderTime.setIs24HourView(true);

        buttonSaveHabit = findViewById(R.id.button_save_habit);

        timePickerReminderTime.setEnabled(false);

        final Intent intent = getIntent();

        // Set close button and window title.
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24dp);
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("EditMode");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            boolean timePickerEnabled = intent.getBooleanExtra(EXTRA_REMINDER_ENABLED, false);
            switchEnableReminder.setChecked(timePickerEnabled);
            timePickerReminderTime.setEnabled(timePickerEnabled);


            // Convert long to hour and minute.
            Date date = new Date(intent.getLongExtra(EXTRA_REMINDER_TIME, -1));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            timePickerReminderTime.setHour(calendar.get(Calendar.HOUR_OF_DAY));
            timePickerReminderTime.setMinute(calendar.get(Calendar.MINUTE));
        } else {
            setTitle("Create Habit");
        }

        // Enable / Disable timePicker on Switch.
        switchEnableReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                timePickerReminderTime.setEnabled(isChecked);
            }
        });

        // Save values to database.
        buttonSaveHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get Values from xml elements.
                String title = editTextTitle.getText().toString();
                boolean reminderEnabled = switchEnableReminder.isEnabled();

                // Converts time from timePicker into Date object.
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR, timePickerReminderTime.getHour());
                calendar.set(Calendar.MINUTE, timePickerReminderTime.getMinute());
                Date reminderTime = reminderEnabled ? calendar.getTime() : null;

                // Create new Alarm manager for reminderTime if enabled
                if (reminderEnabled && reminderTime != null) {
                    AlarmManager alarmManager = (AlarmManager) getApplicationContext()
                            .getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                    intent.putExtra(HabitCreateActivity.EXTRA_TITLE, title);
                    intent.putExtra(HabitCreateActivity.EXTRA_COMPLETED,
                            intent.getBooleanExtra(HabitCreateActivity.EXTRA_COMPLETED, false));
                    PendingIntent alarmIntent = PendingIntent.getBroadcast(
                            getApplicationContext(), 0, intent, 0);

                    alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
                            AlarmManager.INTERVAL_DAY, alarmIntent);
                }


                // Don't allow users to pass without filling in fields.
                if (title.trim().isEmpty()) {
                    Toast.makeText(v.getContext(), "Please fill out title of habit",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (intent.hasExtra(HabitCreateActivity.EXTRA_ID)) {
                    // If habit is being updated
                    int id = intent.getIntExtra(HabitCreateActivity.EXTRA_ID, -1);
                    if (id == -1) {
                        Toast.makeText(HabitCreateActivity.this, "Invalid table id",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    boolean completed = intent.getBooleanExtra(HabitCreateActivity.EXTRA_COMPLETED,
                            false);
                    // Create updated habit
                    Habit updatedHabit = new Habit(title, completed, reminderEnabled, reminderTime);
                    updatedHabit.setId(id);
                    habitCreateViewModel.update(updatedHabit);
                    // Show Confirmation.
                    Toast.makeText(HabitCreateActivity.this, "Habit updated!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // If habit is a new object
                    Habit newHabit = new Habit(title, false, reminderEnabled, reminderTime);
                    habitCreateViewModel.insert(newHabit);
                    // Show Confirmation.
                    Toast.makeText(HabitCreateActivity.this, "Habit created!",
                            Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
    }
}
