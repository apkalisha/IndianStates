package com.indian.states.capitals.indianstates;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class QuizMainActivity extends AppCompatActivity {
    TextView textView;
    private DatabaseReference databaseReference;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_main);
        textView = findViewById(R.id.text);
        Intent intent = getIntent();
        textView.setText(intent.getStringExtra("categoryName"));
        String CatergoryNo=intent.getStringExtra("category_no");

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        mRef.keepSynced(true);

    }
}
