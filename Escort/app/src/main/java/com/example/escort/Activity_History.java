package com.example.escort;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Activity_History extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<String> message;
    DBHelper DB;
    MyAdapter adapter;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        DB = new DBHelper(this);
        message = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerview);
        back = findViewById(R.id.back_button);

        adapter = new MyAdapter(message, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        displayData();
        BackButton();
    }

    private void displayData() {
        message.clear(); // Clear previous data to avoid duplication
        Cursor cursor = DB.getData();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                message.add(cursor.getString(0)); // Assuming first column has the message
            }
        }

        adapter.notifyDataSetChanged(); // Notify adapter after updating list
        cursor.close(); // Close cursor to avoid memory leaks
    }

    private void BackButton() {
        if (back != null) {
            back.setOnClickListener(view -> {
                Intent intent = new Intent(Activity_History.this, Setting.class);
                startActivity(intent);
            });
        }
    }
}
