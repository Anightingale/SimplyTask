package com.example.simplytask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.simplytask.Database.DatabaseInterface;
import com.example.simplytask.Database.GeneralDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import static android.content.ContentValues.TAG;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String email = "email";
        String userID = GeneralDatabase.userID(email);
        String workerID = GeneralDatabase.workerID(userID);
        String managerID = GeneralDatabase.managerID(userID);
        String subjectName = "Simply Task";
        String subjectID = GeneralDatabase.subjectID(managerID, subjectName);
        String enrollmentID = GeneralDatabase.enrollmentID(subjectID, workerID);
        String categoryName = "TO DO";
        String categoryID = GeneralDatabase.categoryID(subjectID, categoryName);
        String fieldName = "Done?";
        String fieldID = GeneralDatabase.fieldID(categoryID, fieldName);
        String value = "no";
        String valueID = GeneralDatabase.valueID(fieldID, value);
        String taskName = "Implement Backend";
        String taskID = GeneralDatabase.taskID(categoryID, taskName);
        String tagID = GeneralDatabase.tagID(taskID, fieldName, workerID);
        String statusID = GeneralDatabase.statusID(taskID, workerID);

//        GeneralDatabase.addUser(email, "pass");
//        GeneralDatabase.addSubject(managerID, subjectName);
//        GeneralDatabase.addEnrollemnt(subjectID, workerID);
//        GeneralDatabase.addCategory(subjectID, categoryName);
//        GeneralDatabase.addField(categoryID, fieldName);
//        GeneralDatabase.addValue(fieldID, value);
//        GeneralDatabase.addTask(categoryID, taskName);
//        GeneralDatabase.addTag(taskID, fieldName, workerID, value);
//        GeneralDatabase.addStatus(taskID, workerID, false);

    }

    public void logIn(final View view){

        Log.d(TAG, "++++ logIn Start");

        EditText emailText = (EditText) findViewById(R.id.email);
        String email = emailText.getText().toString();
        EditText passwordText = (EditText) findViewById(R.id.password);
        String password = passwordText.getText().toString();

        if(email.isEmpty() || password.isEmpty()){
            Log.d(TAG, "____________________email is empty :" + email.isEmpty() + " pass is empty: " + password.isEmpty());
            return;
        }

        FirebaseFirestore db = GeneralDatabase.db;
        DocumentReference docRef = db.collection("User").document(email);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        Log.d(TAG, "____________________User data: " + document.getData());
                        TextView passText = (TextView) findViewById(R.id.password);
                        String passString = passText.getText().toString();
                        if(passString.equals(data.get("Password").toString())) {
                            goToSubjectOverview(view);
                        }
                    } else {
                        Log.d(TAG, "____________________No such user");
                        //TODO error popup - incorrect credentials
                        return;
                    }
                } else {
                    Log.d(TAG, "____________________get user failed with ", task.getException());
                    //TODO error  popup - unable to comunicate with server
                    return;
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "____________________Get User Doc Failed");
            }
        });
        return;
    }

    public void goToSubjectOverview(View view){

        Intent intent = new Intent(this, SubjectOverview.class);

        EditText emailText = (EditText) findViewById(R.id.email);
        String email = emailText.getText().toString();
        Log.d(TAG, "____________________email: " + email);

        String userID = GeneralDatabase.userID(email);

        intent.putExtra("UserID", userID);
        Log.d(TAG, "____________________Start Activity");
        startActivity(intent);

    }

    public void createUser(View view){

        Log.d(TAG, "____________________createUser");
        EditText emailText = (EditText) findViewById(R.id.email);
        String email = emailText.getText().toString();
        Log.d(TAG, "____________________email: " + email);
        EditText passwordText = (EditText) findViewById(R.id.password);
        String password = passwordText.getText().toString();
        Log.d(TAG, "____________________pass: " + password);
        if(email.isEmpty() || password.isEmpty()){
            Log.d(TAG, "____________________email is empty :" + email.isEmpty() + " pass is empty: " + password.isEmpty());
            return;
        }
        DatabaseInterface.addUser(email, password);
        Log.d(TAG, "____________________Added to database?");

        goToSubjectOverview(view);

    }

}
