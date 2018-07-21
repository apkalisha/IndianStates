package com.indian.states.capitals.indianstates;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
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
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private Integer mScore = 0;
    TextView score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);
        Button startBtn = findViewById(R.id.btn_start);
        Button resetBtn = findViewById(R.id.btn_reset);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Quiz").child(category);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabase = mFirebaseDatabase.getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        mDatabase.keepSynced(true);
        getHighScore();

        score = findViewById(R.id.highScore);

       loadQuestions();

       startBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Intent intent = new Intent(HighScoreActivity.this,QuizMainActivity.class);
               startActivity(intent);
               finish();
           }
       });
       resetBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               mDatabase.child("HighScore").child(category).child("Score").removeValue();
               score.setText("0");
           }
       });

    }

    public void getHighScore(){
        mDatabase.child("HighScore").child(category).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("Score")) {
                        mScore = dataSnapshot.child("Score").getValue(Integer.class);
                    score.setText(String.valueOf(mScore));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
