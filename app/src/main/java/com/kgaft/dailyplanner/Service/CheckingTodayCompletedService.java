package com.kgaft.dailyplanner.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.kgaft.dailyplanner.DAOEmu.SQLHelperImplement;
import com.kgaft.dailyplanner.Entities.Goal;

import java.util.ArrayList;

public class CheckingTodayCompletedService extends Service {
    private SQLHelperImplement database;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //This class updates goals in main time table
    @Override
    public void onCreate() {
        super.onCreate();
        database = new SQLHelperImplement(getApplicationContext(), "GOALS", null, 1);
        long timeToStartInSeconds = 0;
        int currentHour = Integer.parseInt(DateHandler.getHours());
        int currentMinute = Integer.parseInt(DateHandler.getMinutes());
        int path = 23 - currentHour;
        path *= 60 * 60;
        timeToStartInSeconds += path;
        path = 0;
        if(currentMinute > 0){
            path = 60 - currentMinute;
            path*=60;
        }
        timeToStartInSeconds += path;
        try{
            Thread.sleep(timeToStartInSeconds*1000);
            updateGoals();
        }catch (Exception e){

        }

    }

    private void updateGoals() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Goal> goalsToCheck = database.getGoalsByDayOfWeek(DateHandler.getDayName());
                goalsToCheck.forEach(element -> { //Fetching goals for main time table
                    if (element.isTodayCompleted()) {
                        element.setTodayCompleted(false);
                    }
                });
                goalsToCheck.forEach(element -> {
                    database.updateGoalById(element.getId(), element);
                });
            }
        }).run();

    }
}
