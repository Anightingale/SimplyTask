package com.example.simplytask.Database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

class UserDatabase implements OnCompleteListener {

    private static String TAG = UserDatabase.class.getCanonicalName();

    private boolean isCallComplete = false;
    private boolean result = false;
    private Exception exception = null;

    public UserDatabase(){

    }

    public String userID(String email){
        return email;
    }

    public boolean addUser(final String email, String password) {

        Map<String, Object> data = new HashMap<>();
        final String userID = userID(email);

        data.put("Password", password);
        data.put("Email", email);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db
                .collection("User")
                .document(userID)
                .set(data)
//                .add(data)
                .addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Log.d(TAG, "onSuccess L");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure LL");
                    }
                })
                .addOnCompleteListener(this);
        while( ! this.isCallComplete){
            Log.d(TAG, "wait: " + Calendar.getInstance().getTime());
            try{
                Thread.sleep(1000);
            }
            catch (InterruptedException e){
                continue;
            }
        }
        return this.result;
    }

    @Override
    public void onComplete(@NonNull Task task) {
        Log.d(TAG, "onComplete Start");
        this.isCallComplete = true;
        this.result = task.isSuccessful();
        this.exception = task.getException();
    }


    public String getException(){
        if(this.exception != null){
            return this.exception.getMessage();
        }
        return "";
    }

}
