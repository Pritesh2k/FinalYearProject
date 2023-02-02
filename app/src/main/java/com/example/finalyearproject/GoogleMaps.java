package com.example.finalyearproject;

import static android.Manifest.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.finalyearproject.databinding.ActivityGoogleMapsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;

public class GoogleMaps extends FragmentActivity implements OnMapReadyCallback {

    private static int CrimePointer = 0;
    private static int LocationPointer = 1;
    private static int LongitudePointer = 2;
    private static int LatitudePointer = 3;
    private static int OutcomePointer = 4;
    private static int LengthOfDocuments;

    static DebugLog debug;

    private GoogleMap mMap;
    private ActivityGoogleMapsBinding binding;

    public static Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;

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

        //Loop through the array
        while (OutcomePointer < LengthOfDocuments ||
                CrimePointer < LengthOfDocuments ||
                LocationPointer < LengthOfDocuments ||
                LongitudePointer < LengthOfDocuments ||
                LatitudePointer < LengthOfDocuments){
//            System.out.println(debug.Documents.get(CrimePointer));
//            System.out.println(debug.Documents.get(LocationPointer));
//            System.out.println(debug.Documents.get(LongitudePointer));
//            System.out.println(debug.Documents.get(LatitudePointer));
//            System.out.println(debug.Documents.get(OutcomePointer));
            //Push to the function
            CreateCrimeMapObjects(googleMap, debug.Documents.get(CrimePointer),
                    debug.Documents.get(OutcomePointer),
                    debug.Documents.get(LocationPointer),
                    Double.valueOf(debug.Documents.get(LongitudePointer)),
                    Double.valueOf(debug.Documents.get(LatitudePointer)));
            //Set pointers to current set of values
            CrimePointer += 5;
            LocationPointer += 5;
            LongitudePointer += 5;
            LatitudePointer += 5;
            OutcomePointer += 5;
        }

        enableMyLocation();

    }

    private void enableMyLocation(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            String Permissions [] = {"android.permission.ACCESS_FINE_LOCATION"};
            ActivityCompat.requestPermissions(this, Permissions, 200);
        }
    }

    private static void CreateCrimeMapObjects(GoogleMap mMap, String Crime, String Outcome, String Location, Double Longitude, Double Latitude){

        // Add a marker and move the camera
        //Plug the values to create the circle
        LatLng Marker = new LatLng(Latitude, Longitude);
        mMap.addMarker(new MarkerOptions().position(Marker).title(Crime).snippet(Location));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Marker));

        //Circles
        CircleOptions Circle = new CircleOptions();
        Circle.center(new LatLng(Latitude, Longitude));
        Circle.radius(1000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Circle.fillColor(Color.argb(0.25F, 184F, 0F, 0F));
        }
        Circle.strokeColor(Color.RED);

        //Adding to Map
        //mMap.addCircle(Circle);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Marker));
    }
}