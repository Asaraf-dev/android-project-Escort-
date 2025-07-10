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

public class ConfirmPin extends AppCompatActivity {
    Button confirmbtn;
    EditText confirmpin;
    TextView result;
    String savedpin, currentpin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_confirm_pin);

        confirmpin = findViewById(R.id.Confirmpin);
        result = findViewById(R.id.resultview);
        confirmbtn = findViewById(R.id.Confirmbtn);

        SharedPreferences spp = getSharedPreferences("SavePin", Context.MODE_PRIVATE);
        savedpin = spp.getString("Pin", "");

        confirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentpin = confirmpin.getText().toString().trim();
                if (currentpin.equals(savedpin)) {
                    Intent intent = new Intent(ConfirmPin.this, Setting.class);
                    startActivity(intent);
                } else {
                    result.setText("Incorrect Pin");
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //clear the data store in sharedpref
        String Pin="";
        SharedPreferences spL = getSharedPreferences("SavePin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spL.edit();
        editor.putString("Pin", Pin);
        editor.apply(); // Use apply() for asynchronous saving

    }
}
