package com.example.admin.restapiassigment;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.admin.restapiassigment.model.repos.ReposClass;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ListingRepositories extends AppCompatActivity {

    private static final String TAG = "ListingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_repositories);
        Intent intent = getIntent();
        final String BaseURL = intent.getStringExtra("ReposKEY");

        new AsyncTask<Void,Void,List<ReposClass>>()
        {

            @Override
            protected List<ReposClass> doInBackground(Void... params) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(BaseURL).build();
                Response response = null;
                String responseMSG = null;
                List<ReposClass> reposClassList = new ArrayList<>();
                try {
                    response = client.newCall(request).execute();
                    responseMSG = response.body().string();
                    Gson gson = new Gson();
                    ReposClass[] reposClassArray = gson.fromJson(responseMSG,ReposClass[].class);
                    for(int i =0;i<reposClassArray.length;i++)
                    {
                        ReposClass r = reposClassArray[i];
                        reposClassList.add(r);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return reposClassList;
            }

            @Override
            protected void onPostExecute(List<ReposClass> reposClasses) {
                ShowRepos(reposClasses);
                super.onPostExecute(reposClasses);
            }
        }.execute();
    }
    public  void ShowRepos(List<ReposClass> reposClasses)
    {
        RecyclerView RVList = (RecyclerView) findViewById(R.id.RVList);
        RVAdapter rvAdapter = new RVAdapter(reposClasses);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        RVList.setAdapter(rvAdapter);
        RVList.setLayoutManager(layoutManager);
        RVList.setItemAnimator(itemAnimator);
    }
}
