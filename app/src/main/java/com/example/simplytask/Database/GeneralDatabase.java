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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.*;

import static android.content.ContentValues.TAG;

/**
 * This Class is intended to act as a base for all other Database Interacting Classes. It provides
 * the Base Controls to make sure each Class only handles one Call at a time. This ensures that
 * the Classes only store the results and information for the most recent Call and no 'wires get
 * crossed'
 */
@SuppressWarnings({"UnnecessaryContinue", "UnnecessaryReturnStatement"})
public class GeneralDatabase implements OnCompleteListener {

    /*Private Constant*/
    //TODO once refactoring is done add final, remove static, and don't initialise
    private static String TAG = GeneralDatabase.class.getCanonicalName();

    /*Protected Constant*/
    //TODO after refactoring is done Remove Static, and make protected
    public static final FirebaseFirestore db= FirebaseFirestore.getInstance();

    /*Protected Variables*/
    //isCallComplete vs isCallRunning is important for the waitForCallToComplete() method to avoid
    //an infinite loop
    protected boolean isCallComplete = false;
    protected boolean isCallRunning = false;
    protected boolean callSuccess = false;
    protected Exception exception = null;
    protected Task<?> currentTask = null;

    /*Constructors*/

    /**
     * Main Constructor for this Class. Used to provide the TAG Value that should be used for
     * logging
     * @param TAG  The Value that should be used to record the Logs relative Location in the Logging
     */
    protected GeneralDatabase(String TAG){
        this.TAG = "(Super)" + TAG;
    }

    /*Protected Methods*/

    /**
     * This Method initialises all the Control Variables to reflect that a new Call has been made
     */
    protected void startNewCall() throws ConcurrentCallsException {

        /*If there is a Call currently running, throw an Exception*/
        if(this.isCallRunning){
            throw new ConcurrentCallsException("Can not start a new Call as there is already a " +
                    "call running");
        }

        /*Set the Control Variables to reflect a new Call has started*/
        this.isCallRunning = true;
        this.isCallComplete = false;
        this.callSuccess = false;
        this.exception = null;

    }

    /**
     * Currently this Method is used to set the Control Variables to reflect
     * that the Database Call had succeeded. It will then also get and store the
     * Success value and of the Call as well as the Exception it Generated (if
     * it did generate one).
     *
     * This Method was intended to be used as a Callback to Database Calls. It
     * was meant to detect when a Call to the database has completed. However it
     * seems that there is an issue with this Callback triggering when we
     * actively wait for it. (maybe try putting this in another class that then
     * changes control variables here? May also have to do with non thread
     * something variables?)
     * @param task  The task representing the Call that was being made
     */
    @Override
    public void onComplete(@NonNull Task task) {

        Log.d(TAG, "++++onComplete Start");

        /*Update all the control variables*/
        this.isCallComplete = true;
        this.isCallRunning =false;

        /*Get the Success Status of the Call*/
        this.callSuccess = task.isSuccessful();

        /*Get the Exception the Call Generated (will be null if none were)*/
        this.exception = task.getException();

        return;

    }

    /**
     * This Method is used to wait for a Call to complete. This will block the
     * thread.
     */
    protected void waitForCallToComplete(){

        /*Initialisation*/
        int MAX_WAIT_STEP = 1000;
        int waitStep = 1;

        /*While there is a Call running that hasn't complete, wait in larger
        * steps until it reaches one second, and then wait in one Second steps*/
//        while( ! this.isCallComplete && this.isCallRunning){
        while( ! this.currentTask.isComplete()){
            Log.d(TAG, "waitForResult: " + Calendar.getInstance().getTime());
            /*Wait for the current Step time*/
            try{
                //TODO Maybe make a better solution than be busy waiting
                //noinspection BusyWait
                Thread.sleep(waitStep);
                /*Increase the Wait Step*/
                waitStep = Math.min(waitStep*2, MAX_WAIT_STEP);
            }
            /*Catch the Sleep Wakeup*/
            catch (InterruptedException e){
                continue;
            }
        }

        /*The Call has completed, so reflect so in the Variables*/
        onComplete(this.currentTask);

        return;

    }

    /*Getters*/

    protected Exception getException() throws CallNotCompleteException {
        if (this.isCallComplete) {
            return this.exception;
        }
        throw new CallNotCompleteException("Can not get the Call Success as the Call has not " +
                "completed yet");
    }

    protected boolean getCallSuccess() throws CallNotCompleteException {
        if(this.isCallComplete) {
            return this.callSuccess;
        }
        throw new CallNotCompleteException("Can not get the Call Success as the Call has not " +
                "completed yet");
    }



    /*//TODO Refactor these Methods into other classes*/


    public static String userID(String email){
        return email;
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