package com.kgaft.dailyplanner.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kgaft.dailyplanner.Activities.Fragments.AllGoalsFragment;
import com.kgaft.dailyplanner.Activities.Fragments.GoalFragment;
import com.kgaft.dailyplanner.Activities.Fragments.MainActivityFragment;
import com.kgaft.dailyplanner.DAOEmu.SQLHelperImplement;
import com.kgaft.dailyplanner.Entities.Goal;
import com.kgaft.dailyplanner.R;
import com.kgaft.dailyplanner.Service.CheckingTodayCompletedService;
import com.kgaft.dailyplanner.Service.DateHandler;
import com.kgaft.dailyplanner.Service.NotificationService;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    /*
    This class serves main activity with tab bar and all goals page
     */
    private ArrayList<GoalFragment> goalFragments = new ArrayList<>(); //This list is containing already added goalFragments
    private ArrayList<String> goalTexts = new ArrayList<>(); //This list is containing already added goal texts
    private FragmentManager manager;
    private AllGoalsFragment allGoalsFragment; //Goals sorted by day of week fragment
    private SQLHelperImplement dataBase = new SQLHelperImplement(this, "GOALS", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);  //removing the top bar
        setContentView(R.layout.activity_main);
        startService(new Intent(this, NotificationService.class));  //starting notification service
        startService(new Intent(this, CheckingTodayCompletedService.class)); //starting clearing service
        allGoalsFragment = new AllGoalsFragment();
        initTabBarListener();  //initializing tab bar listener

    }


    @Override
    protected void onResume() {
        super.onResume();
        initMainActivtyOnStart();
        initTabBarListener();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }


    public void ocActionsGoal(View view) {  //This method is listening all tasks buttons
        switch (view.getId()) {
            case R.id.finishButton:
                Button button = (Button) view;
                String[] args = button.getText().toString().split(" ");
                String goalId = ""; //getting goal id
                for (int counter = 1; counter < args.length; counter++) {
                    if (counter != args.length - 1) {
                        goalId += args[counter] + " ";
                    } else {
                        goalId += args[counter];
                    }

                }
                dataBase.deleteGoalById(goalId);
                FragmentTransaction transaction = manager.beginTransaction();
                Intent intent = new Intent(this, MainActivity.class);  //Going back to main activity
                startActivity(intent);
                break;
            case R.id.editButton:
                Button buttonEdit = (Button) view;
                String[] editArgs = buttonEdit.getText().toString().split(" ");
                String goalEditId = "";
                for (int counter = 1; counter < editArgs.length; counter++) {
                    if (counter != editArgs.length - 1) {
                        goalEditId += editArgs[counter] + " ";
                    } else {
                        goalEditId += editArgs[counter];
                    }

                }
                Intent secondIntent = new Intent(this, EditActivity.class);  //starting activity with edit page
                secondIntent.putExtra("id", goalEditId);
                startActivity(secondIntent);
        }

    }

    public void ocAddGoalActivity(View view) {
        Intent goToAddPageIntent = new Intent(this, AddGoalActivity.class); //Listener for add button
        startActivity(goToAddPageIntent);
    }


    private void initMainActivtyOnStart(){  //This method requires for main activity and initializing it
        manager = getSupportFragmentManager();
        manager.beginTransaction().add(R.id.activityMainLayout, new MainActivityFragment(), "MainActivityFragment").commitNow();  //Adding today goals fragment to activity

        dataBase.getGoalsByDayOfWeek(DateHandler.getDayName()).forEach(element -> {  //Adding goals to activity
            if(!element.isTodayCompleted()){
                GoalFragment fragment = new GoalFragment(element.getId(), element.getMainInfo());
                if (!goalTexts.contains(element.getMainInfo())) {
                    if (element.isInMainTimeTable()) {
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.add(R.id.timeTableTasks, fragment);
                        try {
                            transaction.commitNow();
                        } catch (IllegalStateException ex) {
                        }
                    } else {
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.add(R.id.sampleTasks, fragment);
                        try {
                            transaction.commitNow();
                        } catch (IllegalStateException ex) {
                        }
                    }
                    goalFragments.add(fragment);
                    goalTexts.add(element.getMainInfo());
                }
            }


        });
    }
    private void initAllGoalsActivity(){
        manager = getSupportFragmentManager(); //This method requires for initializing fragment with all goals
        manager.beginTransaction().replace(R.id.activityMainLayout, allGoalsFragment, "AllGoalsFragment").commitNow();

        dataBase.getAllGoals().forEach(element -> {
            GoalFragment fragment = new GoalFragment(element.getId(), element.getMainInfo());
            if (!goalTexts.contains(element.getMainInfo())) {
                if (element.getDayOfWeek().contains("Monday")) {  //Sorting elements by day of week
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.add(R.id.goalsMondayContainer, fragment);
                    try {
                        transaction.commitNow();
                    } catch (IllegalStateException ex) {
                    }
                } else if(element.getDayOfWeek().contains("Tuesday")){
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.add(R.id.goalsTuesdayContainer, fragment);
                    try {
                        transaction.commitNow();
                    } catch (IllegalStateException ex) {
                    }

                }
                else if(element.getDayOfWeek().contains("Wednesday")){
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.add(R.id.goalsWednesdayContainer, fragment);
                    try {
                        transaction.commitNow();
                    } catch (IllegalStateException ex) {
                    }

                }
                else if(element.getDayOfWeek().contains("Thursday")){
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.add(R.id.goalsThursdayContainer, fragment);
                    try {
                        transaction.commitNow();
                    } catch (IllegalStateException ex) {
                    }

                }
                else if(element.getDayOfWeek().contains("Friday")){
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.add(R.id.goalsFridayContainer, fragment);
                    try {
                        transaction.commitNow();
                    } catch (IllegalStateException ex) {
                    }

                }
                else if(element.getDayOfWeek().contains("Saturday")){
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.add(R.id.goalsSaturdayContainer, fragment);
                    try {
                        transaction.commitNow();
                    } catch (IllegalStateException ex) {
                    }

                }
                else if(element.getDayOfWeek().contains("Sunday")){
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.add(R.id.goalsSundayContainer, fragment);
                    try {
                        transaction.commitNow();
                    } catch (IllegalStateException ex) {
                    }

                }
            }
            goalFragments.add(fragment);
            goalTexts.add(element.getMainInfo());
        });

    }
    private void clearMainActivity(){
        ArrayList<GoalFragment> deletedFragments = new ArrayList<>();
        AtomicInteger counter = new AtomicInteger(0);
        goalFragments.forEach(element -> {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.remove(element).commitNow();
            deletedFragments.add(element);
            goalTexts.remove(counter.intValue());
            counter.getAndIncrement();
            counter.getAndDecrement();

        });
        deletedFragments.forEach(element->{
            goalFragments.remove(element);
        });
    }


    private void initTabBarListener(){
        BottomNavigationView toolBar = findViewById(R.id.navigationBar);
        toolBar.setOnNavigationItemSelectedListener((item -> {
            switch(item.getItemId()){
                case R.id.timeTableButton:
                    manager = getSupportFragmentManager();
                    manager.beginTransaction().remove(allGoalsFragment).commitNow();
                    clearMainActivity();
                    initMainActivtyOnStart();
                    break;
                case R.id.AllTaskssButton:
                    clearMainActivity();
                    initAllGoalsActivity();
            }
            return false;
        }));
    }

}
