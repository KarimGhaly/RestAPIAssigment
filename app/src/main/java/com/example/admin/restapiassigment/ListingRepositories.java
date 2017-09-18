package com.example.admin.restapiassigment;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
    private List<ReposClass> reposClassList;

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
                reposClassList = new ArrayList<>();
                try {
                    response = client.newCall(request).execute();
                    responseMSG = response.body().string();
                    Gson gson = new Gson();
                    ReposClass[] reposClassArray = gson.fromJson(responseMSG,ReposClass[].class);
                    reposClassList = Arrays.asList(reposClassArray);
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
        final RVAdapter rvAdapter = new RVAdapter(reposClasses);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        RVList.setAdapter(rvAdapter);
        RVList.setLayoutManager(layoutManager);
        RVList.setItemAnimator(itemAnimator);
        SearchView searchView = (SearchView) findViewById(R.id.search_bar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<ReposClass> newreposClassList = new ArrayList<ReposClass>();
                for(ReposClass RC: reposClassList)
                {
                    if(RC.getName().toLowerCase().contains(newText.toLowerCase()))
                    {
                        newreposClassList.add(RC);
                    }
                }
                rvAdapter.FilterList(newreposClassList);
                return true;
            }
        });
    }

}
