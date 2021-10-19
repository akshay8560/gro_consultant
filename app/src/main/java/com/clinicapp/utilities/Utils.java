package com.clinicapp.utilities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.clinicapp.R;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Utils {
    public static String token="Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxIiwianRpIjoiMDk0YTIzMzQwYmM3NzA4MWYzNzI4ZTZlODY3YzJkMzI3YWM1YTdiNGY2MmVmNjI0NjIxMzkyN2Y1Mjg0MjUxMjlkYTIzM2Y4MTVjMTAxNGUiLCJpYXQiOjE2MjYwODYzNjgsIm5iZiI6MTYyNjA4NjM2OCwiZXhwIjoxNjU3NjIyMzY4LCJzdWIiOiI0MSIsInNjb3BlcyI6W119.EuCHHnOKGiCegLh6NI7SbhW-c_eYLexoMd4igw6xGI4H-t1yWtk_1lBdDBaS3qgn4zoo-_A6f52grhzwwRh7fAxt5Ge9oXcogDhGxcHcLWlLiDjJdI1JZLh9y7FGpC-KIF1jHCZ8z0GABsXtVTPjIdgXY3L-DcoeTxBH8q1GGI9OA0W1DiDCFQ1BHpobGydB-c7kWMRIU-w9_YQTwIEh9yn9sW4nsVr0FkZZG5tPiUe2Ur50aAPpJ3fGhMBgCF61PUU3OekATO_BxGU3ImjhPvDU9zlnkMzHuuZeiQQkp3DovDbsC2iXxlzpzBgGEqJIcFkNwqlds9aDWFtAclE7WSHBU-t2RUHYWy_Qe1VNWtO475xEd__iNxbXkLSYC-7bkGwzXegXBEhRlWEiWsSJF5TT3Y4zb025YssSxyfW_aCDzbJktWC5LV0iQoTnRoNHp13ynCUT6waPdOKQJhmfCwriW2WvkoiCfHUP1VCQUOXukbFaKWjnOqFnTee82Hl-528oAGyDgXBUoNpP7z52fpiJCCixxYYe8-wCbOgVe9SzsvQ9Imo8IZ8vuSlDd2efGDuBzIdu4zQjeTc2hfDBJoSAS4NALGXp4wUJL7XJH7Yvvd8rUOjeqwz3_U53ZSsIXWNR-_nr1TlwzsuZAt9-a0f741qgfi475jyR6ODLF74";
    public static final String GET_SHARED="Get_Shared";

    @SuppressLint("ResourceAsColor")
    public static void notify(Context context, String Message){
        new AlertDialog
                .Builder(context,R.style.AlertDialogCustom)
                .setMessage(Message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
    }

    public static boolean checkAndRequestWifiPermissions(Activity activity){
        if (ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            //return false;
        }
        return true;
    }
    public static boolean checkCameraPermission(Context context){
        if(ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
            return true;
        }
        return false;
    }
    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
