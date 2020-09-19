package com.example.musicdude;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import java.util.zip.Inflater;

public class loadingScreen {
    Activity activity;
    AlertDialog dialog;
    loadingScreen(Activity myactivity){
        activity=myactivity;
    }
    void startLoadingDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        LayoutInflater inflater=activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loadingscreen,null));
        builder.setCancelable(true);
        dialog=builder.create();
        dialog.show();
    }
    void dismissDialog()
    {
        dialog.dismiss();
    }
}
