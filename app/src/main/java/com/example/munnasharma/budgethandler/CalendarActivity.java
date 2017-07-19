package com.example.munnasharma.budgethandler;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;

import classes.Constant;

/**
 * Created by MunnaSharma on 7/14/2017.
 */

public class CalendarActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_view);

        calendarView=(CalendarView)findViewById(R.id.CalenderView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date=dayOfMonth+"/"+month+"/"+year;
                sharedPreferences=getApplicationContext().getSharedPreferences(Constant.MYPref,MODE_PRIVATE);
                editor=sharedPreferences.edit();
                editor.putString(Constant.Date,date);
                editor.commit();
                Intent i =new Intent(getApplicationContext(),DataEntry.class);
                startActivity(i);
            }
        });
    }
}