package com.example.tonyquick.testapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tonyquick.testapp.models.PotentialStop;
import com.example.tonyquick.testapp.R;
import com.example.tonyquick.testapp.services.DatabaseHelper;
import com.example.tonyquick.testapp.models.KnownLocationsRecord;

public class NewLocation extends AppCompatActivity {


    Button cancel;
    Button confirm;
    EditText inputLocation;
    PotentialStop p;
    KnownLocationsRecord klr, klrReturn;
    Context c;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_location);

        cancel = (Button) findViewById(R.id.cancel);
        confirm = (Button) findViewById(R.id.confirm);
        inputLocation = (EditText) findViewById(R.id.inputLocation);

        Bundle b = getIntent().getExtras();
        p = b.getParcelable("potentialStop");
        c = this.getApplicationContext();
        db = DatabaseHelper.getInstance(c);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                setResult(RESULT_CANCELED,i);
                finish();
            }
        });

        //add new location to database taking location from potential stops and name from input text box
        //name cannot be blank

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputLocation.getText().toString()==null){
                    Toast.makeText(c,"Location name cannot be blank",Toast.LENGTH_LONG).show();
                }
                else{
                    String name = inputLocation.getText().toString();

                    klr = new KnownLocationsRecord(name,p.getLongitude(),p.getLatitude());
                    klrReturn=db.addKnownLocation(klr);

                    Intent i = new Intent();
                    Bundle b = new Bundle();
                    b.putParcelable("knownLocation",klrReturn);
                    i.putExtras(b);
                    setResult(RESULT_OK,i);

                    finish();

                }

            }
        });




    }

}
