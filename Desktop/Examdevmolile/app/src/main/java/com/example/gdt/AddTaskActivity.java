package com.example.gdt;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddTaskActivity extends AppCompatActivity {

    private TaskDatabaseHelper dbHelper;
    private EditText editTextTitle;
    private EditText editTextDescription;
    private Spinner spinnerStatus;
    private Button buttonSaveTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        dbHelper = new TaskDatabaseHelper(this);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        buttonSaveTask = findViewById(R.id.buttonSaveTask);

        buttonSaveTask.setOnClickListener(v -> saveTask());
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

        try {
            long newRowId = db.insert("tasks", null, values);
            if (newRowId != -1) {
                Toast.makeText(this, "Tâche ajoutée", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Erreur lors de l'ajout de la tâche", Toast.LENGTH_SHORT).show();
                Log.e("AddTaskActivity", "Erreur lors de l'insertion de la tâche dans la base de données.");
            }
        } catch (Exception e) {
            Toast.makeText(this, "Erreur lors de l'ajout de la tâche", Toast.LENGTH_SHORT).show();
            Log.e("AddTaskActivity", "Exception lors de l'insertion de la tâche: " + e.getMessage());
        }
    }
}
