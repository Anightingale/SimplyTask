package com.example.simplytask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class ManagerSubjectOverview extends AppCompatActivity {

    FirebaseFirestore db = GeneralDatabase.db;
    String userID;
    String managerID;
    HashMap<Integer, String> buttonID;
    Context context;
    Boolean isFirstAddSubjectClick;
    int i;
    int sub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_subject_overview);

        Log.d(TAG, "____________________onCreate ManagerSubjectOverview");

        Intent intent = getIntent();
        this.userID = intent.getStringExtra("UserID");
        this.managerID = GeneralDatabase.managerID(userID);
        this.buttonID = new HashMap<>();
        this.context = this;
        isFirstAddSubjectClick = true;
        this.i = 0;

        /*****Spinner Top*****/

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

        /*****Spinner Bottom*****/

        TextView subjectName = findViewById(R.id.name);
        subjectName.setText(this.userID);

        Log.d(TAG, "____________________Get collection");

        /*****Subjects Head*****/

        db.collection("Subject")
                .whereEqualTo("ManagerID", managerID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "____________________Subject Success");
                            final LinearLayout layout = (LinearLayout) findViewById(R.id.buttonArea);
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Map<String, Object> data = document.getData();
                                String subjectID = document.getId();
                                Log.d(TAG, "____________________Subject SubjectID " + subjectID);
                                final String subjectName = data.get("Name").toString();
                                Log.d(TAG, "____________________Subject Name " + subjectName);

                                Log.d(TAG, "____________________PreButtonID " + buttonID);
                                buttonID.put(i, subjectID);
                                Log.d(TAG, "____________________PostButtonID " + buttonID);

                                Button button = new Button(context);
                                button.setId(i);
                                button.setText(subjectName);
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(context, ManagerSubjectView.class);
                                        intent.putExtra("SubjectID", buttonID.get(v.getId()));
                                        intent.putExtra("Name", subjectName);
                                        intent.putExtra("UserID", userID);
                                        intent.putExtra("ManagerID", managerID);
                                        startActivity(intent);
                                    }
                                });
                                layout.addView(button);
                                Log.d(TAG, "____________________Post addView");
                                i++;
                            }
                        }
                        else {
                            Log.d(TAG, "____________________get Subject failed with ", task.getException());
                        }
                    }
                });

        /*****Subjects Bottom*****/

    }

    public void addNewSubject(View view){
        if(isFirstAddSubjectClick) {
            Log.d(TAG, "____________________AddSubject EditText create");
            LinearLayout layout = (LinearLayout) findViewById(R.id.textboxHere);
            EditText input = new EditText(context);
            input.setHint("Subject Name");
            input.setBackgroundColor(Color.WHITE);
            this.sub=i++;
            input.setId(sub);
            input.setSingleLine(true);
            layout.addView(input);
            isFirstAddSubjectClick = false;
        }
        else {
            Log.d(TAG, "____________________AddSubject New Subject create");
            TextView subjectText = findViewById(sub);
            final String subjectName = subjectText.getText().toString();
            if(subjectName.isEmpty()){
                return;
            }
            Log.d(TAG, "____________________AddSubject Database");

            Map<String, Object> data = new HashMap<>();
            final String subjectID = GeneralDatabase.subjectID(managerID, subjectName);

            data.put("Name", subjectName);
            data.put("ManagerID", managerID);

            db.collection("Subject").document(subjectID).set(data)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                Log.d(TAG, "Subject added with ID: " + subjectID);
                final LinearLayout layout = (LinearLayout) findViewById(R.id.buttonArea);
                Button button = new Button(context);
                button.setId(i);
                button.setText(subjectName);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ManagerSubjectView.class);
                        intent.putExtra("SubjectID", buttonID.get(v.getId()));
                        intent.putExtra("Name", subjectName);
                        intent.putExtra("UserID", userID);
                        intent.putExtra("ManagerID", managerID);
                        Log.d(TAG, "____________________Button onClick intent: " + intent.toString());
                        startActivity(intent);
                    }
                });
                layout.addView(button);
                i++;

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
}
