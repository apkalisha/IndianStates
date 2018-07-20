package com.indian.states.capitals.indianstates;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;

import static com.indian.states.capitals.indianstates.Questions.category;
import static com.indian.states.capitals.indianstates.Questions.questionList;

public class HighScoreActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);
        Button startBtn = findViewById(R.id.btn_start);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Quiz").child(category);

       loadQuestions();

       startBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(HighScoreActivity.this,QuizMainActivity.class);
               startActivity(intent);
               finish();
           }
       });

    }

    private void loadQuestions() {

        if(questionList.size() > 0) {

            questionList.clear();
        }
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Quiz ques =  postSnapshot.getValue(Quiz.class);
                    questionList.add(ques);
                }

                Collections.shuffle(questionList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        databaseReference.addValueEventListener(valueEventListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        databaseReference.removeEventListener(valueEventListener);
    }
}
