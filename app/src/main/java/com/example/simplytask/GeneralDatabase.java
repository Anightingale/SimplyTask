package com.example.simplytask;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.*;

import static android.content.ContentValues.TAG;

public class GeneralDatabase {

    public static FirebaseFirestore db= FirebaseFirestore.getInstance();

    public static void addUser(final String email, String name) {

        Map<String, Object> user = new HashMap<>();

        user.put("Name", name);
        user.put("Email", email);

        db.collection("User").document(email).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "User added with ID: " + email);
                        addWorker(email);
                        addManager(email);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding User", e);
                    }
                });
    }

    public static void addWorker(final String email){

        Map<String, Object> worker = new HashMap<>();

        worker.put("Email", email);

        db.collection("Worker").document(email).set(worker)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Worker added with ID: " + email);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding Worker", e);
                    }
                });
    }

    public static void addManager(final String email){

        Map<String, Object> manager = new HashMap<>();

        manager.put("Email", email);

        db.collection("Manager").document(email).set(manager)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Manager added with ID: " + email);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding Manager", e);
                    }
                });
    }

    public static String subjectID(String ownerEmail, String name){
        return ownerEmail+name;
    }

    public static void addSubject(String ownerEmail, String name) {

        Map<String, Object> subject = new HashMap<>();
        final String id = subjectID(ownerEmail, name);

        subject.put("Name", name);
        subject.put("Owner", ownerEmail);

        db.collection("Subject").document(id).set(subject)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Subject added with ID: " + id);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding Subject", e);
                    }
                });
    }

    public static String categoryID(String subjectID, String name){
        return subjectID+name;
    }

    public static void addCategory(String subjectID, String name){

        Map<String, Object> category = new HashMap<>();
        final String id = categoryID(subjectID, name);

        category.put("Name", name);
        category.put("Subject", subjectID);

        db.collection("Category").document(id).set(category)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Category added with ID: " + id);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding Category", e);
                    }
                });

    }

    public static String taskID(String categoryId, String taskName){
        return categoryId+taskName;
    }

    public static void addTask(String categoryId, String taskName){

        Map<String, Object> task = new HashMap<>();
        final String id = taskID(categoryId, taskName);

        task.put("Name", taskName);
        task.put("Category", categoryId);

        db.collection("Task").document(id).set(task)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Task added with ID: " + id);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding Task", e);
                    }
                });

    }

    public static String fieldID(String categoryID, String name){
        return  categoryID+name;
    }

    public static void addField(String categoryID, String name){

        Map<String, Object> field = new HashMap<>();
        final String id = fieldID(categoryID, name);

        field.put("Name", name);
        field.put("Category", categoryID);

        db.collection("Field").document(id).set(field)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Field added with ID: " + id);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding Field", e);
                    }
                });

    }

    public static String tagID(String fieldID, String name){
        return  fieldID+name;
    }

    public static void addTag(String fieldID, String name){

        Map<String, Object> tag = new HashMap<>();
        final String id = tagID(fieldID, name);

        tag.put("Name", name);
        tag.put("Field", fieldID);

        db.collection("Tag").document(id).set(tag)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "tag added with ID: " + id);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding tag", e);
                    }
                });

    }

    public static String statusID(String taskID, String workerID){
        return taskID+workerID;
    }

    public static void addStatus(String taskID, String workerID, String status){

        Map<String, Object> statusObject = new HashMap<>();
        final String id = subjectID(taskID, workerID);

        statusObject.put("Status", statusObject);
        statusObject.put("Task", taskID);
        statusObject.put("Worker", workerID);

        db.collection("Status").document(id).set(statusObject)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Status added with ID: " + id);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Status adding tag", e);
                    }
                });

    }

//    public static void getUser(String email, final Runnable<Map<String, Object>> callback){
//        final DocumentReference docRef = db.collection("User").document(email);
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        Log.d(TAG, "User data: " + document.getData());
//                    } else {
//                        Log.d(TAG, "No such user");
//                    }
//                } else {
//                    Log.d(TAG, "get user failed with ", task.getException());
//                }
//            }
//        });
//
//    }

}