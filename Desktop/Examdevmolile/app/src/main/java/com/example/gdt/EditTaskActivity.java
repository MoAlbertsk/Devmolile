package com.example.gdt;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditTaskActivity extends AppCompatActivity {

    private TaskDatabaseHelper dbHelper;
    private EditText editTextTitle;
    private EditText editTextDescription;
    private Spinner spinnerStatus;
    private Button buttonSaveTask;
    private long taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        dbHelper = new TaskDatabaseHelper(this);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        buttonSaveTask = findViewById(R.id.buttonSaveTask);

        Intent intent = getIntent();
        taskId = intent.getLongExtra("TASK_ID", -1);

        if (taskId != -1) {
            loadTaskData(taskId);
        }

        buttonSaveTask.setOnClickListener(v -> saveTask());
    }

    private void loadTaskData(long taskId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("tasks", null, "id = ?", new String[]{String.valueOf(taskId)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
            String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));

            editTextTitle.setText(title);
            editTextDescription.setText(description);
            spinnerStatus.setSelection(getStatusIndex(status));

            cursor.close();
        }
    }

    private int getStatusIndex(String status) {
        String[] statusArray = getResources().getStringArray(R.array.status_array);
        for (int i = 0; i < statusArray.length; i++) {
            if (statusArray[i].equals(status)) {
                return i;
            }
        }
        return 0;
    }

    private void saveTask() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        String status = spinnerStatus.getSelectedItem().toString();

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("description", description);
        values.put("status", status);

        int rowsAffected = db.update("tasks", values, "id = ?", new String[]{String.valueOf(taskId)});
        if (rowsAffected > 0) {
            Toast.makeText(this, "Tâche mise à jour", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Erreur lors de la mise à jour de la tâche", Toast.LENGTH_SHORT).show();
        }
    }
}

