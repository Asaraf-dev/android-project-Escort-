package com.example.escort;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Changepin extends AppCompatActivity {

    EditText Email, New_password, Conforim_password;
    Button Verify;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_changepin);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Email = findViewById(R.id.Email);
        New_password = findViewById(R.id.new_password);
        Conforim_password = findViewById(R.id.conform_password);
        Verify = findViewById(R.id.Verify);
        DB = new DBHelper(this);

        Verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = Email.getText().toString().trim();
                String newPassword = New_password.getText().toString().trim();
                String confirmPassword = Conforim_password.getText().toString().trim();

                if (username.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(Changepin.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean checkUser = DB.checkusername(username);
                if (checkUser) {
                    if (newPassword.equals(confirmPassword)) {
                        if (newPassword.length() < 8) {
                            Toast.makeText(Changepin.this, "Password must be at least 8 characters long", Toast.LENGTH_SHORT).show();
                        }
                        else if (!newPassword.matches("^[a-zA-Z0-9]+$"))  {
                            Toast.makeText(Changepin.this, "Password must contain only alphanumeric characters", Toast.LENGTH_SHORT).show();
                        }
                        else {

                            int isPasswordUpdated = DB.updatePassword(username, newPassword);
                            if (isPasswordUpdated > 0) {
                                Toast.makeText(Changepin.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Changepin.this, Login.class));
                                finish();
                            } else {
                                Toast.makeText(Changepin.this, "Failed to update password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(Changepin.this, "Password mismatch", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Changepin.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
