package com.example.simplytask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subject_overview);
    }

    public void goToMath(View view){
        Intent intent = new Intent(this, Maths.class);
        startActivity(intent);
    }

    public void goToEnglish(View view){
        Intent intent = new Intent(this, English.class);
        startActivity(intent);
    }

    public void goToArt(View view){
        Intent intent = new Intent(this, Art.class);
        startActivity(intent);
    }

    public void goToMusic(View view){
        Intent intent = new Intent(this, Music.class);
        startActivity(intent);
    }

    public void goToPersonal(View view){
        Intent intent = new Intent(this, Personal.class);
        startActivity(intent);
    }

}
