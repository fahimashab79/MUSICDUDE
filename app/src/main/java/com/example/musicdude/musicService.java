package com.example.musicdude;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.Nullable;


public class musicService extends Service {


   private MusicModel musicModel;
   final static String MY_ACTION = "MY_ACTION";
   private static Context context;
   Boolean pause=false;
    ModelClass modelClass=ModelClass.getInstance();





    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {



       // modelClass.getGlobalMediaplayer().seekTo(modelClass.getGlobalMediaplayer().getCurrentPosition());



        modelClass.getGlobalMediaplayer().start();
        modelClass.setHandler(new Handler(Looper.getMainLooper()));
        modelClass.setRunnable(new Runnable() {
            @Override
            public void run() {
                int mCurrentPosition =modelClass.getGlobalMediaplayer().getCurrentPosition()/1000;

                Intent intent = new Intent();
                intent.setAction(MY_ACTION);
                intent.putExtra("DATAPASSED",mCurrentPosition);
                sendBroadcast(intent);
                modelClass.getHandler().postDelayed(this, 1000);
            }
        });
        modelClass.getHandler().post(modelClass.getRunnable());
        return super.onStartCommand(intent, flags, startId);
    }



    public void onPause() {

      modelClass.setLength(modelClass.getGlobalMediaplayer().getCurrentPosition()/1000);
        modelClass.getGlobalMediaplayer().pause();
       // Toast.makeText(context, String.valueOf(), Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onDestroy() {
        modelClass.getGlobalMediaplayer().release();
        super.onDestroy();
    }

}
