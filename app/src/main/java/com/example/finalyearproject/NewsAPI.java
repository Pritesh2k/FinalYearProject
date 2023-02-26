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

import com.example.finalyearproject.Model.NewsAdapter;
import com.example.finalyearproject.Model.NewsModel;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.util.ArrayList;

public class NewsAPI extends AppCompatActivity {

    static Button newsSafteyButton;

    NewsApiClient newsApiClient;
    ArrayList<NewsModel> newsModelArrayList;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_api);

        recyclerView=findViewById(R.id.recyclerView);
        newsModelArrayList=new ArrayList<>();//create custom arraylist
        newsApiClient= new NewsApiClient("8ec91d84acb44334963439c4c7b027c3");

        newsApiClient.getEverything(
                new EverythingRequest.Builder()
                        .q("crime+in+london")
                        .language("en")
                        .sortBy("publishedAt")
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        Log.i("MARDUK",response.getArticles().get(0).getTitle());

                        //add 70 article to newsmoderarraylist
                        for (int i = 0; i < 70 ; i++) {
                            newsModelArrayList.add(new NewsModel(response.getArticles().get(i).getTitle(),response.getArticles().get(i).getAuthor(),response.getArticles().get(i).getContent(),response.getArticles().get(i).getDescription(),response.getArticles().get(i).getUrl(),response.getArticles().get(i).getPublishedAt(),response.getArticles().get(i).getUrlToImage(),response.getArticles().get(i).getSource().getName()));
                        }

                        //load data to recylerview
                        NewsAdapter newsAdapter=new NewsAdapter(NewsAPI.this,newsModelArrayList);
                        recyclerView.setAdapter(newsAdapter);
                        LinearLayoutManager linearLayoutManager =
                                new LinearLayoutManager(NewsAPI.this);
                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        recyclerView.setLayoutManager(linearLayoutManager);

                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        System.out.println(throwable.getMessage());
                    }
                }
        );

        SafetyButtonHandler();
    }

    private void SafetyButtonHandler(){
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