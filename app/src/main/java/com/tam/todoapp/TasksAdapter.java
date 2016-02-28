package com.tam.todoapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by toan on 2/28/2016.
 */
public class TasksAdapter extends ArrayAdapter<Task> {

    public TasksAdapter(Context context, List<Task> tasks) {
        super(context, 0, tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Task task = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_task, parent, false);
        }
        TextView tvStatus = (TextView)convertView.findViewById(R.id.tvStatus);
        TextView tvTaskName = (TextView)convertView.findViewById(R.id.tvTaskName);

        tvStatus.setText(task.getStatus());
        tvTaskName.setText(task.getTaskName());
        if (tvStatus.getText().toString().equals("Low")) {
            tvStatus.setTextColor(Color.MAGENTA);
        } else if (tvStatus.getText().toString().equals("Medium")) {
            tvStatus.setTextColor(Color.parseColor("#FFA500"));
        } else if (tvStatus.getText().toString().equals("High")) {
            tvStatus.setTextColor(Color.RED);
        } else {
            //tvStatus.setPaintFlags(tvStatus.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tvTaskName.setPaintFlags(tvTaskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        return convertView;
    }
}
