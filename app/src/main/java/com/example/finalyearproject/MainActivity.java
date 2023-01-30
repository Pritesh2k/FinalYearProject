package com.example.finalyearproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //Referencing Database Instance
    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    WriteBatch Batch = db.batch();

    //Referencing Button Components
    Button GO_TO_MAP_BTN;
    Button POP_UP_BTN;
    Button SAFTEY_BTN;

    //Referencing Text Components
    TextView CRIME_TYPE;
    TextView LOCATION;
    TextView LONGITUDE;
    TextView LATITUDE;
    TextView SOURCE;

    TextView ALERT_MESSAGE;

    //Variable
    boolean isAlertMessage_ACTIVE = true;
    boolean ALERT_MSG_ACTIVE = true;

    //Classes
    PoliceData policeData;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialising the LOC Message for Database & Getting Crime Data

        //Considering the state of the Alert Message Pop Up and setting up the Vibration
        ALERT_MESSAGE = (TextView) findViewById(R.id.ALERT_MESSAGE);
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if(isAlertMessage_ACTIVE == true) { //Alert Message is active
            // Set the new state
            ALERT_MESSAGE.setVisibility(View.VISIBLE);

            //Vibration code
            if (vibrator.hasVibrator()) {
                vibrator.vibrate(1500); // 500 milliseconds
                try {
                    Thread.sleep(1000); // 1000 milliseconds = 1 second
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                vibrator.vibrate(1000); // 500 milliseconds
            }
        }

        //Getting the TextView_Data component via ID - To update with the nearest crime
        CRIME_TYPE = (TextView) findViewById(R.id.Crime_Type_Data);
        LOCATION = (TextView) findViewById(R.id.Location_Data);
        LONGITUDE = (TextView) findViewById(R.id.Longitude_Data);
        LATITUDE = (TextView) findViewById(R.id.Latitude_Data);
        SOURCE = (TextView) findViewById(R.id.Source_Data);

        //On click listener allowing user to click the button to show map
        GO_TO_MAP_BTN = (Button) findViewById(R.id.button);
        GO_TO_MAP_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Accessing Internet and Location
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), GoogleMaps.class);
                startActivity(intent);
            }
        });

        //Creating a manual Pop Up Message (MUST incorporate later FULLY along with predication ect...
        POP_UP_BTN = (Button) findViewById(R.id.pop_up);
        if (ALERT_MSG_ACTIVE == true){
            POP_UP_BTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isAlertMessage_ACTIVE = false;
                    ALERT_MESSAGE.setVisibility(View.INVISIBLE);
                    POP_UP_BTN.setVisibility(View.GONE);
                }
            });
        }

        //Call someone upon OnClick
        SAFTEY_BTN = (Button) findViewById(R.id.saftey_button);
        SAFTEY_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Creating new intent and establishing the data
                try {
                    //Making the call
                    Intent safteyButton_Intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:999"));
                    startActivity(safteyButton_Intent);
                } catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Call UnSuccessful", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Reading from the Firebase DB
        db.collection("November-2022")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CRIME_TYPE.setText(document.getString("Crime Type"));
                                LOCATION.setText(document.getString("Location"));
                                LONGITUDE.setText(document.getString("Longitude"));
                                LATITUDE.setText(document.getString("Latitude"));
                                SOURCE.setText("Police.Data.UK");
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "UnSuccessful", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}