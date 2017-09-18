package com.example.admin.restapiassigment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.restapiassigment.model.user.UserClass;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "SeachTAG";
    EditText txtUserName;
    public String BaseURL;
    private String responseMSG;
    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtUserName = (EditText) findViewById(R.id.txtUserName);
    }

    public void SearchGitHub(View view) {
        if (!txtUserName.getText().toString().isEmpty()) {
            BaseURL = "https://api.github.com/users/" + txtUserName.getText().toString();

            new AsyncTask<Void, Void, UserClass>() {
                @Override
                protected UserClass doInBackground(Void... params) {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(BaseURL).build();
                    Response response;
                    UserClass user1 = null;
                    try {
                        response = client.newCall(request).execute();
                        responseMSG = response.body().string();
                        Gson gson = new Gson();
                        user1 = gson.fromJson(responseMSG, UserClass.class);
                        if(user1.getId() != null)
                        {
                            final ImageView imgView = (ImageView) findViewById(R.id.imgView);
                            Handler handler = new Handler(Looper.getMainLooper());
                            final UserClass finalUser = user1;
                            final Drawable d = LoadImageFromWebOperations(user1.getAvatarUrl());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    imgView.setImageDrawable(d);

                                }
                            });
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return user1;
                }

                @Override
                protected void onPostExecute(UserClass s) {
                    ShowUser(s);
                }
            }.execute();
        } else {
            Toast.makeText(this, "Please Enter Username", Toast.LENGTH_SHORT).show();
        }

    }

    public void ShowUser(final UserClass user1) {

        if (user1.getId() != null) {

            TextView txtName = (TextView) findViewById(R.id.txtName);
            TextView txtLocation = (TextView) findViewById(R.id.txtLocation);
            TextView txtCompany = (TextView) findViewById(R.id.txtCompany);
            TextView txtBio = (TextView) findViewById(R.id.txtBio);
            Button ReposBTN = (Button) findViewById(R.id.ReposBTN);
//
            txtName.setText("Name: " + user1.getName());
            txtLocation.setText("Location: " + user1.getLocation());
            txtCompany.setText("Company: " + user1.getCompany());
            if (user1.getBio() != null)
                txtBio.setText("Bio: " + user1.getBio().toString());

            ReposBTN.setVisibility(View.VISIBLE);
            ReposBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, ListingRepositories.class);
                    intent.putExtra("ReposKEY", user1.getReposUrl());
                    startActivity(intent);
                }
            });
        } else {
            Toast.makeText(this, "Please Enter Valid Username", Toast.LENGTH_SHORT).show();
        }
    }

    private Drawable LoadImageFromWebOperations(String avatarUrl) {
        try {
            InputStream is = (InputStream) new URL(avatarUrl).getContent();
            Drawable d = Drawable.createFromStream(is, "srcName");
            return d;
        } catch (Exception e) {
            Log.d(TAG, "LoadImageFromWebOperations: " + e.toString());
            return null;
        }
    }
}
