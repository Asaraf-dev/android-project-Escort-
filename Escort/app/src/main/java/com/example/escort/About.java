package com.example.escort;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.escort.databinding.ActivityAboutBinding;

public class About extends AppCompatActivity {

    private ActivityAboutBinding binding;
    ImageView backabout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout and bind the views
        binding = ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        // Initialize the ImageView after calling setContentView
        backabout = findViewById(R.id.backabout);

        // Set the onClickListener
        backab();
    }

    public void backab() {
        if (backabout != null) {
            backabout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Start the Setting activity
                    Intent intent = new Intent(About.this, Setting.class);
                    startActivity(intent);
                }
            });
        }
    }
}
