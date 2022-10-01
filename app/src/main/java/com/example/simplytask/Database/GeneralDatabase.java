package com.example.simplytask.Database;

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

    //TODO Figure out how this is actually meant to connect since I don't see where it uses google-services.json...
    //TODO Refactor this into an Interface and have classes to handle the listener logic

    public static FirebaseFirestore db= FirebaseFirestore.getInstance();

    public static String userID(String email){
        return email;
    }

    public static void addUser(final String email, String password) {

        Map<String, Object> data = new HashMap<>();
        final String userID = userID(email);

        data.put("Password", password);
        data.put("Email", email);

        db.collection("User").document(userID).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "User added with ID: " + userID);
                        addWorker(userID);
                        addManager(userID);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding User", e);
                    }
                });
    }

    public static String workerID(String userID){
        return "worker" + userID;
    }

    public static void addWorker(final String userID){

        Map<String, Object> data = new HashMap<>();
        final String workerID = workerID(userID);

        data.put("UserID", userID);

        db.collection("Worker").document(workerID).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Worker added with ID: " + workerID);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding Worker", e);
                    }
                });
    }

    public static String managerID(String userID){
        return "manager" + userID;
    }

    public static void addManager(final String userID){

        Map<String, Object> data = new HashMap<>();
        String managerID = managerID(userID);

        data.put("UserID", userID);

        db.collection("Manager").document(managerID).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Manager added with ID: " + userID);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding Manager", e);
                    }
                });
    }

    public static String subjectID(String managerID, String name){
        return managerID+name;
    }

    public static void addSubject(String managerID, String name) {
        Log.d(TAG, "____________________AddSubject Database");

        Map<String, Object> data = new HashMap<>();
        final String subjectID = subjectID(managerID, name);

        data.put("Name", name);
        data.put("ManagerID", managerID);

        db.collection("Subject").document(subjectID).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Subject added with ID: " + subjectID);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding Subject", e);
                    }
                });
    }

    public static String enrollmentID(String subjectID, String workerID){
        return subjectID+workerID;
    }

    public static void addEnrollemnt(String subjectID, String workerID){

        Map<String, Object> data = new HashMap<>();
        final String enrollmentID = enrollmentID(subjectID, workerID);

        data.put("WorkerID", workerID);
        data.put("SubjectID", subjectID);

        db.collection("Enrollment").document(enrollmentID).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Subject added with ID: " + enrollmentID);
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

        Map<String, Object> data = new HashMap<>();
        final String categoryID = categoryID(subjectID, name);

        data.put("Name", name);
        data.put("SubjectID", subjectID);

        db.collection("Category").document(categoryID).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Category added with ID: " + categoryID);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding Category", e);
                    }
                });

    }

    public static String taskID(String categoryID, String name){
        return categoryID+name;
    }

    public static void addTask(String categoryID, String name){

        Map<String, Object> data = new HashMap<>();
        final String taskID = taskID(categoryID, name);

        data.put("Name", name);
        data.put("CategoryID", categoryID);

        db.collection("Task").document(taskID).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Task added with ID: " + taskID);
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

        Map<String, Object> data = new HashMap<>();
        final String fieldID = fieldID(categoryID, name);

        data.put("Name", name);
        data.put("CategoryID", categoryID);

        db.collection("Field").document(fieldID).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Field added with ID: " + fieldID);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding Field", e);
                    }
                });

    }

    public static String valueID(String fieldID, String name){
        return fieldID+name;
    }

    public static void addValue(String fieldID, String name){

        Map<String, Object> data = new HashMap<>();
        final String valueID = fieldID(fieldID, name);

        data.put("Name", name);
        data.put("FieldID", fieldID);

        db.collection("Value").document(valueID).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Field added with ID: " + valueID);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding Field", e);
                    }
                });

    }

    public static String tagID(String taskID, String fieldName, String workerID){
        return  taskID+fieldName+workerID;
    }

    public static void addTag(String taskID, String fieldName, String workerID, String value){

        Map<String, Object> data = new HashMap<>();
        final String tagID = tagID(taskID, fieldName, workerID);

        data.put("TaskID", taskID);
        data.put("FieldName", fieldName);
        data.put("WorkerID", workerID);
        data.put("Value", value);

        db.collection("Tag").document(tagID).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "tag added with ID: " + tagID);
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

    public static void addStatus(String taskID, String workerID, Boolean status){

        Map<String, Object> data = new HashMap<>();
        final String statusID = statusID(taskID, workerID);

        data.put("Status", status);
        data.put("TaskID", taskID);
        data.put("WorkerID", workerID);

        db.collection("Status").document(statusID).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Status added with ID: " + statusID);
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