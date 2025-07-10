package com.example.escort;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;

import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;

import java.util.Arrays;

public class Map extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap gMap;
    private FusedLocationProviderClient fusedLocationClient;
    private EditText PlacesClient;
    Button navsetting, navmap, navhome;
    String pin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        PlacesClient = findViewById(R.id.search_edit_text);
        navsetting = findViewById(R.id.navsetting);
        navmap = findViewById(R.id.navmap);
        navhome = findViewById(R.id.navhome);

        //Retrieve the pin from sharedpref
        SharedPreferences spp = getApplicationContext().getSharedPreferences("SavePin", Context.MODE_PRIVATE);
        pin= spp.getString("Pin", "");

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.mapapi));
        }

        // Initialize the map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Set window insets for padding (optional)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Search functionality
        EditText searchEditText = findViewById(R.id.search_edit_text);
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            String searchQuery = v.getText().toString();
            if (!searchQuery.isEmpty()) {
                searchPlace(searchQuery);
            }
            return false;
        });
        nav();
        navhome();
    }

    // Search method for user query (this is where you would implement the actual search)
    private void searchPlace(String query) {
        // Create a Places client
        PlacesClient placesClient = Places.createClient(this);

        // Create the autocomplete prediction request
        FindAutocompletePredictionsRequest predictionsRequest = FindAutocompletePredictionsRequest.builder()
                .setQuery(query)
                .build();

        placesClient.findAutocompletePredictions(predictionsRequest)
                .addOnSuccessListener((FindAutocompletePredictionsResponse response) -> {
                    // Get the first prediction
                    AutocompletePrediction prediction = response.getAutocompletePredictions().get(0);
                    String placeId = prediction.getPlaceId();

                    // Get the details of the first place prediction
                    fetchPlaceDetails(placeId);
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Log.e("PlacesAPI", "Place search failed: " + e.getMessage());
                });
    }

    private void fetchPlaceDetails(String placeId) {
        // Create a Place ID request
        FetchPlaceRequest placeRequest = FetchPlaceRequest.newInstance(placeId, Arrays.asList(Place.Field.LAT_LNG));

        // Fetch the place details
        PlacesClient placesClient = Places.createClient(this);
        placesClient.fetchPlace(placeRequest)
                .addOnSuccessListener((FetchPlaceResponse response) -> {
                    // Get the place's location
                    LatLng location = response.getPlace().getLatLng();

                    // Add marker and move the camera
                    if (location != null) {
                        gMap.addMarker(new MarkerOptions().position(location).title(response.getPlace().getName()));
                        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("PlacesAPI", "Place details fetch failed: " + e.getMessage());
                });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;

        // Enable location tracking
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            gMap.setMyLocationEnabled(true);
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    // Method to get the current location
    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                gMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location"));
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void nav() {
        if (navsetting != null) {
            navsetting.setOnClickListener(view -> {
                if (pin.equals("")) {
                    Intent intent = new Intent(Map.this, SetPin.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(Map.this, CheckPin.class);
                    startActivity(intent);
                }
            });
        }
    }
    public void navhome() {
        if (navhome != null) {
            navhome.setOnClickListener(view -> {
                Intent intent = new Intent(Map.this, MainActivity.class);
                startActivity(intent);
            });
        }
    }
}
