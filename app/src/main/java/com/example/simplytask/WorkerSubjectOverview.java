package com.example.simplytask;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class WorkerSubjectOverview extends AppCompatActivity {

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_subject_overview);
        this.context = this;

        Spinner spinner = (Spinner) findViewById(R.id.spinner2);
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

    public void goToSubject(View view){
        Intent intent = new Intent(this, WorkerSubjectTasks.class);
        startActivity(intent);
    }

    public void swapUserType(View view){
        Spinner spinner = (Spinner) view;
        String type = spinner.getPrompt().toString();
        if(type.equals("Manager")){
            Intent intent = new Intent(this, ManagerSubjectOverview.class);
            startActivity(intent);
        }
    }

}
