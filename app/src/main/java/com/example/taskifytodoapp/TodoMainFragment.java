package com.example.taskifytodoapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TodoMainFragment extends Fragment implements TaskAdapter.TaskButtonClickListener {
    private EditText editTextTask;
    private Button buttonAdd, buttonViewCompleted;
    private ListView listViewTasks;
    private ArrayList<TaskAdapter.TaskItem> tasks;
    private ArrayList<TaskAdapter.TaskItem> completedTasks;
    private TaskAdapter adapter;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo_main, container, false);

        editTextTask = view.findViewById(R.id.editTextTask);
        buttonAdd = view.findViewById(R.id.buttonAdd);
        buttonViewCompleted = view.findViewById(R.id.buttonViewCompleted);
        listViewTasks = view.findViewById(R.id.listViewTasks);

        tasks = new ArrayList<>();
        completedTasks = new ArrayList<>();
        sharedPreferences = requireActivity().getSharedPreferences("TodoApp", Context.MODE_PRIVATE);

        loadTasks();
        loadCompletedTasks();

        adapter = new TaskAdapter(requireContext(), tasks, this);
        listViewTasks.setAdapter(adapter);

        buttonAdd.setOnClickListener(v -> {
            String task = editTextTask.getText().toString();
            if (!task.isEmpty()) {
                addNewTask(task);
                editTextTask.setText("");
            }
        });

        buttonViewCompleted.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("completedTasks", completedTasks);
            Navigation.findNavController(view).navigate(R.id.completedTasksFragment, bundle);
        });

        return view;
    }

    private void addNewTask(String taskText) {
        String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());
        tasks.add(new TaskAdapter.TaskItem(taskText, currentDateTime));
        adapter.notifyDataSetChanged();
        saveTasksWithDateTime();
    }

    @Override
    public void onCompleteClick(int position) {
        TaskAdapter.TaskItem task = tasks.remove(position);
        String completedDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());
        completedTasks.add(new TaskAdapter.TaskItem(task.task, task.dateTime, completedDateTime));
        adapter.notifyDataSetChanged();
        saveTasksWithDateTime();
        saveCompletedTasksWithDateTime();
    }

    @Override
    public void onRemoveClick(int position) {
        tasks.remove(position);
        adapter.notifyDataSetChanged();
        saveTasksWithDateTime();
    }


    private void saveTasksWithDateTime() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        StringBuilder sb = new StringBuilder();
        for (TaskAdapter.TaskItem task : tasks) {
            sb.append(task.task).append("||").append(task.dateTime).append("||").append(",,");
        }
        editor.putString("tasks", sb.toString());
        editor.apply();
    }

    private void saveCompletedTasksWithDateTime() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        StringBuilder sb = new StringBuilder();
        for (TaskAdapter.TaskItem task : completedTasks) {
            sb.append(task.task).append("||").append(task.dateTime).append("||").append(task.completedDateTime).append(",,");
        }
        editor.putString("completedTasks", sb.toString());
        editor.apply();
    }

    private void loadTasks() {
        String savedTasks = sharedPreferences.getString("tasks", "");
        if (!savedTasks.isEmpty()) {
            String[] taskArray = savedTasks.split(",,");
            for (String taskData : taskArray) {
                if (!taskData.isEmpty()) {
                    String[] parts = taskData.split("\\|\\|");
                    if (parts.length >= 2) {
                        tasks.add(new TaskAdapter.TaskItem(parts[0], parts[1]));
                    }
                }
            }
        }
    }

    private void loadCompletedTasks() {
        String savedTasks = sharedPreferences.getString("completedTasks", "");
        if (!savedTasks.isEmpty()) {
            String[] taskArray = savedTasks.split(",,");
            for (String taskData : taskArray) {
                if (!taskData.isEmpty()) {
                    String[] parts = taskData.split("\\|\\|");
                    if (parts.length >= 3) {
                        completedTasks.add(new TaskAdapter.TaskItem(parts[0], parts[1], parts[2]));
                    }
                }
            }
        }
    }
}