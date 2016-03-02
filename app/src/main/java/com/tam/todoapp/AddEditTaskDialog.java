package com.tam.todoapp;

//import android.app.DialogFragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by toan on 2/28/2016.
 */
public class AddEditTaskDialog extends DialogFragment {

    public interface AddEditTaskDialogListener {
        void onFinishEditingTask();

        /**
         *
         * @param task A task object after adding to data source (database, file system...) successfully
         */
        void onFinishAddingTask(Task task);
    }

    private String title;
    private Task task;
    private EditText etTaskName;
    private Button btnSave;
    private ToDoItemsDbHelper dbHelper;
    private Spinner spinnerStatus;

    public AddEditTaskDialog() {
        this.title = "Task";
        dbHelper = ToDoItemsDbHelper.getInstance(getActivity());
    }

    public static AddEditTaskDialog newInstance(Task task) {
        AddEditTaskDialog dialog = new AddEditTaskDialog();
        dialog.setTask(task);
        return dialog;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.add_edit_task, container, false);
        getDialog().setTitle(title);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etTaskName = (EditText) view.findViewById(R.id.etTaskName);
        etTaskName.requestFocus();
        btnSave = (Button) view.findViewById(R.id.btnSave);
        spinnerStatus = (Spinner) view.findViewById(R.id.spStatus);
        populateTasksStatus();
        if (task != null) {
            // edit task
            String taskName = this.task.getTaskName();
            etTaskName.setText(taskName);
            etTaskName.setSelection(etTaskName.getText().length());
            spinnerStatus.setSelection(getSelectedStatusIndex(task.getStatus()));
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddEditTaskDialogListener activity = (AddEditTaskDialogListener) getActivity();
                if (task != null) {
                    String taskName = etTaskName.getText().toString().trim();
                    if (!taskName.isEmpty()) {
                        task.setTaskName(etTaskName.getText().toString());
                        task.setStatus(spinnerStatus.getSelectedItem().toString());
                        dbHelper.updateTask(task);
                        activity.onFinishEditingTask();
                    }
                } else {
                    task = new Task();
                    String taskName = etTaskName.getText().toString().trim();
                    if (!taskName.isEmpty()) {
                        task.setTaskName(taskName);
                        task.setStatus(spinnerStatus.getSelectedItem().toString());
                        dbHelper.addTask(task);
                        activity.onFinishAddingTask(task);
                    }
                }
                dismiss();
            }
        });
    }

    private int getSelectedStatusIndex(String status) {
        for (int i = 0; i < Task.ARRAY_STATUS.length; i++) {
            if (Task.ARRAY_STATUS[i].equals(status)) {
                return i;
            }
        }
        return 0;
    }

    private void populateTasksStatus() {
        ArrayAdapter<String> adapterStatus = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, Task.ARRAY_STATUS);
        adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapterStatus);
    }
}
