package com.example.taskifytodoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import java.util.ArrayList;

public class CompletedTaskAdapter extends ArrayAdapter<TaskAdapter.TaskItem> {

    public CompletedTaskAdapter(Context context, ArrayList<TaskAdapter.TaskItem> tasks) {
        super(context, R.layout.completed_task_item, tasks);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.completed_task_item, parent, false);
        }

        TaskAdapter.TaskItem task = getItem(position);
        TextView textViewTask = convertView.findViewById(R.id.textViewCompletedTask);
        TextView textViewDateTime = convertView.findViewById(R.id.textViewCompletedDateTime);

        textViewTask.setText(task.task);
        textViewDateTime.setText("Created: " + task.dateTime + "\nCompleted: " + task.completedDateTime);

        return convertView;
    }
}