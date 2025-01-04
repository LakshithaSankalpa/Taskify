package com.example.taskifytodoapp;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;

public class TaskAdapter extends ArrayAdapter<TaskAdapter.TaskItem> {
    private final Context context;
    private final ArrayList<TaskItem> tasks;
    private final TaskButtonClickListener listener;

    public static class TaskItem implements Parcelable {
        String task;
        String dateTime;
        String completedDateTime;

        public TaskItem(String task, String dateTime) {
            this.task = task;
            this.dateTime = dateTime;
        }

        public TaskItem(String task, String dateTime, String completedDateTime) {
            this.task = task;
            this.dateTime = dateTime;
            this.completedDateTime = completedDateTime;
        }

        protected TaskItem(Parcel in) {
            task = in.readString();
            dateTime = in.readString();
            completedDateTime = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(task);
            dest.writeString(dateTime);
            dest.writeString(completedDateTime);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<TaskItem> CREATOR = new Creator<TaskItem>() {
            @Override
            public TaskItem createFromParcel(Parcel in) {
                return new TaskItem(in);
            }

            @Override
            public TaskItem[] newArray(int size) {
                return new TaskItem[size];
            }
        };
    }

    public interface TaskButtonClickListener {
        void onCompleteClick(int position);
        void onRemoveClick(int position);
    }

    public TaskAdapter(Context context, ArrayList<TaskItem> tasks, TaskButtonClickListener listener) {
        super(context, R.layout.task_list_item, tasks);
        this.context = context;
        this.tasks = tasks;
        this.listener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.task_list_item, parent, false);
        }

        TextView textViewTask = convertView.findViewById(R.id.textViewTask);
        TextView textViewDateTime = convertView.findViewById(R.id.textViewDateTime);
        MaterialButton buttonComplete = convertView.findViewById(R.id.buttonCompleteTask);
        MaterialButton buttonRemove = convertView.findViewById(R.id.buttonRemoveTask);

        TaskItem taskItem = tasks.get(position);
        textViewTask.setText(taskItem.task);
        textViewDateTime.setText("Created: " + taskItem.dateTime);

        buttonComplete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Complete Task")
                    .setMessage("Are you sure you want to complete this task?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        if (listener != null) {
                            listener.onCompleteClick(position);
                            showToast("Task completed successfully!");
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        buttonRemove.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Remove Task")
                    .setMessage("Are you sure you want to remove this task?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        if (listener != null) {
                            listener.onRemoveClick(position);
                            showToast("Task removed successfully!");
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        return convertView;
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}