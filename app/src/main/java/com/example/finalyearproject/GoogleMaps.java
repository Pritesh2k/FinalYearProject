package com.example.finalyearproject;

import static com.example.finalyearproject.PoliceData.getDataArray;
import static com.example.finalyearproject.PoliceData.mainActivity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.finalyearproject.databinding.ActivityGoogleMapsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class GoogleMaps extends FragmentActivity implements OnMapReadyCallback {

    private static int LengthOfDocuments;

    private static ArrayList<String> Crime = new ArrayList<>();
    private static ArrayList<String> Location_geoPoints = new ArrayList<>();
    public static ArrayList<GeoPoint> Position = new ArrayList<>();
    static List<CircleOptions> circleList = new ArrayList<>();

    static DebugLog debug;

    public static GoogleMap mMap;
    private ActivityGoogleMapsBinding binding;

    public static Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;

    private static LatLng USER_CURRENT_LOCATION;

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

        mMap.getUiSettings().setZoomControlsEnabled(true);

        //Plug the values to create the circle
        LatLng Marker = new LatLng(51.51809, -0.11035);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Marker));

        WriteDB(mMap);
        updateCurrentLocation();
        enableMyLocation();
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

    private void enableMyLocation(){
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
                        CheckCircles(location);
                    }
                }
            });
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }
    }

    private void WriteDB(GoogleMap map) {

        int CrimePointer = 0;
        int LocationPointer = 1;
        int LongitudePointer = 2;
        int LatitudePointer = 3;
        int OutcomePointer = 4;

        while (OutcomePointer < getDataArray.size()){

            if (!getDataArray.get(CrimePointer).equals("") || !getDataArray.get(LocationPointer).equals("No Location")
            || !getDataArray.get(LongitudePointer).equals("") || !getDataArray.get(LatitudePointer).equals("")) {

                Crime.add(getDataArray.get(CrimePointer));
                Location_geoPoints.add(getDataArray.get(LocationPointer));
                GeoPoint geoPoint = new GeoPoint(Double.parseDouble(getDataArray.get(LatitudePointer)), Double.parseDouble(getDataArray.get(LongitudePointer)));
                Position.add(geoPoint);

                CrimePointer += 6;
                LocationPointer += 6;
                LongitudePointer += 6;
                LatitudePointer += 6;
                OutcomePointer += 6;
            }
        }

        for (int counter = 0; counter < Crime.size(); counter++){
            MarkerOptions markerOptions = new MarkerOptions();
            LatLng geoPosition = new LatLng(Position.get(counter).getLatitude(), Position.get(counter).getLongitude());
            markerOptions.title(Crime.get(counter)).position(geoPosition).snippet(Location_geoPoints.get(counter));
            map.addMarker(markerOptions);

            //Add circles
            CircleOptions circleOptions = new CircleOptions()
                    .center(geoPosition)
                    .radius(100)
                    .fillColor(Color.parseColor("#500084d3"));
            map.addCircle(circleOptions);
            circleList.add(circleOptions);
        }
    }

    public static void CheckCircles(Location location) {
        for (int counter = 0; counter < circleList.size(); counter++) {
            Location circleLocation = new Location("");
            circleLocation.setLatitude(circleList.get(counter).getCenter().latitude);
            circleLocation.setLongitude(circleList.get(counter).getCenter().longitude);

            float distanceInMeters = location.distanceTo(circleLocation);

            if (distanceInMeters <= 100) {
                // User's location is within 100m of the circle's center
                System.out.println(circleLocation.getLatitude() + " | " + circleLocation.getLongitude() + " | " + distanceInMeters + " | Dangerous");
            } else {
                // User's location is more than 100m away from the circle's center
                System.out.println(circleLocation.getLatitude() + " | " + circleLocation.getLongitude() + " | " + distanceInMeters);
            }
        }
    }
}