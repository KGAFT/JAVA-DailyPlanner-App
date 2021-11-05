package com.kgaft.dailyplanner.Service;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.kgaft.dailyplanner.Activities.MainActivity;
import com.kgaft.dailyplanner.DAOEmu.SQLHelperImplement;
import com.kgaft.dailyplanner.R;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class NotificationService extends Service {
    private SQLHelperImplement database;
    private static String CHANNEL_ID = "Goal";
    private static final int NOTIFICATION_ID = 101;
    private NotificationManager nm;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("Service started!");
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        database = new SQLHelperImplement(getApplicationContext(), "GOALS", null, 1);
    }


    public int onStartCommand(Intent intent, int flags, int startId) {
//
        database.getGoalsByDayOfWeek(DateHandler.getDayName()).forEach(element -> {
            System.out.println("Database getted");
            new Thread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                public void run() {
                    long timeInSecond = 0;
                    if ((!element.getHoursInfo().equals("")) & (!element.getHoursInfo().equals("0"))) {
                        if(Integer.parseInt(DateHandler.getHours()) >= Integer.parseInt(element.getHoursInfo())){
                            int pathHoursTime = Integer.parseInt(DateHandler.getHours()) - Integer.parseInt(element.getHoursInfo());
                            timeInSecond += pathHoursTime*60*60;
                            System.out.println("Hours: "+ pathHoursTime);
                            System.out.println("part one");
                        }
                    }
                    if ((!element.getMinutesInfo().equals("")) & (!element.getMinutesInfo().equals("0"))) {
                        if(Integer.parseInt(DateHandler.getMinutes()) >= Integer.parseInt(element.getMinutesInfo())){
                            int pathTimeInMinutes = Integer.parseInt(DateHandler.getMinutes()) - Integer.parseInt(element.getMinutesInfo());
                            timeInSecond += pathTimeInMinutes*60;
                            System.out.println("Minutes "+ pathTimeInMinutes);
                            System.out.println("part two");
                        }
                        else{
                            int pathTimeInMinutes = 60 - Integer.parseInt(DateHandler.getMinutes());
                            pathTimeInMinutes += Integer.parseInt(element.getMinutesInfo());
                            timeInSecond = pathTimeInMinutes*60;
                            System.out.println("Minutes "+ pathTimeInMinutes);
                            System.out.println("part three");
                        }
                    }
                    System.out.println(timeInSecond);
                    try {
                        Thread.sleep(timeInSecond*1000);
                        System.out.println("Notification sended!");
                        sendNotify(element.getMainInfo());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }).run();
        });


        return super.onStartCommand(intent, flags, startId);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void sendNotify(String text) {
        String notification;
        NotificationManager mNotificationManager;  //Creating notifier manager

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext(), "notify_001"); //Create builder of notifications
        Intent ii = new Intent(getApplicationContext(), MainActivity.class); //Creating intent of mainActivity
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, ii, 0); //Creating pending intent for mainActivity intent

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(text);
        bigText.setBigContentTitle("Task");
        bigText.setSummaryText(text);

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
        mBuilder.setContentTitle(text);
        mBuilder.setContentText(text);
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(bigText);

        mNotificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

// === Removed some obsoletes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "GoalsChannel";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }
        Random rand = new Random();
        int id = rand.nextInt(1000);

        mNotificationManager.notify(id, mBuilder.build());
    }


}

