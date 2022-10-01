package com.example.simplytask.Database;

import android.util.Log;

public class DatabaseInterface {

    public static final String TAG = DatabaseInterface.class.getCanonicalName();

    public static void addUser(final String email, String password) {
        Log.d(TAG, "++++ addUser Start");
        UserDatabase userDatabase = new UserDatabase();
        boolean userAdded = userDatabase.addUser(email, password);
        Log.d(TAG, "++++ addUser: " + userAdded);
        if( ! userAdded){
            Log.e(TAG, userDatabase.getException());
        }
        //add worker
        //add manager
    }

}
