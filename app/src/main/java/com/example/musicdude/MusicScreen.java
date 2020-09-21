package com.example.musicdude;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.awt.font.TextAttribute;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

public class MusicScreen extends AppCompatActivity {

    private MusicModel musicModel;
   private TextView progressText;
   private TextView musicName;
   private TextView musicArtist;
   private ImageView playpause;
   private ImageView albumImage;
   private SeekBar seekBar;
   private TextView musicduration;
   private MediaPlayer mediaPlayer;
   private ModelClass modelClass;



    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_screen);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(musicService.MY_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);
        modelClass=ModelClass.getInstance();


        musicModel=modelClass.getGlobalmodel();
        mediaPlayer= modelClass.getGlobalMediaplayer();
        musicName=findViewById(R.id.songname);
        seekBar=(SeekBar)findViewById(R.id.seekbar);
        musicduration=findViewById(R.id.songduration);
        musicArtist=findViewById(R.id.artistname);
        playpause=findViewById(R.id.playpause);
        albumImage=findViewById(R.id.logo);


        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if(modelClass.getGlobalMediaplayer().isPlaying())
                {
                    modelClass.getGlobalMediaplayer().release();
                    modelClass.setGlobalMediaplayer(null);
                    modelClass.getHandler().removeCallbacks(modelClass.getRunnable());
                }

                modelClass.setIsautoplay(true);
                modelClass.setCurrentsongno(modelClass.getCurrentsongno()+1);

                MainActivity.getInstance().playSongContent(modelClass.getCurrentsongno());
            }
        });
        try {

            setUiElemet();

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(MusicScreen.this, String.valueOf(modelClass.getGlobalMediaplayer().getCurrentPosition()), Toast.LENGTH_SHORT).show();
                modelClass.getGlobalMediaplayer().seekTo((seekBar.getProgress()*1000));

            }
        });



    }



    public void setUiElemet(){
        seekBar.setMax(mediaPlayer.getDuration()/1000);
        musicName.setText(musicModel.getAudioName());
        musicArtist.setText(musicModel.getAudioArtist());
        int totaltime=mediaPlayer.getDuration()/1000;
        String firstpart=String.valueOf( totaltime/60);
        String secondPart=String.valueOf(totaltime%60);
        //Log.i("totaltime",String.valueOf(totaltime));
        if(firstpart.length()<=1){
            firstpart="0"+firstpart;
        }
        if(secondPart.length()<=1)
        {
            secondPart="0"+secondPart;
        }
        musicduration.setText(firstpart+":"+secondPart);
        if(modelClass.isPlay()==false)
        {

            playpause.setImageResource(R.drawable.plaly);
        }
        else
        {
            playpause.setImageResource(R.drawable.pause);
        }

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(musicModel.getAudioPath());
        byte[] artBytes =  mmr.getEmbeddedPicture();
        if(artBytes != null)
        {
            InputStream is = new ByteArrayInputStream(mmr.getEmbeddedPicture());
            Bitmap bm = BitmapFactory.decodeStream(is);
            albumImage.setImageBitmap(bm);
        }
        else
        {
            albumImage.setImageResource(R.drawable.fairy);
        }

    }



    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int mCurrentPosition = intent.getIntExtra("DATAPASSED", 0);
            String f=String.valueOf(mCurrentPosition/60);
            String l=String.valueOf(mCurrentPosition%60);
            if(f.length()<=1)
            {
                f="0"+f;
            }
            if (l.length()<=1){
                l="0"+l;
            }
            progressText=findViewById(R.id.progressText);
           progressText.setText(f+":"+l);
            seekBar.setProgress(mCurrentPosition);
        }
    };



    public void pauseClick(View view){



        if(modelClass.isPlay()==false){

          Intent intent=new Intent(MusicScreen.this,musicService.class);
          startService(intent);
          modelClass.setPlay(true);
          playpause.setImageResource(R.drawable.pause);
        }
        else {
            musicService service=new musicService();
            service.onPause();
            modelClass.setPlay(false);
            playpause.setImageResource(R.drawable.plaly);
        }



  }
  public void seekForward(View view)
  {

      if(modelClass.getCurrentsongno()==(modelClass.getSongsmodel().size()-1))
      {
          modelClass.setCurrentsongno(0);
      }

      else
      {
          modelClass.setCurrentsongno(modelClass.getCurrentsongno()+1);

      }
      if(modelClass.getGlobalMediaplayer().isPlaying())
      {
          modelClass.getGlobalMediaplayer().release();
          modelClass.setGlobalMediaplayer(null);
          modelClass.getHandler().removeCallbacks(modelClass.getRunnable());
      }

      modelClass.setIsautoplay(true);
      MainActivity.getInstance().playSongContent(modelClass.getCurrentsongno());


  }


  public void seekBackward(View view)
    {

        if(modelClass.getCurrentsongno()==0)
        {
            modelClass.setCurrentsongno(modelClass.getSongsmodel().size()-1);
        }

        else
        {
            modelClass.setCurrentsongno(modelClass.getCurrentsongno()-1);

        }
        if(modelClass.getGlobalMediaplayer().isPlaying())
        {
            modelClass.getGlobalMediaplayer().release();
            modelClass.setGlobalMediaplayer(null);
            modelClass.getHandler().removeCallbacks(modelClass.getRunnable());
        }

        modelClass.setIsautoplay(true);
        MainActivity.getInstance().playSongContent(modelClass.getCurrentsongno());

    }



}