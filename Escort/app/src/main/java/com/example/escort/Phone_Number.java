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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.security.SecureRandom;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Phone_Number extends AppCompatActivity {

    EditText number;
    Button send_OTP;
    SharedPreferences sph;
    String Ph_number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);

        number = findViewById(R.id.Phone_Number);
        send_OTP = findViewById(R.id.vefiybtn);
        sph = getSharedPreferences("SaveNumber", Context.MODE_PRIVATE);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        send_OTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isValidIndianMobile(number.getText().toString()))
                {
                    Intent intent = new Intent(Phone_Number.this, Otp.class);
                    startActivity(intent);
                    Ph_number = number.getText().toString();

                    SharedPreferences.Editor editor = sph.edit();
                    editor.putString("PhNumber", Ph_number);
                    editor.commit();
                    Toast.makeText(Phone_Number.this, "Phone Number Saved", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(Phone_Number.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                }
            }
            public boolean isValidIndianMobile(String number) {
                String mobileNumberPattern = "^(\\+91[-\\s]?|0)?[6-9]\\d{9}$";
                return number.matches(mobileNumberPattern);
            }
        });
    }
}
