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
import android.os.Debug;
import android.provider.ContactsContract;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.util.ArrayUtils;
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

    static PoliceData policeData;

    public static TextView DebugText;

    public static ArrayList<String> Documents = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_log);

        DebugText = (TextView) findViewById(R.id.Debug_Testing);
        DebugText.setText("");
        DebugText.setMovementMethod(new ScrollingMovementMethod());

        //Splitting the data from the Text variable holding the crimes and putting them into an arraylist
        String[] DataCollection = policeData.Data.split("\n");
        policeData.dataArray = new ArrayList<String>(Arrays.asList(DataCollection));
        ArrayList<String> getDataArray = policeData.dataArray;

        //Getting the Text variable holding the crimes and displaying them - removing the spaces.
        int counter = 0;
        int contentCounter = 1;

        while (counter < getDataArray.size()){
            if (contentCounter != 6){
                DebugText.append(counter + " |" + getDataArray.get(counter) + "\n");
                contentCounter ++;
            } else {
                DebugText.append("\n");
                contentCounter = 1;
            }
            counter++;
        }
    }
}