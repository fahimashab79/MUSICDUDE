package com.example.musicdude;

import android.media.MediaPlayer;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

public class ModelClass {

    private boolean running;
    private int mCurrentPosition;
    private Runnable runnable;
    private boolean isautoplay=false;
    private Handler handler;
    private int currentsongno;

    private boolean play=false;
    private  MusicModel Globalmodel;
    private MediaPlayer globalMediaplayer=new MediaPlayer();
    private List<MusicModel> songsmodel=new ArrayList<MusicModel>();
    private Handler mainHandler=new Handler();
    private int length=0;

    public boolean isPlay() {
        return play;
    }

    public void setPlay(boolean play) {
        this.play = play;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void setmCurrentPosition(int mCurrentPosition) {
        this.mCurrentPosition = mCurrentPosition;
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    public void setIsautoplay(boolean isautoplay) {
        this.isautoplay = isautoplay;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void setCurrentsongno(int currentsongno) {
        this.currentsongno = currentsongno;
    }



    public void setGlobalmodel(MusicModel globalmodel) {
        Globalmodel = globalmodel;
    }

    public void setGlobalMediaplayer(MediaPlayer globalMediaplayer) {
        this.globalMediaplayer = globalMediaplayer;
    }

    public void setSongsmodel(List<MusicModel> songsmodel) {
        this.songsmodel = songsmodel;
    }

    public void setMainHandler(Handler mainHandler) {
        this.mainHandler = mainHandler;
    }

    public void setLength(int length) {
        this.length = length;
    }

    private ModelClass(){

   }

    private static ModelClass instance;
    public static ModelClass getInstance() {
       if(instance==null)
       {
           instance=new ModelClass();
       }
       return instance;
    }

    public boolean isRunning() {
        return running;
    }

    public int getmCurrentPosition() {
        return mCurrentPosition;
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public boolean isIsautoplay() {
        return isautoplay;
    }

    public Handler getHandler() {
        return handler;
    }

    public int getCurrentsongno() {
        return currentsongno;
    }


    public MusicModel getGlobalmodel() {
        return Globalmodel;
    }

    public MediaPlayer getGlobalMediaplayer() {
        return globalMediaplayer;
    }

    public List<MusicModel> getSongsmodel() {
        return songsmodel;
    }

    public Handler getMainHandler() {
        return mainHandler;
    }

    public int getLength() {
        return length;
    }






}
