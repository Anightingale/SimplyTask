package com.example.simplytask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.simplytask.Database.GeneralDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class SubjectTasks extends AppCompatActivity {

    FirebaseFirestore db = GeneralDatabase.db;
    String userID;
    String workerID;
    String name;
    String subjectID;
    Context context;
    HashMap<Integer, String> checkboxID;
    int checkboxIdIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_tasks);

        Log.d(TAG, "____________________onCreate SubjectView");
        Intent intent = getIntent();
        this.userID = intent.getStringExtra("UserID");
        this.name = intent.getStringExtra("Name");
        this.workerID = intent.getStringExtra("WorkerID");
        this.subjectID = intent.getStringExtra("SubjectID");
        this.context = this;
        this.checkboxID = new HashMap<>();
        this.checkboxIdIndex = 0;

        /*****Create Spinner Top*****/

        Spinner spinner = (Spinner) findViewById(R.id.modeSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parentView, View selectedItem, int pos, long id){
                Log.d("TAG", "____________________ITEM SELECTED parent__ "+ parentView+" __selected__ "+selectedItem);
                Spinner s = (Spinner) parentView;
                String user = s.getSelectedItem().toString();
                if(user.equals("Manager")){
                    Intent intent = new Intent(context, ManagerSubjectOverview.class);
                    intent.putExtra("UserID", userID);
                    startActivity(intent);
                }
                return;
            }
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

        /*****Create Spinner Bottom*****/

        TextView name = findViewById(R.id.name);
        name.setText(this.userID);
        TextView subjectName = findViewById(R.id.subjectName);
        subjectName.setText(this.name);

        db.collection("Category")
                .whereEqualTo("SubjectID", this.subjectID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "____________________Category with SubjectID Success");
                            final LinearLayout layout = (LinearLayout) findViewById(R.id.tableArea);
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                Log.d(TAG, "____________________Category with SubjectID " + document.getId() + " => " + document.getData());
                                final String categoryID = document.getId();
                                Log.d(TAG, "____________________Category with SubjectID Category: " + categoryID);
                                Object nameObject = data.get("Name");
                                String name = nameObject.toString();
                                Log.d(TAG, "____________________Category with SubjectID Name: " + name);

                                final TableLayout tableLayout = new TableLayout(context);
                                final TableRow row = new TableRow(context);
                                TextView title = new TextView(context);
                                title.setText(name);
                                row.addView(title);
                                tableLayout.addView(row);

                                /************Fields Start****************/

                                db.collection("Field")
                                        .whereEqualTo("CategoryID", categoryID)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                final ArrayList<String> fields = new ArrayList<>();

                                                TableRow head = new TableRow(context);

                                                TextView stat = new TextView(context);
                                                stat.setText("Status");
                                                head.addView(stat);

                                                TextView titl = new TextView(context);
                                                titl.setText("Task");
                                                head.addView(titl);

                                                if (task.isSuccessful()) {
                                                    Log.d(TAG, "____________________Field with CategoryID Success");
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        Log.d(TAG, "____________________Field with CategoryID " + document.getId() + " => " + document.getData());
                                                        Map<String, Object> data = document.getData();
                                                        Object nameObject = data.get("Name");
                                                        String name = nameObject.toString();
                                                        Log.d(TAG, "____________________Category with SubjectID Name: " + name);
                                                        fields.add(name);
                                                        TextView field = new TextView(context);
                                                        field.setText(name);
                                                        head.addView(field);
                                                    }
                                                } else {
                                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                                }

                                                tableLayout.addView(head);

                                                /********TASK BODY TOP*********/


                                                db.collection("Task")
                                                        .whereEqualTo("CategoryID", categoryID)
                                                        .get()
                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                if (task.isSuccessful()) {

                                                                    Log.d(TAG, "____________________Task with Category Success");

                                                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                                                        final TableRow body = new TableRow(context);

                                                                        Log.d(TAG, "____________________Task with Category " + document.getId() + " => " + document.getData());
                                                                        Map<String, Object> data = document.getData();
                                                                        final String taskID = document.getId();
                                                                        Log.d(TAG, "____________________Task with Category ID " + taskID);
                                                                        Object nameObject = data.get("Name");
                                                                        final String name = nameObject.toString();
                                                                        Log.d(TAG, "____________________Task with Category Name: " + name);

                                                                        TextView job = new TextView(context);
                                                                        job.setText(name);
                                                                        body.addView(job);

                                                                        final String statusID = GeneralDatabase.statusID(taskID, workerID);

                                                                        /**********Status Top**********/

                                                                        db.collection("Status")
                                                                                .document(statusID)
                                                                                .get()
                                                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                        if (task.isSuccessful()) {
                                                                                            Log.d(TAG, "____________________Status with StatusID Success");
                                                                                            DocumentSnapshot document = task.getResult();
                                                                                            Boolean status = false;
                                                                                            Log.d(TAG, "____________________Status with StatusID status" + status);

                                                                                            if (document.exists()) {
                                                                                                Log.d(TAG, "____________________Status with StatusID" + document.getId() + " => " + document.getData());

                                                                                                Map<String, Object> data = new HashMap<>();
                                                                                                data = document.getData();
                                                                                                status = (Boolean) data.get("Status");

                                                                                            } else {
                                                                                                Log.d(TAG, "____________________No such Status");
                                                                                                //TODO error popup - incorrect credentials

                                                                                            }

                                                                                            final CheckBox checkBox = new CheckBox(context);
                                                                                            checkBox.setId(checkboxIdIndex);
                                                                                            checkboxID.put(checkboxIdIndex, statusID);
                                                                                            checkboxIdIndex++;
                                                                                            checkBox.setChecked(status);
                                                                                            Log.d(TAG, "____________________SetOnCheckedChangeListener");
                                                                                            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                                                                @Override
                                                                                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                                                                    int id = buttonView.getId();
                                                                                                    String key = checkboxID.get(id);
                                                                                                    Map<String, Object> d = new HashMap<>();
                                                                                                    d.put("Status", isChecked);
                                                                                                    Log.d(TAG, "____________________ onChecked key " + key + " d " + d + " isChecked " + isChecked);
                                                                                                    db.collection("Status").document(key).set(d);
                                                                                                }
                                                                                            });
                                                                                            checkBox.setOnClickListener(new View.OnClickListener() {
                                                                                                @Override
                                                                                                public void onClick(View v) {
                                                                                                    CheckBox c = (CheckBox) v;
                                                                                                    int id = c.getId();
                                                                                                    String key = checkboxID.get(id);
                                                                                                    Map<String, Object> d = new HashMap<>();
                                                                                                    d.put("Status", c.isChecked());
                                                                                                    Log.d(TAG, "____________________ onClicked key " + key + " d " + d + " isChecked " + c.isChecked());
                                                                                                    db.collection("Status").document(key).set(d);
                                                                                                }
                                                                                            });
                                                                                            body.addView(checkBox,0);


                                                                                            for(String field : fields){

                                                                                                /***********Tag Top**********/
                                                                                                String tagID = GeneralDatabase.tagID(taskID, field, workerID);
                                                                                                db.collection("Tag").document(tagID).get()
                                                                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                        TextView tag = new TextView(context);
                                                                                                        if (task.isSuccessful()) {
                                                                                                            Log.d(TAG, "____________________Tag with TagID Success");
                                                                                                            DocumentSnapshot document = task.getResult();
                                                                                                            if (document.exists()) {
                                                                                                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                                                                                                Map<String, Object> data = document.getData();
                                                                                                                Object valueObject = data.get("Value");
                                                                                                                String valueString = valueObject.toString();
                                                                                                                tag.setText(valueString);
                                                                                                            } else {
                                                                                                                Log.d(TAG, "No such document");
                                                                                                            }
                                                                                                        } else {
                                                                                                            Log.d(TAG, "get failed with ", task.getException());
                                                                                                        }
                                                                                                        Log.d(TAG, "____________________1");
                                                                                                        body.addView(tag);
                                                                                                        Log.d(TAG, "____________________2");
                                                                                                    }
                                                                                                });
                                                                                                /***********Tag Bottom**********/
                                                                                            }

                                                                                        } else {
                                                                                            Log.d(TAG, "____________________get Status failed with ", task.getException());
                                                                                            //TODO error  popup - unable to comunicate with server
                                                                                            return;
                                                                                        }
                                                                                    }
                                                                                });

                                                                        /**********Status Bottom**********/
                                                                        Log.d(TAG, "____________________3");

                                                                        tableLayout.addView(body);

                                                                        Log.d(TAG, "____________________4");

                                                                    }
                                                                } else {
                                                                    Log.d(TAG, "Error getting task: ", task.getException());
                                                                }
                                                            }
                                                        });

                                                Log.d(TAG, "____________________5");
                                                layout.addView(tableLayout);
                                                Log.d(TAG, "____________________6");

                                                /*******TASK BODY BOTTOM**********/

                                            }
                                        });



                                /************Fields end****************/


                            }
                        } else {
                            Log.d(TAG, "Error getting Category: ", task.getException());
                        }
                    }
                });


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        onCreate(savedInstanceState);
    }
}
