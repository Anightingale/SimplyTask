package com.example.simplytask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import static android.content.ContentValues.TAG;

public class ManagerSubjectOverview extends AppCompatActivity {

    Context context;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_subject_overview);


        Log.d(TAG, "____________________onCreate ManagerSubjectOverview");

        Intent intent = getIntent();
        this.email = intent.getStringExtra("Email");
        this.context = this;

        Spinner spinner = (Spinner) findViewById(R.id.spinner4);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parentView, View selectedItem, int pos, long id){
                Log.d("TAG", "____________________ITEM SELECTED parent__ "+ parentView+" __selected__ "+selectedItem);
                Spinner s = (Spinner) parentView;
                String user = s.getSelectedItem().toString();
                if(user.equals("Worker")){
                    Intent intent = new Intent(context, SubjectOverview.class);
                    intent.putExtra("Email", email);
                    startActivity(intent);
                }
                return;
            }
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

    }

    public void gotToSubject(View view){
        Intent intent = new Intent(this, ManagerSubjectView.class);
        startActivity(intent);
    }
}
