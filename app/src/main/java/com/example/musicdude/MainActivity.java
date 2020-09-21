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
import android.content.SharedPreferences;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
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
    private SharedPreferences sharedPreferences;
   private RecyclerViewHelper recyclerViewHelper;
    private static MainActivity instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        modelClass=ModelClass.getInstance();
        getMusic =new GetMusic();
        sharedPreferences=this.getSharedPreferences("com.example.musicdude",MODE_PRIVATE);
        modelClass.setSongsmodel(getMusic.getAllAudioFromDevice(MainActivity.this));
        songname=(TextView)findViewById(R.id.songName);
        artist=(TextView)findViewById(R.id.songArtist);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerViewHelper=new RecyclerViewHelper(modelClass.getSongsmodel(),getApplicationContext(),this);
        recyclerView.setAdapter(recyclerViewHelper);
        int value=sharedPreferences.getInt("index",-1);
        if(value!=-1){

            modelClass.setCurrentsongno(value);
            modelClass.setGlobalmodel(modelClass.getSongsmodel().get(value));
            songname.setText(modelClass.getGlobalmodel().getAudioName());
            artist.setText(modelClass.getGlobalmodel().getAudioArtist());
            modelClass.setRunning(false);
            modelClass.setLength(0);
            try {

                modelClass.setGlobalMediaplayer(new MediaPlayer());
                modelClass.getGlobalMediaplayer().setDataSource(modelClass.getGlobalmodel().getAudioPath());
                modelClass.getGlobalMediaplayer().prepare();
                int duration=sharedPreferences.getInt("duration",0);
                Log.i("duration",String.valueOf(duration));
                if(duration!=0)
                    modelClass.getGlobalMediaplayer().seekTo(duration);

            }catch (Exception e)
            {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }

        }


    }
    public static MainActivity getInstance() {
        return instance;
    }
    @Override
    protected void onDestroy() {
        Log.i("On destroy called",String.valueOf(modelClass.getGlobalMediaplayer().getCurrentPosition()/1000));
        super.onDestroy();

        sharedPreferences.edit().putInt("duration",modelClass.getGlobalMediaplayer().getCurrentPosition()/1000).apply();
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
        sharedPreferences.edit().putInt("index",position).apply();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.search_menu,menu);
        MenuItem searchItem=menu.findItem(R.id.action_search);
        SearchView searchView=(SearchView)searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                recyclerViewHelper.getFilter().filter(s);
                return false;
            }
        });

        return true;
    }
}