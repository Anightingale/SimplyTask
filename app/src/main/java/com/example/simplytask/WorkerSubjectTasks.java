package com.example.simplytask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

public class WorkerSubjectTasks extends AppCompatActivity {

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_subject_tasks);

        this.context = this;

        Spinner spinner = (Spinner) findViewById(R.id.spinner8);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parentView, View selectedItem, int pos, long id){
                Log.d("TAG", "____________________ITEM SELECTED parent__ "+ parentView+" __selected__ "+selectedItem);
                Spinner s = (Spinner) parentView;
                String user = s.getSelectedItem().toString();
                if(user.equals("Manager")){
                    Intent intent = new Intent(context, ManagerSubjectOverview.class);
                    startActivity(intent);
                }
                return;
            }
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

    }
}
