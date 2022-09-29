package com.rajeev.android.surilamusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.*;


public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView textView;
    ArrayList<ModelClass> songsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        textView = findViewById(R.id.txtNoSongs);

        if (checkUserPermission() == false) {
            requestPermission();
            return;
        }

        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION
        };

        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        Cursor curser = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null, null);

        while (curser.moveToNext()) {
            ModelClass songData = new ModelClass(curser.getString(1), curser.getString(0), curser.getString(2));

            if (new File(songData.getPath()).exists()) {
                songsList.add(songData);
            }
        }
        if (songsList.size() == 0) {
            textView.setVisibility(View.VISIBLE);
        } else {
            // code will go to recyclerView
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new MusicAdapter(songsList, getApplicationContext()));
        }
    }

    boolean checkUserPermission() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(MainActivity.this, "Permission is Required", Toast.LENGTH_SHORT).show();
            //ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},123);

        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (recyclerView!=null){
            recyclerView.setAdapter(new MusicAdapter(songsList,getApplicationContext()));
        }
    }
}