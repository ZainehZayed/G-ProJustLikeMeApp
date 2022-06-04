package com.example.g_pro_justlikemeapp.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.drawable.IconCompat;

import com.example.g_pro_justlikemeapp.MessageActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FireBaseMesseging extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        SharedPreferences sp=getSharedPreferences("SP_USER",MODE_PRIVATE);
        String sent=message.getData().get("sent");
        String user=message.getData().get("user");
        FirebaseUser fuser= FirebaseAuth.getInstance().getCurrentUser();
        if(fuser!=null&&sent.equals(fuser.getUid()))
        {
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
            {
                sendOAndoboveNotification(message);
            }
            else {sendNormalNotification(message);}
        }
    }

    private void sendNormalNotification(RemoteMessage message) {
        String user=message.getData().get("user");
        String icon=message.getData().get("icon");
        String title=message.getData().get("title");
        String body=message.getData().get("body");
        RemoteMessage.Notification notification=message.getNotification();
        int i=Integer.parseInt(user.replaceAll("[\\D]",""));
        Intent intent=new Intent(this, MessageActivity.class);

        Bundle bundle=new Bundle();
        bundle.putString("myid",user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pIntent=PendingIntent.getActivity(this,i,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri defFoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this)
                .setSmallIcon(Integer.parseInt(icon))
                .setContentText(body)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setSound(defFoundUri)
                .setContentIntent(pIntent);
        NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        int j=0;
        if(i>0)
        {
            j=i;
        }
        notificationManager.notify(j,builder.build());




    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendOAndoboveNotification(RemoteMessage message) {
        String user=message.getData().get("user");
        String icon=message.getData().get("icon");
        String title=message.getData().get("title");
        String body=message.getData().get("body");
        RemoteMessage.Notification notification=message.getNotification();
        int i=Integer.parseInt(user.replaceAll("[\\D]",""));
        Intent intent=new Intent(this, MessageActivity.class);

        Bundle bundle=new Bundle();
        bundle.putString("friendid",user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pIntent=PendingIntent.getActivity(this,i,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri defSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
       OreoAndAboveNotification notification1=new OreoAndAboveNotification(this);
        Notification.Builder builder=notification1.getONotifications(title,body,pIntent,defSoundUri,icon);

        int j=0;
        if(i>0)
        {
            j=1;
        }
        notification1.getManger().notify(j,builder.build());


    }
}
