package com.example.gdt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class TaskAdapter extends ArrayAdapter<Task> {
    private Context mContext;
    private ArrayList<Task> tasks;

    public TaskAdapter(Context context, ArrayList<Task> list) {
        super(context, 0, list);
        mContext = context;
        tasks = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.task_item, parent, false);

        Task currentTask = tasks.get(position);

        LinearLayout layout = listItem.findViewById(R.id.taskItemLayout);
        View statusIndicator = listItem.findViewById(R.id.statusIndicator);

        switch (currentTask.getStatus()) {
            case "Completed":
                layout.setBackgroundResource(R.drawable.border_completed);
                statusIndicator.setBackgroundResource(R.drawable.circle_completed);
                break;
            case "In Progress":
                layout.setBackgroundResource(R.drawable.border_in_progress);
                statusIndicator.setBackgroundResource(R.drawable.circle_in_progress);
                break;
            case "Not Started":
                layout.setBackgroundResource(R.drawable.border_not_started);
                statusIndicator.setBackgroundResource(R.drawable.circle_not_started);
                break;
            default:
                layout.setBackgroundResource(R.drawable.border_default);
                statusIndicator.setBackgroundResource(R.drawable.circle_default);
                break;
        }

        TextView title = listItem.findViewById(R.id.taskTitle);
        title.setText(currentTask.getTitle());

        return listItem;
    }
}

