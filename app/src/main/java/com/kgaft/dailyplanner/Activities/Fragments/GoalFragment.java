package com.kgaft.dailyplanner.Activities.Fragments;

import static com.kgaft.dailyplanner.R.color.white;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kgaft.dailyplanner.R;
/*
This class requires for goal fragments, in this class we creating goal
 */

public class GoalFragment extends Fragment {

    private TextView mainInfo;
    private ViewGroup group;
    private Button editButton;
    private Button deleteButton;
    private LayoutInflater inflater;
    private int goalId;
    private String goalText;
    public GoalFragment() {
        // Required empty public constructor
    }

    public GoalFragment(int id, String goalText){
        this.goalId = id;
        this.goalText = goalText;
    }


    public void setGoalText(String text){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        this.group = container;
        return inflater.inflate(R.layout.goal_layout, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RelativeLayout layout = view.findViewById(R.id.goalRelativeLayout);
        TextView text = new TextView(getContext());
        text.setText(goalText);     //Setting the goal text for text view
        text.setTextColor(ColorStateList.valueOf(white));
        text.setTextSize(30);
        layout.addView(text);
        editButton = view.findViewById(R.id.editButton);
        deleteButton = view.findViewById(R.id.finishButton);
        editButton.setText(editButton.getText()+" "+goalId);
        deleteButton.setText(deleteButton.getText()+" "+goalId);
    }
    public TextView getTextView(){
        return  mainInfo;
    }
    public Button getEditButton(){
        return editButton;
    }
    public Button getDeleteButton(){
        return deleteButton;
    }
}