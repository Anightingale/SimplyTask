package com.example.simplytask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.simplytask.Database.GeneralDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class ManagerSubjectView extends AppCompatActivity {

    FirebaseFirestore db = GeneralDatabase.db;
    String userID;
    String managerID;
    String name;
    String subjectID;
    Context context;
    HashMap<Integer, String> checkboxID;
    int checkboxIdIndex;
    HashMap<Integer, Integer> buttonToEditText;
    HashMap<Integer, String> buttonToCategory;
    HashMap<Integer, Integer> buttonToTable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_subject_view);

        Log.d(TAG, "____________________onCreate ManagerSubjectView");
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        for (String key : bundle.keySet()) {
            Log.d(TAG, "____________________" + key + " : " + (bundle.get(key) != null ? bundle.get(key) : "NULL"));
        }        this.userID = intent.getStringExtra("UserID");
        this.name = intent.getStringExtra("Name");
        this.managerID = intent.getStringExtra("ManagerID");
        this.subjectID = intent.getStringExtra("SubjectID");
        this.context = this;
        this.checkboxID = new HashMap<>();
        this.checkboxIdIndex = 0;
        this.buttonToEditText = new HashMap<>();
        this.buttonToCategory = new HashMap<>();
        this.buttonToTable = new HashMap<>();

        Log.d(TAG, "____________________Create Spinner");
        Spinner spinner = (Spinner) findViewById(R.id.modeSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parentView, View selectedItem, int pos, long id){
                Log.d("TAG", "____________________ITEM SELECTED parent__ "+ parentView+" __selected__ "+selectedItem);
                Spinner s = (Spinner) parentView;
                String user = s.getSelectedItem().toString();
                if(user.equals("Worker")){
                    Intent intent = new Intent(context, SubjectOverview.class);
                    intent.putExtra("UserID", userID);
                    startActivity(intent);
                }
                return;
            }
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

        TextView name = findViewById(R.id.name);
        name.setText(this.userID);
        TextView subjectName = findViewById(R.id.subjectName);
        subjectName.setText(this.name);
        final LinearLayout layout = (LinearLayout) findViewById(R.id.tableArea);

        Log.d(TAG, "____________________Get Data");
        db.collection("Category")
                .whereEqualTo("SubjectID", this.subjectID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "____________________Category with SubjectID Success");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                Log.d(TAG, "____________________Category with SubjectID " + document.getId() + " => " + document.getData());
                                final String categoryID = document.getId();
                                Log.d(TAG, "____________________Category with SubjectID Category: " + categoryID);
                                Object nameObject = data.get("Name");
                                String name = nameObject.toString();
                                Log.d(TAG, "____________________Category with SubjectID Name: " + name);

                                final TableLayout tableLayout = new TableLayout(context);
                                final int tableID = tableLayout.generateViewId();
                                tableLayout.setId(tableID);
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

                                                                        for(String field : fields){

                                                                            /***********Tag Top**********/
                                                                            String tagID = GeneralDatabase.tagID(taskID, field, managerID);
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
                                                                                                    Object tagObject = data.get("Tag");
                                                                                                    String tagString = tagObject.toString();
                                                                                                    tag.setText(tagString);
                                                                                                } else {
                                                                                                    Log.d(TAG, "No such document");
                                                                                                }
                                                                                            } else {
                                                                                                Log.d(TAG, "get failed with ", task.getException());
                                                                                            }
                                                                                            body.addView(tag);
                                                                                        }
                                                                                    });
                                                                            /***********Tag Bottom**********/
                                                                        }

                                                                        tableLayout.addView(body);


                                                                    }

                                                                    TableRow end = new TableRow(context);

                                                                    Button button = new Button(context);
                                                                    EditText editText = new EditText(context);

                                                                    int buttonID = button.generateViewId();
                                                                    int editTextID = editText.generateViewId();
                                                                    buttonToEditText.put(buttonID, editTextID);
                                                                    buttonToCategory.put(buttonID, categoryID);
                                                                    buttonToTable.put(buttonID, tableID);

                                                                    button.setId(buttonID);
                                                                    editText.setId(editTextID);
                                                                    button.setText("Add Task");
                                                                    /*****Start OnClick*****/
                                                                    button.setOnClickListener(new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View v) {

                                                                            Log.d(TAG, "____________________Add Task");
                                                                            final int buttonID = v.getId();
                                                                            TextView taskText = findViewById(buttonToEditText.get(buttonID));
                                                                            final String taskString = taskText.getText().toString();
                                                                            if(taskString.isEmpty()){
                                                                                return;
                                                                            }
                                                                            Log.d(TAG, "____________________AddSubject Database");

                                                                            String taskID = GeneralDatabase.taskID(categoryID, taskString);

                                                                            Map<String, Object> data = new HashMap<>();
                                                                            data.put("Name", taskString);
                                                                            data.put("CategoryID", buttonToCategory.get(buttonID));

                                                                            db.collection("Task").document(taskID).set(data)
                                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                        @Override
                                                                                        public void onSuccess(Void aVoid) {
                                                                                            Log.d(TAG, "Subject added with ID: " + subjectID);

                                                                                            final TableLayout table = (TableLayout) findViewById(buttonToTable.get(buttonID));
                                                                                            TextView textView = new TextView(context);
                                                                                            textView.setText(taskString);
                                                                                            table.addView(textView, table.getChildCount()-1);
                                                                                            //TODO get fields then add tags to a table row

                                                                                        }
                                                                                    })
                                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                                        @Override
                                                                                        public void onFailure(@NonNull Exception e) {
                                                                                            Log.w(TAG, "Error adding Subject", e);
                                                                                        }
                                                                                    });
                                                                        }

                                                                    });
                                                                    /*****End OnClick*****/
                                                                    end.addView(button);
                                                                    end.addView(editText);
                                                                    tableLayout.addView(end);
                                                                } else {
                                                                    Log.d(TAG, "Error getting task: ", task.getException());
                                                                }
                                                            }
                                                        });

                                                layout.addView(tableLayout);

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

    public void onClick(View view){
        Intent intent = new Intent(this, ManagerClassOverview.class);
        intent.putExtra("UserID", userID);
        intent.putExtra("ManagerID", this.managerID);
        intent.putExtra("Name", this.name);
        intent.putExtra("SubjectID", this.subjectID);
        this.
        startActivity(intent);
    }

    public void addCategory(View view){
        /******New Category Start*****/

        EditText addCategoryEditText = (EditText) findViewById(R.id.newCategoryText);
        Log.d(TAG, "____________________Add Category");
        TextView categoryText = addCategoryEditText;
        final String categoryString = categoryText.getText().toString();
        if(categoryString.isEmpty()){
            return;
        }
        Log.d(TAG, "____________________AddCategory Database");

        final String categoryID = GeneralDatabase.categoryID(subjectID, categoryString);

        Map<String, Object> data = new HashMap<>();
        data.put("Name", categoryString);
        data.put("SubjectID", subjectID);

        db.collection("Category").document(categoryID).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Category added with ID: " + subjectID);
                        LinearLayout layout = (LinearLayout) findViewById(R.id.tableArea);
                        final TableLayout tableLayout = new TableLayout(context);
                        final int tableID = tableLayout.generateViewId();
                        tableLayout.setId(tableID);
                        final TableRow row = new TableRow(context);
                        TextView title = new TextView(context);
                        title.setText(categoryString);
                        row.addView(title);
                        tableLayout.addView(row);

                        TableRow end = new TableRow(context);

                        Button button = new Button(context);
                        EditText editText = new EditText(context);

                        int buttonID = button.generateViewId();
                        int editTextID = editText.generateViewId();
                        buttonToEditText.put(buttonID, editTextID);
                        buttonToCategory.put(buttonID, categoryID);
                        buttonToTable.put(buttonID, tableID);

                        button.setId(buttonID);
                        editText.setId(editTextID);
                        button.setText("Add Task");
                        /*****Start OnClick*****/
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Log.d(TAG, "____________________Add Task");
                                final int buttonID = v.getId();
                                TextView taskText = findViewById(buttonToEditText.get(buttonID));
                                final String taskString = taskText.getText().toString();
                                if(taskString.isEmpty()){
                                    return;
                                }
                                Log.d(TAG, "____________________AddSubject Database");

                                String taskID = GeneralDatabase.taskID(categoryID, taskString);

                                Map<String, Object> data = new HashMap<>();
                                data.put("Name", taskString);
                                data.put("CategoryID", buttonToCategory.get(buttonID));

                                db.collection("Task").document(taskID).set(data)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "Subject added with ID: " + subjectID);

                                                final TableLayout table = (TableLayout) findViewById(buttonToTable.get(buttonID));
                                                TextView textView = new TextView(context);
                                                textView.setText(taskString);
                                                table.addView(textView, table.getChildCount()-1);
                                                //TODO get fields then add tags to a table row

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error adding Subject", e);
                                            }
                                         });
                            }

                        });
                        /*****End OnClick*****/
                        end.addView(button);
                        end.addView(editText);
                        tableLayout.addView(end);
                        layout.addView(tableLayout);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding Subject", e);
                    }
                });

    }

}
