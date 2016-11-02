package com.example.tonyquick.testapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tonyquick.testapp.R;
import com.example.tonyquick.testapp.services.DatabaseHelper;
import com.example.tonyquick.testapp.models.KnownActivitiesRecord;

import java.util.Arrays;
import java.util.List;

public class NewActivity extends AppCompatActivity {


    Button cancel;
    Button confirm;
    Spinner categorySpin;
    EditText inputActivity;
    Context c;
    KnownActivitiesRecord kar,karReturn;
    DatabaseHelper db;
    List<String> categories;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_activity);

        cancel = (Button) findViewById(R.id.cancel);
        confirm = (Button) findViewById(R.id.confirm);
        inputActivity = (EditText) findViewById(R.id.inputActivity);
        categorySpin = (Spinner) findViewById(R.id.categorySpin);
        c = this.getApplicationContext();

        //categories displayed in spinner
        categories = Arrays.asList("Business","Leisure","Tourism");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,categories);
        categorySpin.setAdapter(adapter);

        //cancel new activity, do nothign
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                setResult(RESULT_CANCELED,i);
                finish();
            }
        });

        //add new activity to known activities
        //return new knownactivity in bundle, must be parcelable

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //user must have selected an activity category and name
                if (inputActivity.getText().toString().isEmpty()){
                    Toast.makeText(c,"Activity name cannot be blank",Toast.LENGTH_LONG).show();

                }
                else{
                    String name = inputActivity.getText().toString();
                    String category = (String)categorySpin.getSelectedItem();
                    kar = new KnownActivitiesRecord(category,name);
                    db = DatabaseHelper.getInstance(c);
                    karReturn=db.addKnownActivity(kar);
                    Intent i = new Intent();
                    Bundle b = new Bundle();
                    b.putParcelable("knownActivity", karReturn);
                    i.putExtras(b);
                    setResult(RESULT_OK, i);
                    finish();
                }

            }
        });

    }

}
