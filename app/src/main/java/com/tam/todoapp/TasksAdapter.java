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

    private static class ViewHolder {
        TextView tvStatus;
        TextView tvTaskName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Task task = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_task, parent, false);
            viewHolder.tvStatus = (TextView) convertView.findViewById(R.id.tvStatus);
            viewHolder.tvTaskName = (TextView) convertView.findViewById(R.id.tvTaskName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String taskName = task.getTaskName();
        String taskStatus = task.getStatus();
        viewHolder.tvTaskName.setText(taskName);
        viewHolder.tvStatus.setText(taskStatus);
        viewHolder.tvTaskName.setPaintFlags(viewHolder.tvTaskName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        viewHolder.tvStatus.setTextColor(Color.BLACK);
        if (taskStatus.equals("Low")) {
            viewHolder.tvStatus.setTextColor(Color.MAGENTA);
        } else if (taskStatus.equals("Medium")) {
            viewHolder.tvStatus.setTextColor(Color.parseColor("#FFA500"));
        } else if (taskStatus.equals("High")) {
            viewHolder.tvStatus.setTextColor(Color.RED);
        } else {
            viewHolder.tvTaskName.setPaintFlags(viewHolder.tvTaskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        return convertView;
    }
}
