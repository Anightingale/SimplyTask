package com.example.simplytask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class SubjectOverview extends AppCompatActivity {

    FirebaseFirestore db = GeneralDatabase.db;
    String email;
    HashMap<Integer, String> buttonID;

    //TODO investigate storing the list of buttons to prevent them going missing when going back

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_overview);

        Log.d(TAG, "____________________onCreate SubjectOverview");
        Intent intent = getIntent();
        this.email = intent.getStringExtra("Email");
        this.buttonID = new HashMap<Integer, String>();

        TextView name = findViewById(R.id.name);
        name.setText(this.email);

        final Context context = this;

        Log.d(TAG, "____________________Get collection");
        db.collection("Subject")
                .whereEqualTo("Owner", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "____________________Success");
                            LinearLayout layout = (LinearLayout) findViewById(R.id.buttonArea);
                            int i = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, "____________________Data: " + document.getData());
                                Map<String, Object> data = document.getData();
                                String subjectID = document.getId();
                                Log.d(TAG, "____________________SubjectID " + subjectID);
                                Object nameObject = data.get("Name");
                                final String name = nameObject.toString();
                                Log.d(TAG, "____________________Name " + name);
                                Log.d(TAG, "____________________PreButtonID " + buttonID);
                                buttonID.put(i, subjectID);
                                Log.d(TAG, "____________________PostButtonID " + buttonID);



                                Log.d(TAG, "____________________DocID: " + document.getId() + "Data: " + document.getData() + " SubjectID " + subjectID + " Name: " + name);
                                Button button = new Button(context);
                                button.setId(i);
                                button.setText(name);
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(context, SubjectTasks.class);
                                        intent.putExtra("SubjectID", buttonID.get(v.getId()));
                                        intent.putExtra("Name", name);
                                        intent.putExtra("Email", email);
                                        startActivity(intent);
                                    }
                                });
                                layout.addView(button);
                                Log.d(TAG, "____________________Post addView");
                                i++;

                            }
                        } else {
                            Log.d(TAG, "____________________Error getting documents: ", task.getException());
                        }
                    }
                });


    }
}
