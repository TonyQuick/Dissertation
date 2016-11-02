package com.example.tonyquick.testapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.example.tonyquick.testapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateSelector extends AppCompatActivity {

    DatePicker dp;

    //method to allow user to select date from date picker, used by both review day and display data by date

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_selector);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //return selected date
        Button confirm = (Button) findViewById(R.id.confirm);
        dp = (DatePicker) findViewById(R.id.datePicker);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                Bundle b = new Bundle();
                b.putString("date",getDate(dp));
                i.putExtras(b);
                setResult(RESULT_OK, i);
                finish();


            }
        });

        //return action cancelled, ends tasks
        Button cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                setResult(RESULT_CANCELED,i);
                finish();
            }
        });



    }

    private String getDate(DatePicker dp){
        //get selected date from datepicker menu item
        int day = dp.getDayOfMonth();
        int month = dp.getMonth();
        int year = dp.getYear();
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dat = sdf.format(c.getTime());

        Log.d("AJQ", dat);
        return dat;

    }

}
