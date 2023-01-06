package com.example.finalyearproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //FirebaseFirestore fireStore;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Map<String, Object> RecentCrime = new HashMap<>();
        RecentCrime.put("CrimeID", "dc6a77d7098065e15cf1f3ca960a302058ca11042775b0728f40da2d8dd89a8d");
        RecentCrime.put("Longitude", "-0.09154");
        RecentCrime.put("Latitude", "51.511896");
        RecentCrime.put("Location", "On or near Cannon Street");
        RecentCrime.put("Crime type", "Drugs");
        RecentCrime.put("Last outcome category", "Awaiting court outcome");


        db.collection("Crime")
                .add(RecentCrime)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_LONG).show();
                    }
                });
    }
}