package com.example.finalyearproject;

import static com.example.finalyearproject.PoliceData.getDataArray;
import static com.example.finalyearproject.PoliceData.mainActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.Batch;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.finalyearproject.databinding.ActivityGoogleMapsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class GoogleMaps extends FragmentActivity implements OnMapReadyCallback {

    private static int LengthOfDocuments;

    private static ArrayList<String> Crime = new ArrayList<>();
    private static ArrayList<String> Location = new ArrayList<>();
    private static ArrayList<GeoPoint> Position = new ArrayList<>();

    static DebugLog debug;

    public static GoogleMap mMap;
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

        mMap.getUiSettings().setZoomControlsEnabled(true);

        //Plug the values to create the circle
        LatLng Marker = new LatLng(51.51809, -0.11035);
        //mMap.addMarker(new MarkerOptions().position(Marker).title("Crime").snippet("On or near Holborn"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Marker));
        //Adding to Map

        WriteDB(mMap);
        updateCurrentLocation();

        enableMyLocation();
    }

    private void updateCurrentLocation(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
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
                Location.add(getDataArray.get(LocationPointer));
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
            markerOptions.title(Crime.get(counter)).position(geoPosition).snippet(Location.get(counter));
            map.addMarker(markerOptions);
        }
    }
}