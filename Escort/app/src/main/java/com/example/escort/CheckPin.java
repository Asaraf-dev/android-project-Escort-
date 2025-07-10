package com.example.escort;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class CheckPin extends AppCompatActivity {
    // Declare buttons
    Button enterbtn;
    EditText enterpin;
    TextView resultview,forgotpin;
    String pin,checkpin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_check_pin);
        enterbtn=findViewById(R.id.enterbtn);
        enterpin=findViewById(R.id.enterpin);
        resultview=findViewById(R.id.resultview);
        forgotpin=findViewById(R.id.forgotpin);


        //Retrieve the pin from sharedpref
        SharedPreferences spp = getApplicationContext().getSharedPreferences("SavePin", Context.MODE_PRIVATE);
        pin= spp.getString("Pin", "");
        enterp();
        forgotpi();
    }
    public void forgotpi(){
        if (forgotpin != null){
            forgotpin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(CheckPin.this, ForgotPin.class);
                    startActivity(intent);
                    }
            });
        }
    }
    public void enterp() {
        if (enterbtn != null) {
            enterbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkpin=enterpin.getText().toString();
                    if (pin.equals(checkpin)) {
                        Intent intent = new Intent(CheckPin.this, Setting.class);
                        startActivity(intent);
                    } else {
                        resultview.setText("Incorrect Pin");
                    }

                }
            });
        }
    }
}