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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.simplytask.Database.GeneralDatabase;
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

public class SubjectOverview extends AppCompatActivity {

    FirebaseFirestore db = GeneralDatabase.db;
    String userID;
    String workerID;
    HashMap<Integer, String> buttonID;
    Context context;

    //TODO investigate storing the list of buttons to prevent them going missing when going back

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_overview);

        Log.d(TAG, "____________________onCreate SubjectOverview");
        Intent intent = getIntent();
        this.userID = intent.getStringExtra("UserID");
        this.workerID = GeneralDatabase.workerID(userID);
        this.buttonID = new HashMap<Integer, String>();
        this.context = this;

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

        Log.d(TAG, "____________________Get collection");

        /*****Subjects Head*****/

        db.collection("Enrollment")
                .whereEqualTo("WorkerID", workerID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "____________________Enrollment Success");
                            final LinearLayout layout = (LinearLayout) findViewById(R.id.buttonArea);
                            int i = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Map<String, Object> data = document.getData();
                                String subjectID = data.get("SubjectID").toString();
                                Log.d(TAG, "____________________Enrollment SubjectID " + subjectID);

                                DocumentReference docRef = db.collection("Subject").document(subjectID);
                                final int finalI = i;
                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "____________________Subject Success");
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {

                                                Log.d(TAG, "____________________Data: " + document.getData());
                                                Map<String, Object> data = document.getData();
                                                String subjectID = document.getId();
                                                Log.d(TAG, "____________________SubjectID " + subjectID);
                                                Object nameObject = data.get("Name");
                                                final String name = nameObject.toString();
                                                Log.d(TAG, "____________________Name " + name);
                                                Log.d(TAG, "____________________PreButtonID " + buttonID);
                                                buttonID.put(finalI, subjectID);
                                                Log.d(TAG, "____________________PostButtonID " + buttonID);

                                                Button button = new Button(context);
                                                button.setId(finalI);
                                                button.setText(name);
                                                button.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent intent = new Intent(context, SubjectTasks.class);
                                                        intent.putExtra("SubjectID", buttonID.get(v.getId()));
                                                        intent.putExtra("Name", name);
                                                        intent.putExtra("WorkerID", workerID);
                                                        intent.putExtra("UserID", userID);
                                                        startActivity(intent);
                                                    }
                                                });
                                                layout.addView(button);
                                                Log.d(TAG, "____________________Post addView");

                                            } else {
                                                Log.d(TAG, "No such document");
                                            }
                                        } else {
                                            Log.d(TAG, "get failed with ", task.getException());
                                        }
                                    }
                                });
                                i++;
                            }
                        } else {
                            Log.d(TAG, "____________________Error getting documents: ", task.getException());
                        }
                    }
                });

        /*****Subjects Bottom*****/

    }
}
