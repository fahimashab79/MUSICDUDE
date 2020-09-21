package com.example.musicdude;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class GetMusic implements ActivityCompat.OnRequestPermissionsResultCallback {
    private int REQUESTCODE=1;
    private Uri uri;
    Context context;

    public List<MusicModel> getAllAudioFromDevice(Context context) {
        this.context=context;
        final List<MusicModel> tempAudioList = new ArrayList<>();

        if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUESTCODE);
        }
        else {
            uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }


        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.ALBUM, MediaStore.Audio.ArtistColumns.ARTIST,};
        Cursor c = context.getContentResolver().query(uri, projection, MediaStore.Audio.Media.DATA + " like ? ", new String[]{"%Music%"}, null);

        if (c != null) {
            while (c.moveToNext()) {


                final String path = c.getString(0);
                String album = c.getString(1);
                String artist = c.getString(2);

                String name = path.substring(path.lastIndexOf("/"));
                name=name.replaceFirst("/","");
                name=name.replace("_"," ");
                name=name.replace("-"," ");
                MusicModel musicModel=new MusicModel(path,name,artist,album);
                tempAudioList.add(musicModel);
                //Log.i("Name:",name);

            }
            c.close();
        }



        return tempAudioList;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==REQUESTCODE){
            if (grantResults.length > 0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED) {
                uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

            }
            else {
                ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUESTCODE);
            }
        }
    }
}
