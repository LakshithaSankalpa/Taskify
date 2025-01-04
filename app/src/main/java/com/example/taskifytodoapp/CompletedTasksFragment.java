package com.example.taskifytodoapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.widget.ListView;
import java.util.ArrayList;

public class CompletedTasksFragment extends Fragment {
    private ArrayList<TaskAdapter.TaskItem> completedTasks;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completed_tasks, container, false);

        completedTasks = getArguments() != null ? getArguments().getParcelableArrayList("completedTasks") : new ArrayList<>();
        ListView listView = view.findViewById(R.id.listViewCompletedTasks);

        CompletedTaskAdapter adapter = new CompletedTaskAdapter(requireContext(), completedTasks);
        listView.setAdapter(adapter);

        return view;
    }
}