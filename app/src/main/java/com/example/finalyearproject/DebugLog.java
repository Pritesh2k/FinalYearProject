package com.example.finalyearproject;

import static com.example.finalyearproject.MainActivity.DataCollection;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

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
        DataCollection = policeData.Data.split("\n");
        policeData.dataArray = new ArrayList<String>(Arrays.asList(DataCollection));
        PoliceData.getDataArray = policeData.dataArray;

        //Getting the Text variable holding the crimes and displaying them - removing the spaces.
        int counter = 0;
        int contentCounter = 1;

        while (counter < PoliceData.getDataArray.size()){
            if (contentCounter != 6){
                DebugText.append(counter + " |" + PoliceData.getDataArray.get(counter) + "\n");
                contentCounter ++;
            } else {
                DebugText.append("\n");
                contentCounter = 1;
            }
            counter++;
        }
    }
}