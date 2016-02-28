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
        void onFinishedDialog();
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

    public static AddEditTaskDialog newInstance(String title, Task task) {
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
        etTaskName = (EditText)view.findViewById(R.id.etTaskName);
        etTaskName.requestFocus();
        btnSave = (Button)view.findViewById(R.id.btnSave);
        spinnerStatus = (Spinner)view.findViewById(R.id.spStatus);
        populateTakssStatus();

//        String taskName = this.getArguments().getString("taskName");
//        etTaskName.setText(taskName);
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

                if (task != null) {
                    task.setTaskName(etTaskName.getText().toString());
                    task.setStatus(spinnerStatus.getSelectedItem().toString());
                    dbHelper.updateTask(task);
                } else {
                    task = new Task();
                    task.setTaskName(etTaskName.getText().toString());
                    task.setStatus(spinnerStatus.getSelectedItem().toString());
                    dbHelper.addTask(task);
                }
                AddEditTaskDialogListener activity = (AddEditTaskDialogListener)getActivity();
                activity.onFinishedDialog();
                dismiss();
            }
        });
    }

    private int getSelectedStatusIndex(String status) {
        for (int i = 0; i < Task.ARRAY_STATUS.length; i++)
        {
            if (Task.ARRAY_STATUS[i].equals(status)) {
                return i;
            }
        }
        return 0;
    }

    private void populateTakssStatus() {
        ArrayAdapter<String> adapterStatus = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, Task.ARRAY_STATUS);
        // Specify the layout to use when the list of choices appears
        adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerStatus.setAdapter(adapterStatus);
    }
}
