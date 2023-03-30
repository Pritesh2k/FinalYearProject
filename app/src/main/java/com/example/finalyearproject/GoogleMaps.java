package com.example.finalyearproject;

import static com.example.finalyearproject.PoliceData.getDataArray;
import static com.example.finalyearproject.PoliceData.mainActivity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.finalyearproject.databinding.ActivityGoogleMapsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GoogleMaps extends FragmentActivity implements OnMapReadyCallback {

    private static int LengthOfDocuments;

    private static ArrayList<String> Crime = new ArrayList<>();
    private static ArrayList<String> Location_geoPoints = new ArrayList<>();
    public static ArrayList<GeoPoint> Position = new ArrayList<>();

    static DebugLog debug;

    public static GoogleMap mMap;
    private ActivityGoogleMapsBinding binding;

    public static Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;

    public static LatLng USER_CURRENT_LOCATION;

    Button homePage_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGoogleMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        LengthOfDocuments = debug.Documents.size();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        homePage_button = (Button) findViewById(R.id.homepage_button);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Plug the values to create the circle
        LatLng Marker = new LatLng(51.51809, -0.11035);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Marker));

        WriteDB(mMap);
        updateCurrentLocation();
        enableMyLocation();

        homePage_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PoliceData.updateClosestCrime = true;
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateCurrentLocation(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getUserCurrentPosition();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updateCurrentLocation();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void enableMyLocation(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            String[] Permissions = {"android.permission.ACCESS_FINE_LOCATION"};
            ActivityCompat.requestPermissions(this, Permissions, 200);
        }
    }

    public void getUserCurrentPosition(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        currentLocation = location;
                        USER_CURRENT_LOCATION = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                        System.out.println(USER_CURRENT_LOCATION);

                        //
                        PoliceData.CheckCircles(location);
                    }
                }
            });
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }
    }

    private void WriteDB(GoogleMap map) {
        //Initialising pointers for each column in the data array
        int CrimePointer = 0;
        int LocationPointer = 1;
        int LongitudePointer = 2;
        int LatitudePointer = 3;
        int OutcomePointer = 4;
        //Iterating through each crime and its values
        while (OutcomePointer < getDataArray.size()){
            //Checking for empty values
            if (!getDataArray.get(CrimePointer).equals("") || !getDataArray.get(LocationPointer).equals("No Location")
            || !getDataArray.get(LongitudePointer).equals("") || !getDataArray.get(LatitudePointer).equals("")) {
                //if all the required values are available, then it is added to their respected arraylists
                Crime.add(getDataArray.get(CrimePointer));
                Location_geoPoints.add(getDataArray.get(LocationPointer));
                GeoPoint geoPoint = new GeoPoint(Double.parseDouble(getDataArray.get(LatitudePointer)), Double.parseDouble(getDataArray.get(LongitudePointer)));
                Position.add(geoPoint);
                //Incrementing pointer by 6 to fetch the next value (5 values away from the current position + 1 due to an empty space in the text view)
                CrimePointer += 6;
                LocationPointer += 6;
                LongitudePointer += 6;
                LatitudePointer += 6;
                OutcomePointer += 6;
            }
        }
        //iterating through each Crime, fetching its values from the other arraylists
        for (int counter = 0; counter < Crime.size(); counter++){
            //Assigning the crime and its values to the Marker and its properties
            MarkerOptions markerOptions = new MarkerOptions();
            LatLng geoPosition = new LatLng(Position.get(counter).getLatitude(), Position.get(counter).getLongitude());
            markerOptions.title(Crime.get(counter)).position(geoPosition).snippet(Location_geoPoints.get(counter));
            //Switch case checking the crime and appointing a colour to the marker
            switch (Objects.requireNonNull(markerOptions.getTitle())){
                case "Theft from the person":
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                    break;
                case "Bicycle theft":
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    break;
                case "Other theft":
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                    break;
                case "Robbery":
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    break;
                case "Burglary":
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                    break;
                case "Shoplifting":
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                    break;
                case "Vehicle crime":
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                    break;
                case "Drugs":
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                    break;
                case "Anti-social behaviour":
                    //Red
                    break;
                case "Violence and sexual offences":
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                    break;
                case "Criminal damage and arson":
                    //Red
                    break;
                case "Other crime":
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                    break;
                case "Possession of weapons":
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    break;
                case "Public order":
                    //Red
                    break;
                default:
                    break;
            }
            //Adding marker to map
            map.addMarker(markerOptions);
            PoliceData.markerList.add(markerOptions);

            //Add circles
            CircleOptions circleOptions = new CircleOptions()
                    .center(geoPosition)
                    .radius(100)
                    .strokeWidth(1.5f);
            map.addCircle(circleOptions);
            PoliceData.circleList.add(circleOptions);
        }
    }
}