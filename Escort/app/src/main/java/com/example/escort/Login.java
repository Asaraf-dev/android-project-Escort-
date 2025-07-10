package com.example.escort;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class Login extends AppCompatActivity {

    EditText username, password ;
    TextView createaccount, forgot_password;
    Button btnlogin;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.Email);
        password = (EditText) findViewById(R.id.Password);
        btnlogin = (Button) findViewById(R.id.loginbutton);
        createaccount = (TextView) findViewById(R.id.create_account);
       forgot_password = (TextView) findViewById(R.id.forgot_password);
        DB = new DBHelper(this);
        SharedPreferences spL = getSharedPreferences("LoginCheck", Context.MODE_PRIVATE);


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user = username.getText().toString();
                String pass = password.getText().toString();

                if(user.equals("")||pass.equals(""))
                    Toast.makeText(Login.this, "Please enter Email/Password", Toast.LENGTH_SHORT).show();
                else{
                    Boolean checkuserpass = DB.checkusernamepassword(user, pass);

                    //Login Check
                    SharedPreferences.Editor editor = spL.edit();
                    editor.putString("UserData", user);
                    editor.apply(); // Use apply() for asynchronous saving


                    if(checkuserpass==true){
                        Toast.makeText(Login.this, "Sign in successfull", Toast.LENGTH_SHORT).show();
                        Intent intent  = new Intent(getApplicationContext(), Phone_Number.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(Login.this, "Invalid Email/Password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
            }
        });
        forgot_password();
    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        // Create an AlertDialog.Builder instance
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)  // Prevents closing the dialog by tapping outside
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Close the app completely
                        finishAffinity();  // Close all activities and end the app
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Do nothing when the user cancels the dialog
                        dialog.dismiss();
                    }
                });

        // Create and show the dialog
        AlertDialog alert = builder.create();
        alert.show();
    }
    public void forgot_password() {
        if (forgot_password != null) {
            forgot_password.setOnClickListener(view -> {
                Intent intent = new Intent(Login.this, Changepin.class);
                startActivity(intent);
            });
        }
    }




}
