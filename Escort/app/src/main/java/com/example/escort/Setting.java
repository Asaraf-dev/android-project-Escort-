package com.example.escort;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Setting extends AppCompatActivity {
    Button logout, about, save, changenumber, activityhistory;
    ImageView backsetting;
    String savedSpeed;
    EditText treshold;
    TextView speedlim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Ensure EdgeToEdge mode is correctly supported
        setContentView(R.layout.activity_setting);

        // Initialize views
        backsetting = findViewById(R.id.BackSetting);
        logout = findViewById(R.id.logout);
        about = findViewById(R.id.about);
        treshold = findViewById(R.id.threshold);
        speedlim = findViewById(R.id.speedLim);
        save = findViewById(R.id.savebtn);
        changenumber = findViewById(R.id.changenumber);
        activityhistory = findViewById(R.id.ActivityHistory);


        // Handle system bars insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Retrieve the saved threshold value from SharedPreferences
        SharedPreferences sp = getSharedPreferences("SpeedLimit", Context.MODE_PRIVATE);

        save.setOnClickListener(view -> {
            int treshold1 = Integer.parseInt(treshold.getText().toString());
            if (treshold.getText().toString().isEmpty())
                Toast.makeText(Setting.this, "Please Enter Speed Limit", Toast.LENGTH_SHORT).show();
            else {
                if (treshold1 > 90 || treshold1 < 40) {
                    Toast.makeText(Setting.this, "Speed Limit must be between 40 and 90", Toast.LENGTH_SHORT).show();
                } else {

                    savedSpeed = treshold.getText().toString();

                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("Speed", savedSpeed);
                    editor.apply(); // Use apply() for asynchronous saving
                    Toast.makeText(Setting.this, "Speed Limit Saved", Toast.LENGTH_SHORT).show();
                    speedlim.setText(savedSpeed);
                }
            }

        });

        // Set up button listeners
        back();
        logout();
        about();
        changeNumber();
        ActivityHistory();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Setting.this, MainActivity.class);
        startActivity(intent);
    }

    public void back() {
        if (backsetting != null) {
            backsetting.setOnClickListener(view -> {
                Intent intent = new Intent(Setting.this, MainActivity.class);
                startActivity(intent);
            });
        }
    }

    public void logout() {
        if (logout != null) {

            logout.setOnClickListener(view -> {
                //clear the data store in sharedpref
                String Logout = "UserData";
                SharedPreferences spL = getSharedPreferences("LoginCheck", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = spL.edit();
                editor.putString("UserData", Logout);
                editor.apply(); // Use apply() for asynchronous saving
                Intent intent = new Intent(Setting.this, Login.class);
                startActivity(intent);
            });
        }
    }

    public void about() {
        if (about != null) {
            about.setOnClickListener(view -> {
                Intent intent = new Intent(Setting.this, About.class);
                startActivity(intent);
            });
        }
    }

    public void changeNumber() {
        if (changenumber != null) {
            changenumber.setOnClickListener(view -> {
                Intent intent = new Intent(Setting.this, Phone_Number.class);
                startActivity(intent);
            });
        }
    }

    public void ActivityHistory() {
        if (activityhistory != null) {
            activityhistory.setOnClickListener(view -> {
                Intent intent = new Intent(Setting.this, Activity_History.class);
                startActivity(intent);
            });
        }
    }
}
