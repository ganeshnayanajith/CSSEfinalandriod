package com.example.alexa.Adapter;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.alexa.Model.User;
import com.example.alexa.OrderListActivity;
import com.example.alexa.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull final RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {

           System.out.println("inside notification");
           Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
           showNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody(),OrderListActivity.class);
           Intent intent = new Intent(MyFirebaseMessagingService.this, OrderListActivity.class);
           sendBroadcast(intent);


        }
    }

    private void showNotification(String title, String body, Class activity) {
        NotificationCompat.Builder builer = new NotificationCompat.Builder(this,"Notify1");

        createNotificationChannel();
        Intent intent;
        intent = new Intent(this, activity);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 01, intent, 0);
        builer.setContentIntent(pendingIntent);
        builer.setDefaults(Notification.DEFAULT_ALL);
        builer.setContentTitle(title);
        builer.setSmallIcon(R.mipmap.new_logo);
//        builer.setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.new_logo));
        builer.setAutoCancel(true);
        builer.setContentText(body);

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);

        int randomInt = new Random().nextInt(9999-1)+1;
        notificationManager.notify(randomInt, builer.build());
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = this.getString(R.string.channel_name);
            String description = this.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("Notify1", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = (NotificationManager)this.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
