package com.example.simplytask.Database;

import android.util.Log;

/**
 * This Class is intended to be used as an Interface for the rest of the Code to
 * Access the Database through. All Database Calls should go through this
 * Interface. This Class will then Control all the relevant logic of the
 * Database call.
 */
public class DatabaseInterface {

    /*Private Constants*/
    private static final String TAG = DatabaseInterface.class.getCanonicalName();

    /**
     * This Class is used to add new users to the Database. This Method will
     * block the main thread until the Database Call has completed and has been
     * marked as either a Success or a Failure. If the Call Succeeds, this
     * Method will return true, if it fails, then this Method will return false
     * @param email  The email address of the user to be added
     * @param password  The password of the user to be added //TODO Don't actually handle passwords and just pass that off to Google Auth
     * @return  True if the User was added successfully, False if the User
     *          failed to be added
     */
    public static boolean addUser(final String email, String password) {

        /*Initialisation*/
        Log.d(TAG, "++++ addUser Start");
        UserDatabase userDatabase = new UserDatabase();

        /*Try to Add the User to the Database and wait for it to complete*/
        try {

            /*Make the Call to add the User to the Database*/
            userDatabase.addUser(email, password);

            /*Wait for the Call to complete*/
            userDatabase.waitForCallToComplete();

            /*If the Call was not Successful and an Exception occurred, Log it*/
            Exception exception = userDatabase.getException();
            if ( (! userDatabase.getCallSuccess()) && exception != null) {
                Log.e(TAG, exception.getMessage());
            }

            /*Return the Success of the Database Call*/
            return userDatabase.getCallSuccess();

        }
        /*Catch Exceptions thrown by the UserDatabase Class*/
        catch (CallNotCompleteException | ConcurrentCallsException e) {
            /*Ignore for now as it should never happen*/
            Log.d(TAG, e.getMessage());
            e.printStackTrace();
        }

        /*If we get here, something went wrong so return false*/
        return false;

        //TODO add worker
        //TODO add manager
    }

}
