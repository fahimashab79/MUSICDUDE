package com.example.musicdude;

import android.graphics.Bitmap;

import java.io.Serializable;

public class MusicModel implements Serializable {

    String audioPath;
    String audioName;
    String audioArtist;
    String audioAlbum;



    public MusicModel(String audioPath, String audioName, String audioArtist, String audioAlbum) {
        this.audioPath = audioPath;
        this.audioName = audioName;
        this.audioArtist = audioArtist;
        this.audioAlbum = audioAlbum;


    }

    public String getAudioPath() {
        return audioPath;
    }

    public String getAudioName() {
        return audioName;
    }

    public String getAudioArtist() {
        return audioArtist;
    }

    public String getAudioAlbum() {
        return audioAlbum;
    }


}
