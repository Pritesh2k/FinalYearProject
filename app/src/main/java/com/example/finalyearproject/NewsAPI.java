package com.example.finalyearproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.util.ArrayList;

public class NewsAPI extends AppCompatActivity {

    Button newsSafteyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_api);

        newsSafteyButton = findViewById(R.id.news_saftey_button);
        newsSafteyButton.setOnClickListener(new View.OnClickListener() {
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
    }
}