package com.kgaft.dailyplanner.Activities;



import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kgaft.dailyplanner.DAOEmu.SQLHelperImplement;
import com.kgaft.dailyplanner.Entities.Goal;
import com.kgaft.dailyplanner.R;

public class AddGoalActivity extends AppCompatActivity {
    private EditText mainInfo;
    private EditText hoursInfo;
    private EditText minutesInfo;
    private Spinner spinnerInput;
    private CheckBox isTimeTableGoal;
    private FloatingActionButton addButton;

    /*
    This class serves the activity add new goal
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_goal);
        mainInfo =findViewById(R.id.AddMainInfo); //Getting all input fields
        hoursInfo = findViewById(R.id.AddHoursInfo);
        minutesInfo = findViewById(R.id.AddMinutesInfo);
        spinnerInput = findViewById(R.id.spinnerInput);
        addButton = findViewById(R.id.AddButton);
        isTimeTableGoal = findViewById(R.id.IsInTimeTable);
        Goal goalToAdd = new Goal();
        spinnerInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                goalToAdd.setDayOfWeek(adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Recieving the information from input fields
                String mainSInfo = mainInfo.getText().toString();
                String hoursSInfo = hoursInfo.getText().toString();
                String minutesSInfo = minutesInfo.getText().toString();
                boolean isInTimeTable = isTimeTableGoal.isChecked();
                goalToAdd.setMainInfo(mainSInfo);
                goalToAdd.setHoursInfo(hoursSInfo);
                goalToAdd.setMinutesInfo(minutesSInfo);
                goalToAdd.setInMainTimeTable(isInTimeTable);
                SQLHelperImplement db = new SQLHelperImplement(getApplicationContext(), "GOALS", null, 1);
                db.addNewGoal(goalToAdd);
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}