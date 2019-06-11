package com.habitrack.elliotedgington.habitrack;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class HabitItemAdapter extends ListAdapter<Habit, HabitItemAdapter.HabitItemHolder> {

    private onSwitchCheckedChangeListener switchListener;
    private onItemContextMenuClickListener contextMenuClickListener;

    public HabitItemAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Habit> DIFF_CALLBACK = new DiffUtil.ItemCallback<Habit>() {
        @Override
        public boolean areItemsTheSame(@NonNull Habit oldItem, @NonNull Habit newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Habit oldItem, @NonNull Habit newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getCompleted() == newItem.getCompleted() &&
                    oldItem.isReminder_enabled() == newItem.isReminder_enabled() &&
                    oldItem.getReminder_time() == newItem.getReminder_time();
        }
    };

    @NonNull
    @Override
    public HabitItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_habit, parent, false);
        return new HabitItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitItemHolder holder, int position) {
        Habit currentHabit = getItem(position);
        holder.textViewTitle.setText(currentHabit.getTitle());
        holder.textViewReminderTime.setText(getTimestampFromDate(currentHabit.getReminder_time()));
        if (holder.switchCompleted.isChecked() != currentHabit.getCompleted())
            holder.switchCompleted.setChecked(currentHabit.getCompleted());
    }

    // Converts time into more human readable format.
    public String getTimestampFromDate(Date date) {
        return new SimpleDateFormat("HH:mm").format(date);
    }

    class HabitItemHolder extends RecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        private TextView textViewTitle;
        private TextView textViewReminderTime;
        private Switch switchCompleted;

        public HabitItemHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnCreateContextMenuListener(this);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewReminderTime = itemView.findViewById(R.id.text_view_reminder_time);
            switchCompleted = itemView.findViewById(R.id.switch_completed);

            // Switch Click.
            switchCompleted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int index = getAdapterPosition();
                    if (switchListener != null && index != RecyclerView.NO_POSITION)
                        switchListener.onSwitchCheckedChange(getItem(index), isChecked);
                }
            });
        }


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem editItem = menu.add(0, 0, 0, "Edit");
            MenuItem deleteItem = menu.add(0, 1, 0, "Delete");
            editItem.setOnMenuItemClickListener(this);
            deleteItem.setOnMenuItemClickListener(this);
        }


        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int index = getAdapterPosition();
            if (contextMenuClickListener != null && index != RecyclerView.NO_POSITION)
                contextMenuClickListener.onItemContextMenuClick(getItem(index), item);
            return false;
        }
    }

    public interface onItemContextMenuClickListener {
        void onItemContextMenuClick(Habit habit, MenuItem item);
    }

    public interface onSwitchCheckedChangeListener {
        void onSwitchCheckedChange(Habit habit, boolean isChecked);
    }

    public void setOnItemContextMenuClickListener(onItemContextMenuClickListener listener) {
        this.contextMenuClickListener = listener;
    }

    public void onSwitchCheckedChangeListener(onSwitchCheckedChangeListener listener) {
        this.switchListener = listener;
    }
}
