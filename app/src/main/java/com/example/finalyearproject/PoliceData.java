package com.example.finalyearproject;

import static android.content.ContentValues.TAG;

import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PoliceData {

    private static String file_path = "E://FYP/2022-11-city-of-london-street - 2022-11-city-of-london-street.csv";

    static MainActivity mainActivity;

    public static ArrayList<String> Longitude = new ArrayList<>();
    public static ArrayList<String> Latitude = new ArrayList<>();
    public static ArrayList<String> Location = new ArrayList<>();
    public static ArrayList<String> CrimeType = new ArrayList<>();
    public static ArrayList<String> LOC = new ArrayList<>();

    public static ArrayList<String> LOC_messages = new ArrayList<String>();
    public static ArrayList<String> PackagedData = new ArrayList<>();

    public static ArrayList<String> dataArray;

    public static int Length_Of_DataSet;
    public static String Data = "";

    public static boolean GetCrimeData = true;
    public static ArrayList<String> getDataArray = new ArrayList<String>();

    static List<CircleOptions> circleList = new ArrayList<>();
    static List<MarkerOptions> markerList = new ArrayList<>();

    public static ArrayList<String> title_read = new ArrayList<String>();
    public static ArrayList<String> location_read = new ArrayList<String>();
    public static ArrayList<Double> lat_read = new ArrayList<Double>();
    public static ArrayList<Double> long_read = new ArrayList<Double>();

    public static boolean updateClosestCrime = false;

    public static void main(String[] args) {

    }

    public static void CreateLOCMEssages() {
        LOC_messages.add("Action to be taken by another organisation");
        LOC_messages.add("Awaiting court outcome");
        LOC_messages.add("Further investigation is not in the public interest");
        LOC_messages.add("Investigation complete; no suspect identified");
        LOC_messages.add("Offender given a caution");
        LOC_messages.add("Offender given a drugs possession warning");
        LOC_messages.add("Unable to prosecute suspect");
        LOC_messages.add("Under investigation");
        LOC_messages.add("Not data found");
    }

    public static void GetCrimeData() {
        getCrimeData();
        getLOCData();
        Length_Of_DataSet = Location.size();
    }

    public static void PackagedData() {

        for (int counter = 1; counter < CrimeType.size(); counter++) {

            //Adding
            PackagedData.add(CrimeType.get(counter));
            PackagedData.add(Longitude.get(counter));
            PackagedData.add(Latitude.get(counter));
            PackagedData.add(Location.get(counter));

            if (LOC.get(counter).equals("1")) {
                //System.out.println(LOC_messages.get(0));
                PackagedData.add(LOC_messages.get(0));
            } else if (LOC.get(counter).equals("2")) {
                //System.out.println(LOC_messages.get(1));
                PackagedData.add(LOC_messages.get(1));
            } else if (LOC.get(counter).equals("3")) {
                //System.out.println(LOC_messages.get(2));
                PackagedData.add(LOC_messages.get(2));
            } else if (LOC.get(counter).equals("4")) {
                //System.out.println(LOC_messages.get(3));
                PackagedData.add(LOC_messages.get(3));
            } else if (LOC.get(counter).equals("5")) {
                //System.out.println(LOC_messages.get(4));
                PackagedData.add(LOC_messages.get(4));
            } else if (LOC.get(counter).equals("6")) {
                //System.out.println(LOC_messages.get(5));
                PackagedData.add(LOC_messages.get(5));
            } else if (LOC.get(counter).equals("7")) {
                //System.out.println(LOC_messages.get(6));
                PackagedData.add(LOC_messages.get(6));
            } else if (LOC.get(counter).equals("8")) {
                //System.out.println(LOC_messages.get(7));
                PackagedData.add(LOC_messages.get(7));
            } else if (LOC.get(counter).equals("9")) {
                //System.out.println(LOC_messages.get(8));
                PackagedData.add(LOC_messages.get(8));
            }

            if (PackagedData.get(0).equals("Crime type")) { //Prevents Duplications
                break;
            } else {
                PackagedData.clear();
            }

        }
        GetCrimeData = false;
    }

    public static void getCrimeData() {
        try (BufferedReader br = new BufferedReader(new FileReader(file_path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                //Crime Type
                if (values[9].equals("") || values[9] == null) {
                    CrimeType.add("");
                } else {
                    CrimeType.add(values[9]);
                }

                //Long
                if (values[4].equals("")) {
                    Longitude.add("No data found");
                } else {
                    Longitude.add(values[4]);
                }

                //Lat
                if (values[5].equals("")) {
                    Latitude.add("No data found");
                } else {
                    Latitude.add(values[5]);
                }

                //Location
                if (values[6].equals("")) {
                    Location.add("No data found");
                } else {
                    Location.add(values[6]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getLOCData() {
        try (BufferedReader br = new BufferedReader(new FileReader(file_path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                LOC.add(values[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void GetFirebaseDocuments() {
        //Reading from the Firebase DB
        mainActivity.db.collection("November-2022")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (!document.getString("Location").equals("No Location") || !document.getString("Longitude").equals("") || !document.getString("Latitude").equals("")) {
                                    Data = Data + (document.getString("Crime Type") + "\n" + document.getString("Location")
                                            + "\n" + document.getString("Longitude") + "\n" + document.getString("Latitude")
                                            + "\n" + document.getString("Last Outcome Catagory") + "\n\n");
                                }
                            }
                        } else {
                            Toast.makeText(mainActivity.getApplicationContext(), "UnSuccessful", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public static void CheckCircles(Location location) {
        float closestDistance = 900000;

        for (int counter = 0; counter < circleList.size(); counter++) {
            Location circleLocation = new Location("");
            circleLocation.setLatitude(circleList.get(counter).getCenter().latitude);
            circleLocation.setLongitude(circleList.get(counter).getCenter().longitude);

            float distanceInMeters = location.distanceTo(circleLocation);

            if (distanceInMeters < closestDistance) {
                closestDistance = distanceInMeters;
            }

            if (distanceInMeters <= 100) {
                // User's location is within 100m of the circle's center
                System.out.println(circleLocation.getLatitude() + " | " + circleLocation.getLongitude() + " | " + distanceInMeters + " | Dangerous");
                System.out.println(markerList.get(counter).getTitle());
                System.out.println(markerList.get(counter).getSnippet());

                addNearestCrimeToCollection(markerList.get(counter).getTitle(), markerList.get(counter).getSnippet(), circleLocation.getLatitude(), circleLocation.getLongitude());

                //Alert user

            } else {
                // User's location is more than 100m away from the circle's center
                System.out.println(circleLocation.getLatitude() + " | " + circleLocation.getLongitude() + " | " + distanceInMeters);
            }

            //Closest Dist Works
            System.out.println(closestDistance);
        }
    }

    static void addNearestCrimeToCollection(String crimeType, String location, double latitude, double longitude) {
        //Clear
        mainActivity.db.collection("NearestCrime").document("Closest_Crime").delete();

        // Create a new user with a first and last name
        Map<String, Object> crime = new HashMap<>();
        crime.put("Title", crimeType);
        crime.put("Location", location);
        crime.put("Latitude", latitude);
        crime.put("Longitude", longitude);

        mainActivity.db.collection("NearestCrime")
                .document("Closest_Crime")
                .set(crime)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot added with ID: Closest_Crime");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    static void readClosestCrimeData() {
        title_read.clear();
        location_read.clear();
        lat_read.clear();
        long_read.clear();
        mainActivity.db.collection("NearestCrime")
                .document("Closest_Crime")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                title_read.add(document.getString("Title"));
                                System.out.println(title_read.get(title_read.size()-1));
                                location_read.add(document.getString("Location"));
                                System.out.println(location_read.get(title_read.size()-1));
                                lat_read.add(document.getDouble("Latitude"));
                                System.out.println("LAT: " + lat_read.get(lat_read.size()-1));
                                long_read.add(document.getDouble("Longitude"));
                                System.out.println("LONG: " + long_read.get(long_read.size()-1));
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.w(TAG, "Error getting document", task.getException());
                        }
                        storeReadData();
                    }
                });
    }


    public static void storeReadData(){
        System.out.println(PoliceData.title_read.get(0));
        System.out.println(PoliceData.title_read.size());
        MainActivity.CRIME_TYPE.setText(PoliceData.title_read.get(0));
        MainActivity.LOCATION.setText(PoliceData.location_read.get(0));
        MainActivity.LATITUDE.setText(String.valueOf(PoliceData.lat_read.get(0)));
        MainActivity.LONGITUDE.setText(String.valueOf(PoliceData.long_read.get(0)));
        MainActivity.SOURCE.setText("Police.UK");
        updateClosestCrime = false;
    }
}
