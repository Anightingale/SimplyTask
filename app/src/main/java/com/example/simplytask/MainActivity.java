package com.example.simplytask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.content.ContentValues.TAG;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GeneralDatabase.addSubject("email", "Math");
        GeneralDatabase.addSubject("email", "Science");
        GeneralDatabase.addSubject("email", "History");
        GeneralDatabase.addSubject("email", "English");
        String subjectID = GeneralDatabase.subjectID("email", "History");
        GeneralDatabase.addCategory(subjectID, "Assignment");
        GeneralDatabase.addCategory(subjectID, "Homework");
        GeneralDatabase.addCategory(subjectID, "Fun");
        String categoryID = GeneralDatabase.categoryID(subjectID, "Assignment");
        GeneralDatabase.addTask(categoryID, "proj1");
        GeneralDatabase.addTask(categoryID, "Ass1");
        GeneralDatabase.addTask(categoryID, "Essay");
        GeneralDatabase.addTask(categoryID, "Report");
        GeneralDatabase.addTask(categoryID, "Presentation");
    }

    public void logIn(final View view){


        EditText emailText = (EditText) findViewById(R.id.email);
        String email = emailText.getText().toString();
        EditText passwordText = (EditText) findViewById(R.id.password);
        String password = passwordText.getText().toString();

        FirebaseFirestore db = GeneralDatabase.db;
        DocumentReference docRef = db.collection("User").document(email);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "____________________User data: " + document.getData());

                        goToSubjectOverview(view);

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
        });
        return;
    }

    public void goToSubjectOverview(View view){

        Intent intent = new Intent(this, SubjectOverview.class);

        EditText emailText = (EditText) findViewById(R.id.email);
        String email = emailText.getText().toString();
        Log.d(TAG, "____________________email: " + email);

//        intent.putExtra("Email", email);
        intent.putExtra("Email", "email");
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
        GeneralDatabase.addUser(email, password);
        Log.d(TAG, "____________________Added to database?");

        goToSubjectOverview(view);

    }

}
