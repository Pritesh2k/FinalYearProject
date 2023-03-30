package com.example.finalyearproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    //Referencing Database Instance
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();

    //Referencing Button Components
    //Reference to Map
    Button GO_TO_MAP_BTN;
    //Reference to Map
    Button DO_TO_DEBUG;
    //Reference to Safety Button
    Button SAFTEY_BTN;
    Button ALERT_MESSAGE;

    //Referencing Text Components
    public static TextView CRIME_TYPE;
    public static TextView LOCATION;
    public static TextView LONGITUDE;
    public static TextView LATITUDE;
    public static TextView SOURCE;

    public boolean updateNearestcrime = false;

    static  String[] DataCollection;

    @SuppressLint({"WrongThread", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PoliceData.GetFirebaseDocuments();

        //Getting the TextView_Data component via ID - To update with the nearest crime
        CRIME_TYPE = (TextView) findViewById(R.id.Crime_Type_Data);
        LOCATION = (TextView) findViewById(R.id.Location_Data);
        LONGITUDE = (TextView) findViewById(R.id.Longitude_Data);
        LATITUDE = (TextView) findViewById(R.id.Latitude_Data);
        SOURCE = (TextView) findViewById(R.id.Source_Data);

        //On click listener allowing user to click the button to show map
        GO_TO_MAP_BTN = (Button) findViewById(R.id.CrimeLog_Button);
        GO_TO_MAP_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), DebugLog.class);
                startActivity(intent);
            }
        });

        //Go To Debug Page
        DO_TO_DEBUG = (Button) findViewById(R.id.News);
        DO_TO_DEBUG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), NewsAPI.class);
                startActivity(intent);
            }
        });

        //Considering the state of the Alert Message Pop Up and setting up the Vibration
        ALERT_MESSAGE = (Button) findViewById(R.id.NotificationButton);
        ALERT_MESSAGE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GO_TO_MAP_BTN.setVisibility(View.VISIBLE);
                DO_TO_DEBUG.setVisibility(View.VISIBLE);
                ALERT_MESSAGE.setVisibility(View.INVISIBLE);
            }
        });

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

        int vibrationDuration = 1000;
        if (PoliceData.updateClosestCrime){
            try {
                PoliceData.readClosestCrimeData();
                GO_TO_MAP_BTN.setVisibility(View.INVISIBLE);
                DO_TO_DEBUG.setVisibility(View.INVISIBLE);
                ALERT_MESSAGE.setVisibility(View.VISIBLE);
                Vibrate(3, vibrationDuration);
            } catch (Exception e){
                System.out.println(e);
                Toast.makeText(getApplicationContext(), "An Unexpected Error Occured, Please Re-Load Map", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void vibratePhone(Context context, int durationInMillis) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator.hasVibrator()) {
            // Vibrate for the specified duration
            vibrator.vibrate(durationInMillis);
        } else {
            // The device does not have a vibrator
            Toast.makeText(context, "This device does not have a vibrator.", Toast.LENGTH_SHORT).show();
        }
    }

    public void Vibrate(int itterations, int vibrationDuration) throws InterruptedException {
        for (int counter = 0; counter < itterations; counter ++) {
            vibratePhone(getApplicationContext(), vibrationDuration);
            Thread.sleep(500);
        }
    }

}