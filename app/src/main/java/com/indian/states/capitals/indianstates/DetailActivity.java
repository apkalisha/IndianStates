package com.indian.states.capitals.indianstates;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        /*mTextView = findViewById(R.id.tv_detail);*/

        /*Intent intent = getIntent();
        if(intent.hasExtra("State")) {
            mTextView.setText(intent.getStringExtra("State"));
        }*/
    }
}
