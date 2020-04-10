package com.example.simplytask;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class GeneralDatabase {

    public static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static void main(){
        GeneralDatabase.helloWorld();
        GeneralDatabase.addTest();
    }

    public static void helloWorld(){
        System.out.println("hello world");
        return;
    }

    public static void addTest() {
        Map<String, Object> user = new HashMap<>();
        user.put("first", "A");
        user.put("last", "Z");

        db.collection("users").add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

    }
}