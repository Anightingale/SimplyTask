package com.example.simplytask.Database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * This Class is intended to handle all Database operations for the User Documents
 */
@SuppressWarnings("UnnecessaryReturnStatement")
class UserDatabase extends GeneralDatabase{

    /*Private Constants*/
    private static String TAG = UserDatabase.class.getCanonicalName();

    /**
     * This is the main Constructor for the UserDatabase Class. It is used just
     * to give the Super Class a relevant String to use as the Location for
     * Logging
     * */
    public UserDatabase(){
        super(TAG);
    }

    /**
     * This Method is a temp way of generating a userID. This will likely change
     * later or it may be confirmed as permanent logic. Who knows.
     * @param email  The email of the User we want to generate the ID of
     * @return  The Users ID
     */
    public static String userID(String email){
        return email;
    }

    /**
     * This Method is used to Add a new User to the Database. It will create a
     * call for the User to be added. If this Class is already handling a Call,
     * an Exception will be thrown.
     *
     * NOTE: Currently waitForCallToComplete() needs to be called in order for
     *      this class to detect that a Call has completed. This is due to
     *      issues getting the Call Back to work.
     * @param email  The email of the User that's being added.
     * @param password  The password of the User that's being added. //TODO pass this off to Google Auth
     * @throws ConcurrentCallsException  Throws ConcurrentCallsException if a
     *      new Call is requested to a Class that is already handling (and
     *      waiting for) a Call to Complete.
     */
    public void addUser(final String email, String password) throws ConcurrentCallsException {

        /*Assert that a new Call can be made, then set the Control Variables to
        * reflect that a new Call is being made*/
        this.startNewCall();

        /*Creates a new 'Data Object' to Encapsulate the Data for the Database*/
        Map<String, Object> data = new HashMap<>();
        data.put("Password", password);
        data.put("Email", email);

        /*Get the ID for the User being added*/
        String userID = userID(email);

        /*Make the Database Call*/
        super.currentTask = super.db.collection("User")
                .document(userID)
                .set(data);

        return;

    }



}
