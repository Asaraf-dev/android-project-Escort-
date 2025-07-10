package com.example.escort;

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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ForgotPin extends AppCompatActivity {
    Button verify;
    EditText email,password;
    TextView resultv;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_pin);

        //Declare buttons
        verify=findViewById(R.id.Verify);
        email=findViewById(R.id.Email);
        password=findViewById(R.id.password);
        resultv=findViewById(R.id.resultv);
        DB=new DBHelper(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        verifypin();

    }
    public void verifypin(){
        if (verify != null){
            verify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String user = email.getText().toString();
                    String pass = password.getText().toString();

                    if(user.equals("")||pass.equals(""))
                        resultv.setText("Please enter Email/Password");
                    else{
                        Boolean checkuserpass = DB.checkusernamepassword(user, pass);
                        if(checkuserpass==true){
                            Toast.makeText(ForgotPin.this, "Verify in successfull", Toast.LENGTH_SHORT).show();
                            Intent intent  = new Intent(getApplicationContext(), SetPin.class);
                            startActivity(intent);
                        }else{
                            resultv.setText("Invalid Email/Password");
                        }
                    }
                }
            });
        }
    }
}