package com.example.escort;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

//date and time
import java.text.SimpleDateFormat;
import java.util.Date;

//Exit Dialog Box
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import android.view.KeyEvent;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;

import android.os.Handler;
import android.os.MessageQueue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//for sms
import android.telephony.SmsManager;
import android.Manifest;

import androidx.core.view.WindowInsetsCompat;

import com.github.anastr.speedviewlib.PointerSpeedometer;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    // Declare buttons
    Button navsetting, navmap, navhome;
    private TextView avgspeed, maxspeedtv, speedtv;
    public TextView phone_numberview, speedlim;
    private LocationManager locationManager;
    private final Handler handler = new Handler();
    private Runnable resetspeedrunnable;
    private PointerSpeedometer pointerSpeedometer;
    private float maxspeed = 0.0f;
    private Location previousLocation;
    private final ArrayList<Float> speedlist = new ArrayList<>();
    SharedPreferences sp;
    int speedLimit;
    String speedlimite;
    private String MaxSPEED;

    //for sms
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static String EMERGENCY_NUMBER,pin;

    //database
    DBHelper DB;


    private final ActivityResultLauncher<String> activityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                    new ActivityResultCallback<Boolean>() {
                        @Override
                        public void onActivityResult(Boolean granted) {
                            if (granted) {  // Corrected condition
                                startLocationUpdates();
                            }
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        // Initialize buttons
        navsetting = findViewById(R.id.navsetting);
        navmap = findViewById(R.id.navmap);
        navhome = findViewById(R.id.navhome);
        DB = new DBHelper(this);

        //Check Login
        SharedPreferences spL = getSharedPreferences("LoginCheck", Context.MODE_PRIVATE);
        String UserData = spL.getString("UserData", "UserData");

        //Retrieve the phone Number from sharedpref
        SharedPreferences sph = getApplicationContext().getSharedPreferences("SaveNumber", Context.MODE_PRIVATE);
        EMERGENCY_NUMBER = sph.getString("PhNumber", "");

        //condition for Login Or Not
        if (UserData.equals("UserData")) {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        }
        //Retrieve the pin from sharedpref
        SharedPreferences spp = getApplicationContext().getSharedPreferences("SavePin", Context.MODE_PRIVATE);
        pin= spp.getString("Pin", "");



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });


        // Initialize text views and speedometer
        avgspeed = findViewById(R.id.avgspeed);
        maxspeedtv = findViewById(R.id.maxspeed);
        speedtv = findViewById(R.id.speedTV); // Make sure this matches the XML ID
        pointerSpeedometer = findViewById(R.id.speedometer);
        pointerSpeedometer.setMaxSpeed(200);
        phone_numberview = findViewById(R.id.Phone_Numberview);


        //Display Emergency Phone Number
        //phone_numberview.setText(EMERGENCY_NUMBER);


        //SpeedLimit Saver
        speedlim = findViewById(R.id.speedlim);   // TextView to display threshold
        SharedPreferences sp = getApplicationContext().getSharedPreferences("SpeedLimit", Context.MODE_PRIVATE);
        speedlimite = sp.getString("Speed", "60");
        speedlim.setText(speedlimite);
        speedLimit = Integer.parseInt(speedlimite);

        // Initialize LocationManager
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_CODE);
        } else {
            startLocationUpdates();
        }

        resetspeedrunnable = () -> {
            speedtv.setText(String.format(Locale.getDefault(), "Speed: %.2f km/h", 0.0f));
            pointerSpeedometer.speedTo(0);
        };

        // Setup navigation buttons
        nav();
        navmapto();
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


    private void startLocationUpdates() {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000L, (float) 1, (LocationListener) this);
        } catch (Exception e) {
            Toast.makeText(this, "Error listening for location updates", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private float calculateAverageSpeed() {
        float sum = 0.0f;
        for (float s : speedlist) {
            sum += s;
        }
        return sum / speedlist.size();
    }

    private boolean isSmsSent = false; // Flag to track if the SMS has been sent


    //Speed Calculate

    public void onLocationChanged(@NonNull Location location) {
        float speed = location.getSpeed() * 3.6f;  // Convert speed from m/s to km/h
        speedlist.add(speed);
        pointerSpeedometer.speedTo(speed);

        if (speed > maxspeed) {
            maxspeed = speed;
        }

        float avgspeedtv = calculateAverageSpeed();
        previousLocation = location;
        speedtv.setText(String.format(Locale.getDefault(), "%.2f km/h", speed));
        avgspeed.setText(String.format(Locale.getDefault(), "%.2f", avgspeedtv));
        maxspeedtv.setText(String.format(Locale.getDefault(), "%.2f", maxspeed));

        handler.removeCallbacks(resetspeedrunnable);
        handler.postDelayed(resetspeedrunnable, 3000);

        // Send SMS only once if speed exceeds the limit
        if (speed > speedLimit && !isSmsSent) {

            //to store a date and time
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
            String currentTime = sdf.format(new Date());

            // Create the message
            String MESSAGE = "Speed limit exceeded!" + "\n" +
                    "Location: " + previousLocation.getLatitude() + "," + previousLocation.getLongitude() + "\n" +
                    "MaxSpeed: " + speed + "\n" +
                    "Time: " + currentTime;
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(EMERGENCY_NUMBER, null, MESSAGE, null, null);

            isSmsSent = true;  // Set the flag to true after sending the SMS
           // handler.postDelayed(() -> isSmsSent = false, 900000);  // Reset the flag after 15 minutes

            //to store data in database
            Boolean checkinsertdata = DB.insertSpeed(MESSAGE);
            if (checkinsertdata == true) {
                Toast.makeText(this, "Data Stored", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Data Not Stored", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void nav() {
        if (navsetting != null) {
            navsetting.setOnClickListener(view -> {
                if (pin.equals("")) {
                    Intent intent = new Intent(MainActivity.this, SetPin.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(MainActivity.this, CheckPin.class);
                    startActivity(intent);
                }
            });
        }
    }

    public void navmapto() {
        if (navmap != null) {
            navmap.setOnClickListener(view -> {
                Intent intent = new Intent(MainActivity.this, Map.class);
                startActivity(intent);
            });
        }
    }
}
