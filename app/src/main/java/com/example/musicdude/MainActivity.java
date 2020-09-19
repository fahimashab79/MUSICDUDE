package com.example.musicdude;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewHelper.onSongListener {


    private RecyclerView recyclerView;


    public  TextView songname;
    public TextView artist;
    private GetMusic getMusic;
    private ModelClass modelClass;



    private static MainActivity instance;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        Log.i("On create called","called");

        modelClass=ModelClass.getInstance();
        getMusic =new GetMusic();



        modelClass.setSongsmodel(getMusic.getAllAudioFromDevice(MainActivity.this));
        songname=(TextView)findViewById(R.id.songName);
        artist=(TextView)findViewById(R.id.songArtist);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        RecyclerViewHelper recyclerViewHelper=new RecyclerViewHelper(modelClass.getSongsmodel(),getApplicationContext(),this);
        recyclerView.setAdapter(recyclerViewHelper);
    }
    public static MainActivity getInstance() {
        return instance;
    }


    @Override
    protected void onStart() {



        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.i("On Resume","called");


        super.onResume();



            Log.i("On complete called","called");


        }


    @Override
    protected void onPause() {
        Log.i("On pause called","called");

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.i("On destroy called","called");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("On stop called","called");



    }

    public void gotoMusicScreen(View view)
    {
        Intent intent=new Intent(MainActivity.this,MusicScreen.class);
        intent.putExtra("value",2);
        startActivity(intent);
    }

    public void playSongContent(int position)
    {

        modelClass.setGlobalmodel(modelClass.getSongsmodel().get(position));
        songname.setText(modelClass.getGlobalmodel().getAudioName());
        artist.setText(modelClass.getGlobalmodel().getAudioArtist());
        modelClass.setRunning(false);
        modelClass.setLength(0);
        try {

            modelClass.setGlobalMediaplayer(new MediaPlayer());
            modelClass.getGlobalMediaplayer().setDataSource(modelClass.getGlobalmodel().getAudioPath());
            //modelClass.getGlobalMediaplayer().setOnPreparedListener((MediaPlayer.OnPreparedListener) this);
            modelClass.getGlobalMediaplayer().prepare();

        }catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }

        modelClass.setPlay(true);

        if(modelClass.isRunning()==false){
            modelClass.setRunning(true);
            Intent intent=new Intent(MainActivity.this,musicService.class);
            startService(intent);

        }


        Intent intent1=new Intent(MainActivity.this,MusicScreen.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent1);



    }
    public Context getContext(){
        return MainActivity.this;
    }
    @Override
    public void onSongClickListner(int position) {

        if(modelClass.getGlobalMediaplayer().isPlaying())
        {
            modelClass.getGlobalMediaplayer().release();
            modelClass.setGlobalMediaplayer(null);
            modelClass.getHandler().removeCallbacks(modelClass.getRunnable());
        }

        modelClass.setCurrentsongno(position);
        modelClass.setIsautoplay(false);
        playSongContent(position);
    }

    public void playnextSong(int position){


       if(modelClass.getGlobalMediaplayer().isPlaying())
        {
            modelClass.getGlobalMediaplayer().release();
            modelClass.setGlobalMediaplayer(null);
            modelClass.getHandler().removeCallbacks(modelClass.getRunnable());

        }


        modelClass.setIsautoplay(false);
        playSongContent(position);
    }



}