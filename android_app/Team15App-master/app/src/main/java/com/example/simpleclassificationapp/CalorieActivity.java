package com.example.simpleclassificationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

public class CalorieActivity extends AppCompatActivity {
    TextView calories_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie);
        calories_text = findViewById(R.id.calories_textview);

        Intent intent = getIntent();
        String predicted_dish = intent.getStringExtra("classificationResult");
        String calories = intent.getStringExtra("caloriesResult");

        String output = "Your dish was predicted as \n" + predicted_dish + " \nand it has \n" +  calories + " calories / 100 grams";
        calories_text.setText(output);

        Button back = findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}