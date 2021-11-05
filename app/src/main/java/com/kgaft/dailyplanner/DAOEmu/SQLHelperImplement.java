package com.kgaft.dailyplanner.DAOEmu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.kgaft.dailyplanner.Entities.Goal;

import java.util.ArrayList;
/*
This class serves the sqlite compact database
 */
public class SQLHelperImplement extends SQLiteOpenHelper {
    private static final String DB_NAME = "DailyPlanner";  //Constant with database name
    private static final int DB_VERSION = 1; //Constant with database version
    private SQLiteDatabase classDb;

    public SQLHelperImplement(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Creating the database
        db.execSQL("CREATE TABLE GOALS(" + "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "mainInfo TEXT, " +
                "hoursInfo TEXT, " +
                "minutesInfo TEXT, "  +
                "dayOfWeek TEXT, "+
                "isTodayCompleted NUMBERIC, "
                + "isInMainTimeTable NUMBERIC"+");");
        this.classDb = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addNewGoal(Goal goal) {
        //This method parser goal entity and adding it to database
        ContentValues goalToAdd = new ContentValues();
        goalToAdd.put("mainInfo", goal.getMainInfo());
        goalToAdd.put("hoursInfo", goal.getHoursInfo());
        goalToAdd.put("minutesInfo", goal.getMinutesInfo());
        goalToAdd.put("dayOfWeek", goal.getDayOfWeek());
        goalToAdd.put("isInMainTimeTable", goal.isInMainTimeTable());
        goalToAdd.put("isTodayCompleted", goal.isTodayCompleted());
        classDb = getWritableDatabase();
        classDb.insert("GOALS", null, goalToAdd);
    }

    public Goal getGoalById(int id) {
        //This method return goal by id
        classDb = getReadableDatabase();
        Cursor cursor = classDb.query("GOALS", new String[]{"_id", "mainInfo",  "hoursInfo", "minutesInfo", "dayOfWeek", "isInMainTimeTable", "isTodayCompleted"},
                "_id = ?", new String[]{String.valueOf(id)}, null, null, null);
        cursor.moveToFirst();
        ArrayList<Goal> fetchedGoals = fetchCursor(cursor);
        return fetchedGoals.get(0);

    }

    public ArrayList<Goal> getGoalsByDayOfWeek(String dayOfWeek) {
        //THis method returns all goals by day of week
        ArrayList<Goal> goalsToReturn = new ArrayList<>();
        classDb = getReadableDatabase();
        Cursor cursor = classDb.query("GOALS", new String[]{"_id", "mainInfo",  "hoursInfo", "minutesInfo", "dayOfWeek", "isInMainTimeTable", "isTodayCompleted"},
                "dayOfWeek = ?", new String[]{dayOfWeek}, null, null, null);
        goalsToReturn = fetchCursor(cursor);
        return goalsToReturn;
    }

    public void deleteGoalById(String id) {
        classDb = getWritableDatabase();
        Goal goal = getGoalById(Integer.parseInt(id));
        if(goal.isInMainTimeTable()){
            goal.setTodayCompleted(true);
            updateGoalById(Integer.parseInt(id), goal);
        }
        else{
            classDb.delete("GOALS", "_id = ?", new String[]{id});
        }
    }

    public void updateGoalById(int id, Goal goal) {
        //This method edits the recieved goal and adds it to database
        classDb = getWritableDatabase();
        ContentValues goalToUpdate = new ContentValues();
        goalToUpdate.put("mainInfo", goal.getMainInfo());
        goalToUpdate.put("hoursInfo", goal.getHoursInfo());
        goalToUpdate.put("minutesInfo", goal.getMinutesInfo());
        goalToUpdate.put("dayOfWeek", goal.getDayOfWeek());
        goalToUpdate.put("isInMainTimeTable", goal.isInMainTimeTable());
        goalToUpdate.put("isTodayCompleted", goal.isTodayCompleted());
        classDb.update("GOALS", goalToUpdate, "_id = ?", new String[]{String.valueOf(id)});
    }

    public ArrayList<Goal> getAllGoals() {
        ArrayList<Goal> goalsToReturn = new ArrayList<>();
        getGoalsByDayOfWeek("Monday").forEach(element-> goalsToReturn.add(element));
        getGoalsByDayOfWeek("Tuesday").forEach(element-> goalsToReturn.add(element));
        getGoalsByDayOfWeek("Wednesday").forEach(element-> goalsToReturn.add(element));
        getGoalsByDayOfWeek("Thursday").forEach(element-> goalsToReturn.add(element));
        getGoalsByDayOfWeek("Friday").forEach(element-> goalsToReturn.add(element));
        getGoalsByDayOfWeek("Saturday").forEach(element-> goalsToReturn.add(element));
        getGoalsByDayOfWeek("Sunday").forEach(element-> goalsToReturn.add(element));
        return goalsToReturn;
    }
    private ArrayList<Goal> fetchCursor(Cursor cursor){
        ArrayList<Goal> goalsToReturn = new ArrayList<>();
        cursor.moveToFirst();
        //This method fetches the recieved cursor and returning list with foals, tha required in cursor
        do {
            try {
                Goal goalToReturn = new Goal();
                goalToReturn.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                goalToReturn.setMainInfo(cursor.getString(cursor.getColumnIndex("mainInfo")));
                goalToReturn.setHoursInfo(cursor.getString(cursor.getColumnIndex("hoursInfo")));
                goalToReturn.setMinutesInfo(cursor.getString(cursor.getColumnIndex("minutesInfo")));
                goalToReturn.setDayOfWeek(cursor.getString(cursor.getColumnIndex("dayOfWeek")));
                int isInMain = cursor.getInt(cursor.getColumnIndex("isInMainTimeTable"));
                int isCompleted = cursor.getInt(cursor.getColumnIndex("isTodayCompleted"));
                if (isInMain == 1) {
                    goalToReturn.setInMainTimeTable(true);
                } else {
                    goalToReturn.setInMainTimeTable(false);
                }
                if (isCompleted == 1) {
                    goalToReturn.setTodayCompleted(true);
                } else {
                    goalToReturn.setTodayCompleted(false);
                }
                goalsToReturn.add(goalToReturn);
            } catch (CursorIndexOutOfBoundsException ex) {

            }

        } while (cursor.moveToNext());
        return goalsToReturn;
    }


}
