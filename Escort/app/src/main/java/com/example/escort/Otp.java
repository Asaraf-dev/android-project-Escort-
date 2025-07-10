package com.example.escort;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.security.SecureRandom;

public class Otp extends AppCompatActivity {

    EditText otpinput;
    Button verifiybtn;
    TextView resendotp;
    ProgressBar progressBar;
    String phoneNumber;

    private static final int SMS_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        otpinput = findViewById(R.id.otp);
        verifiybtn = findViewById(R.id.vefiybtn);
        progressBar = findViewById(R.id.progress_bar);
        resendotp = findViewById(R.id.resend_otp);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });


        // Retrieve the phone number from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("SaveNumber", Context.MODE_PRIVATE);
        phoneNumber = sharedPreferences.getString("PhNumber", null);

        verifiybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (otpinput.getText().toString().equals(phoneNumber)) {
                    Intent intent = new Intent(Otp.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(Otp.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Otp.this, Phone_Number.class);
                    startActivity(intent);
                }
            }
        });
    }

}
