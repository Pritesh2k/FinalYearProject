package com.example.finalyearproject;

import static android.content.ContentValues.TAG;

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

import org.checkerframework.checker.units.qual.C;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //Referencing Database Instance
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();

    //Referencing Button Components
    Button GO_TO_MAP_BTN;
    Button DO_TO_DEBUG;
    Button POP_UP_BTN;
    Button SAFTEY_BTN;

    //Referencing Text Components
    public static TextView CRIME_TYPE;
    public static TextView LOCATION;
    public static TextView LONGITUDE;
    public static TextView LATITUDE;
    public static TextView SOURCE;
    public static TextView OUTCOME;

    TextView ALERT_MESSAGE;

    //Variable
    boolean isAlertMessage_ACTIVE = true;
    boolean ALERT_MSG_ACTIVE = true;

    //Classes
    PoliceData policeData;

    @SuppressLint({"WrongThread", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Considering the state of the Alert Message Pop Up and setting up the Vibration
        ALERT_MESSAGE = (TextView) findViewById(R.id.ALERT_MESSAGE);
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if (isAlertMessage_ACTIVE == true) { //Alert Message is active
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
        OUTCOME = (TextView) findViewById(R.id.Outcome_Data);
        //DebugText = (TextView) findViewById(R.id.Debug_Testing);

        //On click listener allowing user to click the button to show map
        GO_TO_MAP_BTN = (Button) findViewById(R.id.button3);
        GO_TO_MAP_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Accessing Internet and Location
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), GoogleMaps.class);
                startActivity(intent);
            }
        });

        //Go To Debug Page
        DO_TO_DEBUG = (Button) findViewById(R.id.Debugging);
        DO_TO_DEBUG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), DebugLog.class);
                startActivity(intent);
            }
        });

        //Creating a manual Pop Up Message (MUST incorporate later FULLY along with predication ect...
        POP_UP_BTN = (Button) findViewById(R.id.pop_up);
        if (ALERT_MSG_ACTIVE == true) {
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
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Call UnSuccessful", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Reading from the Firebase DB
        policeData.GetFirebaseDocuments();
    }
}