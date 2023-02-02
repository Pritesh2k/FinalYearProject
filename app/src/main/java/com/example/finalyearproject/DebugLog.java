package com.example.finalyearproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DebugLog extends AppCompatActivity {

    static MainActivity mainActivity;
    static GoogleMaps googleMaps;
    public static TextView DebugText;

    private static TextView UserLocation;
    public static ArrayList<String> Documents = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_log);

        DebugText = (TextView) findViewById(R.id.Debug_Testing);
        DebugText.setText("");
        DebugText.setMovementMethod(new ScrollingMovementMethod());

        UserLocation = (TextView) findViewById(R.id.UserLocation);

        GetFirebaseDocuments();
    }

    public void GetFirebaseDocuments(){

        //Reading from the Firebase DB
        mainActivity.db.collection("November-2022")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (!document.getString("Location").equals("No Location") || !document.getString("Longitude").equals("") || !document.getString("Latitude").equals("")) {

                                    Documents.add(document.getString("Crime Type"));
                                    Documents.add(document.getString("Location"));
                                    Documents.add(document.getString("Longitude"));
                                    Documents.add(document.getString("Latitude"));
                                    Documents.add(document.getString("Last Outcome Catagory"));

                                    DebugText.append(Documents.get(0) + "\n" + Documents.get(1) + "\n" + Documents.get(2) + "\n" + Documents.get(3) + "\n" + Documents.get(4) + "\n\n");
                                } else{
                                    continue;
                                }
                            }
                        } else {
                            Toast.makeText(mainActivity.getApplicationContext(),"UnSuccessful", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}