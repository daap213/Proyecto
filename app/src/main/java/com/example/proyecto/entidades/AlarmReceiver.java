package com.example.proyecto.entidades;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.core.content.ContextCompat;
//Crea una alarma y cuando salte el evento llama a la notificación
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service1 = new Intent(context, NotificationService.class);
        service1.setData((Uri.parse("custom://" + System.currentTimeMillis())));
        ContextCompat.startForegroundService(context, service1 );
        Log.d("WALKIRIA", " ALARM RECEIVED!!!");

    }
}