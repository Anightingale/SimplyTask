package com.example.simplytask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
                            Log.d(TAG, "____________________Success");
                            LinearLayout layout = (LinearLayout) findViewById(R.id.tableArea);
                            int i = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                Log.d(TAG, "____________________" + document.getId() + " => " + document.getData());
                                String categoryID = document.getId();
                                Log.d(TAG, "____________________Category: " + categoryID);
                                Object nameObject = data.get("Name");
                                String name = nameObject.toString();
                                Log.d(TAG, "____________________Name: " + name);

                                final TableLayout tableLayout = new TableLayout(context);
                                TableRow row = new TableRow(context);
                                TextView title = new TextView(context);
                                title.setText(name);
                                row.addView(title);
                                tableLayout.addView(row);

                                Log.d(TAG, "____________________PRE FOR");

                                db.collection("Task")
                                        .whereEqualTo("CategoryID", categoryID)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {

                                                    Log.d(TAG, "____________________Success");

                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        TableRow row = new TableRow(context);
                                                        CheckBox checkBox = new CheckBox(context);
                                                        row.addView(checkBox);

                                                        Log.d(TAG, "____________________" + document.getId() + " => " + document.getData());
                                                        Map<String, Object> data = document.getData();
                                                        Object nameObject = data.get("Name");
                                                        String name = nameObject.toString();
                                                        Log.d(TAG, "____________________Name: " + name);

                                                        TextView thing = new TextView(context);
                                                        thing.setText(name);
                                                        row.addView(thing);

                                                        tableLayout.addView(row);
                                                    }
                                                } else {
                                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                                }
                                            }
                                        });










//                                for (int j = 0; j <5; j++) {
//                                    Log.d(TAG, "____________________0");
//
//                                    TableRow row = new TableRow(context);
//                                    Log.d(TAG, "____________________1");
//                                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
//                                    Log.d(TAG, "____________________2");
//                                    row.setLayoutParams(lp);
//                                    Log.d(TAG, "____________________3");
//                                    CheckBox checkBox = new CheckBox(context);
//                                    Log.d(TAG, "____________________4");
//                                    TextView tv = new TextView(context);
//                                    Log.d(TAG, "____________________5");
//                                    Button addBtn = new Button(context);
//                                    Log.d(TAG, "____________________6");
////                                    addBtn.setImageResource(R.drawable.add);
//                                    Log.d(TAG, "____________________7");
//                                    Button minusBtn = new Button(context);
//                                    Log.d(TAG, "____________________8");
////                                    minusBtn.setImageResource(R.drawable.minus);
//                                    Log.d(TAG, "____________________9");
//                                    TextView qty = new TextView(context);
//                                    Log.d(TAG, "____________________10");
//                                    checkBox.setText("hello");
//                                    Log.d(TAG, "____________________11");
//                                    qty.setText("10");
//                                    Log.d(TAG, "____________________12");
//                                    row.addView(checkBox);
//                                    Log.d(TAG, "____________________13");
//                                    row.addView(minusBtn);
//                                    Log.d(TAG, "____________________14");
//                                    row.addView(qty);
//                                    Log.d(TAG, "____________________15");
//                                    row.addView(addBtn);
//                                    Log.d(TAG, "____________________16");
//                                    tableLayout.addView(row);
//                                    Log.d(TAG, "____________________17");
//                                }
                                Log.d(TAG, "____________________POST FOR");

                                layout.addView(tableLayout);
                                i++;
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


    }
}
