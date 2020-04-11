package com.example.simplytask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class SubjectTasks extends AppCompatActivity {

    FirebaseFirestore db = GeneralDatabase.db;
    String email;
    String name;
    String subjectID;
    Context context;
    HashMap<Integer, String> checkboxID;
    int checkboxIdIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_tasks);

        Log.d(TAG, "____________________onCreate SubjectOverview");
        Intent intent = getIntent();
        this.email = intent.getStringExtra("Email");
        this.name = intent.getStringExtra("Name");
        this.subjectID = intent.getStringExtra("SubjectID");
        this.context = this;
        this.checkboxID = new HashMap<>();
        this.checkboxIdIndex = 0;
        Log.d(TAG, "____________________Email: " + email + " Name: " + name + " SubjectID: " + subjectID);

        TextView name = findViewById(R.id.name);
        name.setText(this.email);
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
                            LinearLayout layout = (LinearLayout) findViewById(R.id.tableArea);
                            int i = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                Log.d(TAG, "____________________Category with SubjectID " + document.getId() + " => " + document.getData());
                                String categoryID = document.getId();
                                Log.d(TAG, "____________________Category with SubjectID Category: " + categoryID);
                                Object nameObject = data.get("Name");
                                String name = nameObject.toString();
                                Log.d(TAG, "____________________Category with SubjectID Name: " + name);

                                final TableLayout tableLayout = new TableLayout(context);
                                TableRow row = new TableRow(context);
                                TextView title = new TextView(context);
                                title.setText(name);
                                row.addView(title);
                                tableLayout.addView(row);

                                db.collection("Task")
                                        .whereEqualTo("CategoryID", categoryID)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {

                                                    Log.d(TAG, "____________________Task with Category Success");

                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        final TableRow row = new TableRow(context);

                                                        Log.d(TAG, "____________________Task with Category " + document.getId() + " => " + document.getData());
                                                        Map<String, Object> data = document.getData();
                                                        String taskID = document.getId();
                                                        Log.d(TAG, "____________________Task with Category ID " + taskID);
                                                        Object nameObject = data.get("Name");
                                                        final String name = nameObject.toString();
                                                        Log.d(TAG, "____________________Task with Category Name: " + name);

                                                        TextView thing = new TextView(context);
                                                        thing.setText(name);
                                                        row.addView(thing);


                                                        final String statusID = GeneralDatabase.statusID(taskID, email);

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
                                                                            Log.d(TAG, "____________________ key " + key + " d " + d + " isChecked " + isChecked);
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
                                                                            Log.d(TAG, "____________________ key " + key + " d " + d + " isChecked " + c.isChecked());
                                                                            db.collection("Status").document(key).set(d);
                                                                        }
                                                                    });
                                                                    row.addView(checkBox,0);

                                                                } else {
                                                                    Log.d(TAG, "____________________get user failed with ", task.getException());
                                                                    //TODO error  popup - unable to comunicate with server
                                                                    return;
                                                                }
                                                            }
                                                        });


                                                        tableLayout.addView(row);


                                                    }
                                                } else {
                                                    Log.d(TAG, "Error getting task: ", task.getException());
                                                }
                                            }
                                        });

                                layout.addView(tableLayout);
                                i++;
                            }
                        } else {
                            Log.d(TAG, "Error getting Category: ", task.getException());
                        }
                    }
                });


    }
}
