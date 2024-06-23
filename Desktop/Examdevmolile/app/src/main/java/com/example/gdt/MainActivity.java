package com.example.gdt;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TaskDatabaseHelper dbHelper;
    private ArrayList<Task> tasks;
    private TaskAdapter adapter;
    private Spinner spinnerFilterStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listViewTasks = findViewById(R.id.listViewTasks);
        dbHelper = new TaskDatabaseHelper(this);
        FloatingActionButton buttonAddTask = findViewById(R.id.fabAddTask);
        spinnerFilterStatus = findViewById(R.id.spinnerFilterStatus);

        tasks = new ArrayList<>();
        adapter = new TaskAdapter(this, tasks);
        listViewTasks.setAdapter(adapter);

        buttonAddTask.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
            startActivity(intent);
        });

        spinnerFilterStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadTasks();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                loadTasks();
            }
        });

        listViewTasks.setOnItemClickListener((parent, view, position, id) -> {
            Task selectedTask = tasks.get(position);
            Intent intent = new Intent(MainActivity.this, EditTaskActivity.class);
            intent.putExtra("TASK_ID", selectedTask.getId());
            startActivity(intent);
        });

        loadTasks();
    }

    private void loadTasks() {
        tasks.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String filter = spinnerFilterStatus.getSelectedItem().toString();
        String selection = null;
        String[] selectionArgs = null;

        if (!filter.equals("statut")) {
            selection = "status = ?";
            selectionArgs = new String[]{filter};
        }

        Cursor cursor = db.query("tasks", null, selection, selectionArgs, null, null, null);

        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow("id"));
            String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
            String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
            Task task = new Task(id, title, description, status);
            tasks.add(task);
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTasks();
    }
}

