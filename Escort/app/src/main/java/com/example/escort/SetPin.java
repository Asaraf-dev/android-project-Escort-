package com.example.escort;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class SetPin extends AppCompatActivity {
    Button set;
    EditText pin;
    TextView result;
    SharedPreferences spp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_set_pin);

        set = findViewById(R.id.setbtn);
        pin = findViewById(R.id.pin);
        result = findViewById(R.id.resultview);
        spp = getSharedPreferences("SavePin", Context.MODE_PRIVATE);

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pinc = pin.getText().toString().trim();
                if (pinc.length() == 4) {
                    SharedPreferences.Editor editor = spp.edit();
                    editor.putString("Pin", pinc);
                    editor.apply(); // Use apply() for asynchronous saving
                    Toast.makeText(SetPin.this, "PIN SAVED", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SetPin.this, ConfirmPin.class);
                    startActivity(intent);
                } else {
                    result.setText("Enter PIN of 4 digits");
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

    /* public void logout() {
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
    }*/
}
